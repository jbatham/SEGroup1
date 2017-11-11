// Connects to the mysql database
const mysql = require('mysql');
const async = require('async');

var state = {
	pool: null;
}

exports.connect = function(done) {
	state.pool = mysql.createPool({
		host: 'localhost',
		user: 'ase1',
		password: 'rainforest12',
		database: 'android'
	});

	done();
}

exports.get = function(){
	return state.pool;
}
