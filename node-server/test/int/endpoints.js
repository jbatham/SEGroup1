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
				done();
			});
	});
	it('should respond with error to a request at incorrect port', function(done) {
		var wrongPort = PORT - 1;
		chai.request(`http://localhost:${wrongPort}`)
			.get('/')
			.end(function(err, response) {
				expect(err).to.not.be.null;
				expect(err.errno).to.equal('ECONNREFUSED');
				done();
			});
	});
	it('should respond with 404 to invalid endpoint', function(done) {
		chai.request(`http://localhost:${PORT}`)
			.get('/nothing')
			.end(function(err, response) {
				expect(err).to.not.be.null;
				expect(err.status).to.equal(404);
				done();
			});
	});
	it.skip('should get a response from the db request', function(done) {
		chai.request(`http://localhost:${PORT}`)
			.post()
	});
	it('server should be listening');
});
