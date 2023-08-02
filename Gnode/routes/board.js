const express = require('express');
const router = express.Router();
const db = require('../config/database');

const conn = db.init();
db.connect(conn);

router.post('/', (req, res) => {
  let sql = 'SELECT * FROM board';
  conn.query(sql, (err,rows)=>{
        if(err){
            console.log(err);
            res.send('Fail');
        }else{
            res.send(rows);
        }
  });
});




module.exports = router;
