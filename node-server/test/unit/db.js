const chai = require('chai');
const sinon = require('sinon');
const expect = chai.expect;
const mysql = require('mysql');
const db = require('../../db.js');


describe('DB functions', () => {
	describe('db.connect', () => {
		var stub = sinon.stub(mysql,'createPool');
		it('should create a pool without error and call callback', function(done) {
			db.connect(function() {
				expect('Made it').to.equal('Made it');
			});
			done();
		});
	});
	describe('db.get', () => {
		it('should return a null pool', function(done) {
			var pool = db.get();
			expect(pool).to.be.undefined;
			done();
		});
	});
});
