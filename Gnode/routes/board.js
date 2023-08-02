const express = require('express');
const router = express.Router();
const db = require('../config/database');

const conn = db.init();
db.connect(conn);

// 기존 코드를 유지합니다.
router.post('/', (req, res) => {
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


router.get('/detail/:id', (req, res) => {
  const board_id = req.params.id;
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
      res.send(boardResult[0]);
    }
  });
});


router.get('/detail/:id/comments', (req, res) => {
  const board_id = req.params.id;

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


// 게시글 작성
router.post('/write', (req, res) => {
  console.log(req.body);

  // let { title, content, user_id, img } = JSON.parse(req.body.board)

  // let decode = Buffer.from(img, 'base64')

  // const uuid = uuidv4()
  // fs.writeFileSync('public/img/board/' + uuid + '.jpg', decode)


  // let sql = "insert into andboard values (null, ?, ?, ?, ?, null)"  // 테이블 확인하고 고치기

  // conn.query(sql, [title, content, user_id, uuid], (err, rows) => {
  //   if (err) {
  //     console.log(err)
  //     res.send("Fail")
  //   } else {
    
  //     console.log(rows[0].img)
  //     let readFile = fs.readFileSync('public/img/board/' + rows[0].img + '.jpg'); //이미지 파일 읽기
  //     let encode = Buffer.from(readFile).toString('base64'); //파일 인코딩
  //     res.send(encode)
  //     res.send("Success")
  //   }
  // })
})

// 게시글 수정
router.post('/update', (req, res) => {
  console.log(req.body)
  let { title, content, board_id } = JSON.parse(req.body.board)

  let sql = 'update board set title=?, content=? where board_id=?'
  conn.query(sql, [title, content, board_id], function (err, rows) {
    if (err) {
      // 오류 발생
      console.log(err)
      res.send('Fail')
    } else {
      // console.log(rows)
      if (rows.length > 0) {
        res.send("Success")
      } else {
        res.send("Update Fail")
      }
    }
  })
})

module.exports = router;

