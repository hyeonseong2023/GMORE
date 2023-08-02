const express = require('express');
const app = express();
const indexRouter = require('./routes/board');
const favoritesRouter = require('./routes/favorites'); // 대/소문자를 일치시키세요

app.use(express.urlencoded({ extended: true }));
app.use('/board', indexRouter);
app.use('/favorites', favoritesRouter); // 수정된 변수를 사용하세요
app.set('port', process.env.PORT || 8888);
app.listen(app.get('port'), () => {
  console.log(app.get('port'), '번 포트에서 서버연결 기다리는 중...');
});
