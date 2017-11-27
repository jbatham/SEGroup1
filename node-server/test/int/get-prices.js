let chai = require('chai');
// let chaiHttp = require('chai-http');
let get_location_radius = require('../../controllers/get-prices');
let expect = chai.expect();

describe('Price controller integrations', () => {
	describe('Erroneous requests', () => {
		it('should respond with error 1');
		it('should respond with error 2 etc');
	});
	describe('Good requests', () => {
		it('should respond with results based on good request 1');
		it('should respond with results based on good request 2 etc');
	});
});
