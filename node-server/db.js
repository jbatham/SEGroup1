// ** Handles connection to the mysql database **
const mysql = require('mysql');
const async = require('async');
require('dotenv').config();

var pool = null;

// TODO: move the db config details to a dotenv file
exports.connect = function(done) {
	pool = mysql.createPool({
		host: process.env.DATABASE_HOST,
		user: process.env.DATABASE_USER,
		password: process.env.DATABASE_PASSWORD,
		database: process.env.DATABASE_NAME
	});

	done();
}

exports.get = function(){
	return pool;
}
