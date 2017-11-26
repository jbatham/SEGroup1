const chai = require('chai');
const sinon = require('sinon');
const expect = chai.expect();
const get_location_radius = require('../../controllers/get-prices.js');

describe('get_location_radius', () => {
	describe('get_data callback function', () => {
		it('should send an error if there is an err', () => {
			var stub = sinon.stub('prices','get_data').throws('DB TEST');
			// var =
			get_location_radius
		});
		it('should send a json of the result if no err');
	});
});
