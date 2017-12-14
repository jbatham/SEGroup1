const chai = require('chai');
const chaiHttp = require('chai-http');
// const server = require('../../app');
const expect = chai.expect;
chai.use(chaiHttp);
require('dotenv').config();

describe('Server integrations', () => {

	var PORT = process.env.TEST_PORT;

	it('should respond to a basic GET request', function(done) {
		chai.request(`http://localhost:${PORT}`)
			.get('/')
			.end(function(err, response) {
				expect(response.statusCode).to.equal(200);
				expect(response.headers['content-type']).to.equal('text/html; charset=utf-8');
			});
		done();
	});
	it('should respond with error to a request at incorrect port', function(done) {
		var wrongPort = PORT - 1;
		chai.request(`http://localhost:${wrongPort}`)
			.get('/')
			.end(function(err, response) {
				expect(err).to.not.be.null;
				expect(err.errno).to.equal('ECONNREFUSED');
			});
		done();
	});
	it('should respond with 404 to invalid endpoint', function(done) {
		chai.request(`http://localhost:${PORT}`)
			.get('/nothing')
			.end(function(err, response) {
				expect(err).to.not.be.null;
				expect(err.status).to.equal(404);
			});
		done();
	});
	it('should get a response from the db request', function(done) {
		chai.request(`http://localhost:${PORT}`)
			.post('/price-data/get')
			.set('content-type', 'application/x-www-form-urlencoded')
			.query({lat:50.8388481140, long:-0.1175390035, distance:3})
			.end(function(err, response) {
				expect(response.body).to.have.length(5);
				expect(response.body[0]).to.have.property('postcode');
				expect(response.body[0]).to.have.property('lat');
				expect(response.body[0]).to.have.property('lng');
				expect(response.body[0]).to.have.property('price');
				expect(response.body[0].postcode).to.equal('BN2 4EN');
			});
		done();
	});
	it('more db request stuff');
});
