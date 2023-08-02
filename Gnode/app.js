const express = require('express');
const app = express();
const indexRouter = require('./routes/board')
const memberRouter = require('./routes/member')

app.use(express.urlencoded({extended:true}))
app.use('/board', indexRouter)
app.use('/member', memberRouter)

app.set('port', process.env.PORT || 8888);
app.listen(app.get('port'),()=>{
    console.log(app.get('port'), '번 포트에서 서버연결 기다리는 중...');
})