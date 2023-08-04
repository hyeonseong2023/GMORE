const express = require('express')
const router = express.Router()
const db = require('../config/database');

const conn = db.init();
db.connect(conn);

var tb_name = "user"

router.get('/checkID/:id', (req, res) => {
	console.log("아이디 체크")
	let id = req.params.id
	let sql = 'SELECT * FROM '+ tb_name +' where email = ?';
	

	conn.query(sql,[id], (err,rows)=>{
		if(err){
			console.log("실팽", err, "실팽");
			res.send('Fail');
		}else{
			// 빈 문자열 -> 없는 이메일 
			if(rows=='') {
				console.log("문자열 없음", rows)
				// console.log('빈 문자열')
				res.send("이메일 없음");
			}else { // 빈 문자열 아님 -> 있는 이메일
				console.log("문자열 있음", rows)
				res.send("이메일 있음")
			}
		}
	});
});

router.get('/checkNick/:nick', (req, res)=>{
	let nick = req.params.nick
	let sql = 'select * from '+ tb_name +' where nickname = ?'

	conn.query(sql, [nick], (err, rows) => {
		if(err) {
			console.log("실패!", err)
			res.send("Fail")
		} else {
			console.log("성공", rows)
			res.send(rows);
		}
	})
})

router.post('/join', (req, res)=>{
	console.log("res.body는 ? ", req.body)
	console.log("res.body는 ? ", req.body.JoinMember)
	let {id, pw, nick, img} = JSON.parse(req.body.JoinMember) 
	console.log("id ? ", id)

	let sql = 'insert into '+ tb_name +" (email, password, nickname, user_type, image_url) values (?, ?, ?, 'u', null)"
	
	conn.query(sql, [id, pw, nick], (err,rows) => {
		if(err) {
			console.log("실패", err)
			res.send("Fail")
		} else {
			if(rows.affectedRows>0) {
				console.log("로그인 성공", rows)
				res.send("로그인 성공");
			} else {
				console.log("회원가입 실패")
				res.send("Fail")
			}
			
		}
	})
})

router.post('/login', (req, res) => {
	console.log("로그인 req.body는? ", req.body.LoginMember);
	let { id, pw } = JSON.parse(req.body.LoginMember);
	let sql = 'select user_id as id, nickname as nick from ' + tb_name + ' where email = ? and password = ?';

	conn.query(sql, [id, pw], (err, rows) => {
		console.log(rows);
		if (err) {
			console.log("실패", err);
			res.send("Fail");
		} else {
			if (rows == '') { // 없는 정보 -> 로그인 실패
				res.send("fail");
			} else { //-> 로그인 성공
				// Array로 반환된 rows를 JSON 객체 형태로 변환합니다.
				const responseObject = { members: rows };
				res.send(responseObject);
			}
		}
	});
});


router.get('/getuserid/:email', (req, res)=>{
	let email = req.params.email
	let sql = 'select user_id, nickname from user where email = ?'

	conn.query(sql, [email], (err, rows) => {
		if(err) {
			console.log("실패", err)
			res.send("Fail");
		} else {
			if(rows == '') {
				res.send("Fail")
			} else {
				console.log("getuserid", rows)
				res.send(rows)
			}
		}		
	})	
})

module.exports = router