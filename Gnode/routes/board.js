const express = require('express');
const router = express.Router();
const { v4: uuidv4 } = require('uuid')
const fs = require('fs')
const path = require('path');
const db = require('../config/database');
const isNullOrUndefined = (value) => value === null || value === undefined;
const isStringValueEmpty = (value) => typeof value === "string" && value.trim().length === 0;
const conn = db.init();
db.connect(conn);

router.post('/', (req, res) => {
	if (isNullOrUndefined(req.body) || isStringValueEmpty(req.body)) {
		res.status(400).send('Invalid data');
		return;
	}

	let sql = `SELECT board.*, COUNT(likes.board_id) as like_count
			   FROM board
			   LEFT JOIN likes ON board.board_id = likes.board_id
			   GROUP BY board.board_id`;

	conn.query(sql, (err, rows) => {
		if (err) {
			console.log(err);
			res.send('Fail');
		} else {
			res.send(rows);
		}
	});
});


function encodeImageToBase64(filePath, callback) {
	fs.readFile(filePath, (err, data) => {
		if (err) {
			callback(err, null);
		} else {
			const base64Image = new Buffer.from(data).toString('base64');
			callback(null, base64Image);
		}
	});
}

router.get('/detail/:id', (req, res) => {
	const board_id = req.params.id;

	if (isNullOrUndefined(board_id) || isStringValueEmpty(board_id)) {
		res.status(400).send('Invalid data');
		return;
	}

	let sql = `SELECT board.board_id, board.title, board.content, board.image_url,
			   board.date_created, user.nickname
			   FROM board
			   INNER JOIN user ON board.user_id = user.user_id
			   WHERE board.board_id = ?`;

	conn.query(sql, [board_id], (err, boardResult) => {
		if (err) {
			console.log(err);
			res.send('Fail');
		} else {
			if (boardResult && boardResult[0] && boardResult[0].image_url && boardResult[0].image_url.length > 0) {
				const imgPath = path.join(__dirname, '..', 'public', 'img', 'board', boardResult[0].image_url + '.jpg');
        
				fs.exists(imgPath, (exists) => {
					if (exists) {
						encodeImageToBase64(imgPath, (err, base64Image) => {
							if (err) {
								console.error(err);
								res.send('Fail');
							} else {
								boardResult[0].image_url = base64Image;
								res.send(boardResult[0]);
							}
						});
					} else {
						console.log('Image not found on the server');
						boardResult[0].image_url = null;
						res.send(boardResult[0]);
					}
				});
			} else {
				res.send(boardResult[0]);
			}
		}
	});
});

router.get('/filepath/:fileId', (req, res, next) => {
	const fileId = req.params.fileId;

	if (isNullOrUndefined(fileId) || isStringValueEmpty(fileId)) {
		res.status(400).send('Invalid data');
		return;
	}

	const imgPath = path.join(__dirname, '..', 'public', 'img', 'board', req.params.fileId + '.jpg');
	res.sendFile(imgPath);
});

router.get('/detail/:id/comments', (req, res) => {
	const board_id = req.params.id;

	if (isNullOrUndefined(board_id) || isStringValueEmpty(board_id)) {
		res.status(400).send('Invalid data');
		return;
	}

	const sql = `SELECT comments.comment_id, comments.content, comments.date_created,
				 user.nickname
				 FROM comments
				 INNER JOIN user ON comments.user_id = user.user_id
				 WHERE comments.board_id = ?
				 ORDER BY comments.date_created ASC`;

	conn.query(sql, [board_id], (err, commentsResult) => {
		if (err) {
			console.log(err);
			res.status(500).send('Fail');
		} else {
			res.send(commentsResult);
		}
	});
});

router.get('/detail/:board_id/:user_id/:is_bookmarked/book', (req, res) => {
	const board_id = req.params.board_id;
	const user_id = req.params.user_id;
	const is_bookmarked = req.params.is_bookmarked == 'true';

	if (isNullOrUndefined(board_id) || isNullOrUndefined(user_id) ||
	isStringValueEmpty(board_id) || isStringValueEmpty(user_id)) {
		res.status(400).send('Invalid data');
		return;
	}

	if (is_bookmarked) {
		// 즐겨찾기 추가
		const sql = 'INSERT INTO favorites (board_id, user_id) VALUES (?, ?)';
		conn.query(sql, [board_id, user_id], (err) => {
			if (err) {
				console.log(err);
				res.status(500).send('Fail');
			} else {
				res.send('Success');
			}
		});
	} else {
		// 즐겨찾기 제거
		const sql = 'DELETE FROM favorites WHERE board_id = ? AND user_id = ?';
		conn.query(sql, [board_id, user_id], (err) => {
			if (err) {
				console.log(err);
				res.status(500).send('Fail');
			} else {
				res.send('Success');
			}
		});
	}
});

router.get('/detail/:board_id/:user_id/:is_bookmarked/like', (req, res) => {
	const board_id = req.params.board_id;
	const user_id = req.params.user_id;
	const is_bookmarked = req.params.is_bookmarked == 'true';

	if (is_bookmarked) {
		// 좋아요 추가
		const sql = 'INSERT INTO likes (board_id, user_id) VALUES (?, ?)';
		conn.query(sql, [board_id, user_id], (err) => {
			if (err) {
				console.log(err);
				res.status(500).send('Fail');
			} else {
			res.send('Success');
			}
		});
	} else {
		// 좋아요 제거
		const sql = 'DELETE FROM likes WHERE board_id = ? AND user_id = ?';
		conn.query(sql, [board_id, user_id], (err) => {
			if (err) {
				console.log(err);
				res.status(500).send('Fail');
			} else {
				res.send('Success');
			}
		});
	}
});

router.post('/write', (req, res) => {
	console.log(req.body);
	let { user_id, title, content, image_url, category } = JSON.parse(req.body.board)
	const uuid = uuidv4()

	if(image_url==""){

		let sql = "insert into board values(null, ?, ?, ?, '', ?, now())"

		conn.query(sql, [ user_id, title, content, category ], (err, rows) => {
			if (err) {
				console.log(err)
				res.send("Fail")
			} else {
				res.send("Success")
			}
		})

	}else{
		let decode = Buffer.from(image_url, 'base64')
		fs.writeFileSync('public/img/board/' + uuid + '.jpg', decode)
		let sql = "insert into board values(null, ?, ?, ?, ?, ?, now())"

		conn.query(sql, [ user_id, title, content, uuid, category ], (err, rows) => {
			if (err) {
				console.log(err)
				res.send("Fail")
			} else {
				res.send("Success")
			}
		})
	}
})

router.post('/update', (req, res) => {
	console.log(req.body)
	let { title, content, board_id, image_url } = JSON.parse(req.body.board)
	const uuid = uuidv4()

	if(image_url=="" || image_url==null){
		let sql = 'update board set title=?, content=?, image_url=? where board_id=?'
		conn.query(sql, [title, content, uuid, board_id], function (err, rows) {
			if (err) {
				console.log(err)
				res.send('Fail')
			} else {
				if (rows != null) {
					res.send("Success")
				} else {
					res.send("Update Fail")
			}}
		})
	}else{
		let decode = Buffer.from(image_url, 'base64')
		fs.writeFileSync('public/img/board/' + uuid + '.jpg', decode)

		let sql = 'update board set title=?, content=?, image_url=? where board_id=?'
		conn.query(sql, [title, content, uuid, board_id], function (err, rows) {
			if (err) {
				console.log(err)
				res.send('Fail')
			} else {
				if (rows != null) {
					res.send("Success")
				} else {
					res.send("Update Fail")
				}
			}
		})
	}
})

router.post('/detail/:board_id/newcomment/:userid', (req, res) => {
	const board_id = req.params.board_id;
	const user_id = req.params.userid;
	const { content } = req.body;

	const sql = 'INSERT INTO comments (board_id, user_id, content) VALUES (?, ?, ?)';

	conn.query(sql, [board_id, user_id, content], (err) => {
		if (err) {
			console.log(err);
			res.status(500).send('fail');
		} else {
			res.send('ok');
		}
	});
});


// 게시물 삭제
router.delete('/detail/:board_id/delete', (req, res) => {
	const board_id = req.params.board_id;

	const sql = 'DELETE FROM board WHERE board_id = ?';

	conn.query(sql, [board_id], (err) => {
		if (err) {
			console.log(err);
			res.status(500).send('Fail');
		} else {
			res.send('Success');
		}
	});
});


module.exports = router;