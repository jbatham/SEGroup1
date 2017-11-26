const chai = require('chai');
const sinon = require('sinon');
const expect = chai.expect;
const get_location_radius = require('../../controllers/get-prices').get_location_radius;
const prices = require('../../models/prices');


describe('get_location_radius', () => {
	describe('get_data callback function', () => {
		var stub = null; var request = {}; var response = {};
		before(function() {
			stub = sinon.stub(prices,'get_data');
			request = {
				query: {
					lat: 50.8388481140,
					long: -0.1175390035,
					distance: 2
				}
			};
			response = {};
		});
		it('should call prices.get_data', function(done) {
			get_location_radius(request, response);
			var callCount = stub.callCount;
			expect(callCount).equals(1);
			done();
		});
	});
});
