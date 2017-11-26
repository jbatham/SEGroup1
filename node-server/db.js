// ** Handles connection to the mysql database **
const mysql = require('mysql');
const async = require('async');
require('dotenv').config();

var pool = null;

exports.connect = function(done) {
	pool = mysql.createPool({
		host: process.env.NODE_ENV == 'test' ? process.env.TEST_DATABASE_HOST : process.env.DATABASE_HOST,
		user: process.env.NODE_ENV == 'test' ? process.env.TEST_DATABASE_USER : process.env.DATABASE_USER,
		password: process.env.NODE_ENV == 'test' ? process.env.TEST_DATABASE_PASSWORD : process.env.DATABASE_PASSWORD,
		database: process.env.NODE_ENV == 'test' ? process.env.TEST_DATABASE_NAME : process.env.DATABASE_NAME
	});

	done();
}

exports.get = function(){
	return pool;
}
