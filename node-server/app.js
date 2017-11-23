// ** Basic server setup **
const db = require('./db');
const express = require('express');
const price_routes = require('./routes/price-data.js')
require('dotenv').config();

const PORT = process.env.PORT;
const app = express();

app.use('/price-data', price_routes);

db.connect(function(err) {
	console.log('> Trying to connect to db..');
	if (err) {
		console.log('> Unable to connect to the database!');
		process.exit(1);
	} else {
		console.log('  Connected!')
		app.listen(PORT, function(err) {
			console.log("> Connecting to server..");
			if (err) {
				console.log('> Unable to start the server!');
			}
			console.log('  Connected!');
			console.log('> Listening on port ' + PORT + '..');
		})
	}
})
