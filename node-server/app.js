// ** Basic server setup **
const db = require('./db');
const express = require('express');
const price_routes = require('./routes/price-data')
require('dotenv').config();

var server = null;
const PORT = process.env.NODE_ENV == 'test' ? process.env.TEST_PORT : process.env.PORT;
const app = express();

app.use('/price-data', price_routes);
app.get('/', function(req,res) {
	res.send('Server Online');;
});

db.connect(function(err) {
	console.log('> Trying to connect to db..');
	if (err) {
		console.log('> Unable to connect to the database!');
		// throw new Error('DB CONNECT ERROR');
	} else {
		console.log('  Connected!')
		app.listen(PORT, function(err) {
			console.log("> Connecting to server..");
			if (err) {
				console.log('> Unable to start the server!');
				// throw new Error('SERVER START ERROR');
			}
			console.log('  Connected!');
			console.log('> Listening on port ' + PORT + '..');
		})
	}
});
