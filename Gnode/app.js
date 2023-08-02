const express = require('express');
const app = express();
const boardRouter = require('./routes/board')
const memberRouter = require('./routes/member')
const indexRouter = require('./routes')
const favoritesRouter = require('./routes/favorites')

app.use(express.urlencoded({ extended: true }));
app.use('/board', boardRouter);
app.use('/user', indexRouter);
app.use('/favorites', favoritesRouter); // 수정된 변수를 사용하세요
app.use('/member', memberRouter)
app.set('port', process.env.PORT || 8888);
app.listen(app.get('port'), () => {
  console.log(app.get('port'), '번 포트에서 서버연결 기다리는 중...');
});