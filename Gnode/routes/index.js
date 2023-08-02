// routes -> index.js 폴더
const express = require('express')
const router = express.Router()
const db_config = require('../config/database')
const {v4:uuidv4} = require('uuid')
const fs = require('fs')


const conn = db_config.init()
db_config.connect(conn)


router.post('/updateimg', (req, res) => {

    // 데이터 파싱해주기
    // let {img} =JSON.parse(req.body.img)
    let {email,image_url} =JSON.parse(req.body.myPageData)
    // console.log(req.body.img);
    // img파일 디코딩

    // console.log(image_url)
    let decode = Buffer.from(image_url, 'base64')

    // 랜덤한 id(중복되지 않는 랜덤한 문자열) 생성
    const uuid = uuidv4() //  바로 파일이름으로 사용
    //파일 저장되는 기본 경롤 : 프로젝트 폴더 바로 아래 (기준)
    fs.writeFileSync('public/img/user/'+uuid+'.jpg',decode)


  
     let sql = "update user set image_url=? where email=?"
    //  테스트 쿼리
    // let sql = "update user set img=?"
    conn.query(sql,[uuid,email],(err,rows)=>{
        if(err){
            console.log(err)
        }else{
            console.log("row",rows);
            res.send('이미지저장성공')
        }
    })
    //res는 한 번만
    // res.send("보드데이터OK")
})


router.post('/checknick', (req, res) => {

    console.log(req.body.changeNick)
   
    let changeNick = req.body.changeNick

   
    let sql = "select * from user where nickname=?"
    conn.query(sql,[changeNick],(err,rows)=>{
        if(err){
            console.log(err)
        }else{
            if(rows.length>0){
            res.send('닉네임중복')
            }else{
            res.send('닉네임중복XX')
            }
        }
    })
    //res는 한 번만
    // res.send("보드데이터OK")
})


router.post('/editmypage', (req, res) => {

console.log("춘식이랑고구마",JSON.parse(req.body.editPageData))
console.log("춘식이랑비밀번호",JSON.parse(req.body.editPageData).password)
console.log("춘식이랑닉네임",JSON.parse(req.body.editPageData).nick)

    // let email=JSON.parse(req.body.editPageData).email
    // let password =JSON.parse(req.body.editPageData).password
    // let nick =JSON.parse(req.body.editPageData).nick
    let{email,password,nick}=JSON.parse(req.body.editPageData)
 
    let sql
    let sendData

    // JSON.parse(req.body.editPageData).
    if(password==null){
        sql = "update user set nickname=? where email=?"
        sendData =[nick,email]
    }else if(nick==null){
        sql = "update user set password=? where email=?"
        sendData =[password,email]
    }else{
        sql = "update user set password=?,nickname=? where email=?"
        sendData =[password,nick,email]
    }
  
   
 
    conn.query(sql,sendData,(err,rows)=>{
        console.log("edit페이지",rows)
        if(err){
            console.log(err)
        }else{
            console.log("row",rows);
            res.send('회정정보 수정 성공')
        }
    })

})

module.exports = router