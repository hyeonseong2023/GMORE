const mysql = require('mysql2');
const db_info = {
  host: 'project-db-campus.smhrd.com',
  port: '3307',
  user: 'campus_23SW_FS_hack_1',
  password: 'smhrd1',
  database: 'campus_23SW_FS_hack_1',
};

module.exports = {
  init: function () {
    return mysql.createConnection(db_info);
  },
  connect: function (conn) {
    conn.connect(function (err) {
      if (err) console.log('연결 실패' + err);
      else console.log('연결 성공!');
    });
  },
};
