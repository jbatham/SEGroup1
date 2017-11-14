// ** Handles connection to the mysql database **
const mysql = require('mysql');
const async = require('async');

var state = {
	pool: null
}

// TODO: move the db config details to a dotenv file
exports.connect = function(done) {
	state.pool = mysql.createPool({
		host: 'localhost',
		user: 'root',
		password: '',
		database: 'android',
	});

	done();
}

exports.get = function(){
	return state.pool;
}
