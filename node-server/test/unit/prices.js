const chai = require('chai');
const sinon = require('sinon');
const expect = chai.expect;
const get_data = require('../../models/prices.js').get_data;
const db = require('../../db');

describe('get_data', () => {
	it('should return an error message for undefined db', function() {
		var val = get_data(null,null,null);
		expect(val).to.equal('Error in getting database config');
	});
});
