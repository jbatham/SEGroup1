const chai = require('chai');
const sinon = require('sinon');
const expect = chai.expect;
const get_data = require('../../models/prices.js').get_data;
const db = require('../../db');

describe('get_data', () => {
	// it('should call db.get().query', function() {
	// 	var stub = sinon.stub(db,'query');
	// 	var spy = sinon.spy();
	// 	// var stub2 = sinon.stub(stub,'query');
  //
	// 	get_data(null,null,spy);
	// 	var callCountStub = stub.callCount;
	// 	var callCountSpy = spy.callCount;
	// 	console.log('stub count', callCountStub);
	// 	console.log('spy count', callCountSpy);
	// 	expect(callCount).to.equal(1);
	// });
	it('should return an error message for undefined db', function() {
		var val = get_data(null,null,null);
		expect(val).to.equal('Error in getting database config');
	});
});
