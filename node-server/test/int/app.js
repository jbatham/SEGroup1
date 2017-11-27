let chai = require('chai');
var request = require('request');
// let chaiHttp = require('chai-http');
let server = require('../../app');
let expect = chai.expect();

describe('Server integrations', () => {
	it('should respond to a basic GET request', function() {
		request
			.get('http://ec2-35-176-136-57.eu-west-2.compute.amazonaws.com:/')
			.on('response', function(response) {
				expect(response.statusCode).to.equal(200);
				expect(response.headers['content-type']).to.equal('text/html; charset=utf-8');
			});
	});
	it('should connect to db without errors');
	it('server should be listening');
});
