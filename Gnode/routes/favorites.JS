const express = require('express');
const router = express.Router();
const db = require('../config/database');
const connection = db.init();
db.connect(connection);

router.get('/:user_id', (req, res) => {
  const user_id = req.params.user_id;
  console.log(req.params.user_id);
  const queryString = `
    SELECT 
      board.title as categoryTitle,
      user.nickname as categoryNick,
      DATE_FORMAT(board.date_created, '%m-%d %H:%i') as categoryDate,
      count(likes.board_id) as categoryLikeCnt,
      board.board_id as categoryBoardId
    FROM favorites
    INNER JOIN board ON favorites.board_id = board.board_id
    LEFT JOIN likes ON board.board_id = likes.board_id
    LEFT JOIN user ON board.user_id = user.user_id
    WHERE favorites.user_id = ?
    GROUP BY board.board_id
  `;

  connection.query(queryString, [user_id], (err, rows) => {
    if (err) {
      console.error('Failed to query: ' + err);
      res.sendStatus(500);
      return;
    }
    else{
      console.log(rows);
      res.send(rows)
    }

  });
});

module.exports = router;
