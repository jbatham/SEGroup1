// ** Basic server setup **
const db = require('./db');
const express = require('express');

const app = express()

app.use('/get-prices', './controllers/get-prices')

db.connect(function(err) {
	if (err) {
		console.log('Unable to connect to the database');
	} else {
		app.listen(80, function() {
			console.log('Listening on port 80..');
		})
	}
})
