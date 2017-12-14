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
			response = {
				send: function() {
					return 'error';
				},
				json: function(res) {
					return res;
				}
			};
		});
		after(function() {
			prices.get_data.restore();
		});
		it('should call prices.get_data', function(done) {
			get_location_radius(request, response);
			var callCount = stub.callCount;
			expect(callCount).equals(1);
			done();
		});
		it('should send error in callback of prices.get_data if err', function(done) {
			stub.callsArgWith(2,'Error');
			var spy = sinon.spy(response,'send');
			get_location_radius(request, response);
			var called = spy.called;
			var returned = spy.returned('error');
			expect(called).equals(true);
			expect(returned).equals(true);
			done();
		});
		it('should send json in callback of prices.get_data if no err', function(done) {
			var res = {
				foo: 'bar',
				foo2: 'bar2'
			}
			stub.callsArgWith(2,null,res);
			var spy = sinon.spy(response,'json');
			var JSONStringSpy = sinon.spy(JSON,'stringify');
			var JSONParseSpy = sinon.spy(JSON,'parse');
			get_location_radius(request, response);
			var called = spy.called;
			var stringCalled = JSONStringSpy.called;
			var parseCalled = JSONParseSpy.called;
			var returned = spy.returned({
				foo: 'bar',
				foo2: 'bar2'
			});
			expect(called).equals(true);
			expect(stringCalled).equals(true);
			expect(parseCalled).equals(true);
			expect(returned).equals(true);
			done();
		});
	});
});
