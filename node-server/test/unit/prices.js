const chai = require('chai');
const sinon = require('sinon');
const expect = chai.expect();

describe('get_data', () => {
	it('should call db.get().query');
	it('db.get().query should call its callback');
});
