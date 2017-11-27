const chai = require('chai');
const sinon = require('sinon');
const expect = chai.expect;
const bounding_box = require('../../utils/bounding-box').bounding_box;
const Geopoint = require('geopoint');

describe('bounding-box', () => {
	describe('Erroneous parameters', () => {
		it('Geopoint should throw error message if null params', function() {
			var val = bounding_box(null);
			expect(val).to.equal('Error: no params object');
		});
		it('Geopoint should throw error if invalid lat', function() {
			var params = {
				lat: 'silly',
				long: '-0.1175390035',
				distance: '2'
			};

			try {
				bounding_box(params);
			} catch (e) {
				expect(e.message).to.equal('Invalid latitude');
			}
		});
		it('Geopoint should throw error if invalid long', function() {
			var params = {
				lat: '50.8388481140',
				long: 'silly',
				distance: '2'
			};

			try {
				bounding_box(params);
			} catch (e) {
				expect(e.message).to.equal('Invalid longitude');
			}
		});
		it('Geopoint should throw error if lat out of bounds', function() {
			var params = {
				lat: '100000',
				long: '-0.1175390035',
				distance: '2'
			};

			try {
				bounding_box(params);
			} catch (e) {
				expect(e.message).to.equal('Latitude out of bounds');
			}
		});
		it('Geopoint should throw error if long out of bounds', function() {
			var params = {
				lat: '50.8388481140',
				long: '100000',
				distance: '2'
			};

			try {
				bounding_box(params);
			} catch (e) {
				// console.log(e.message + ' **\n');
				expect(e.message).to.equal('Longitude out of bounds');
			}
		});
		it('boundingCoordinates should throw error if invalid distance', function() {
			var params = {
				lat: '50.8388481140',
				long: '-0.1175390035',
				distance: 'silly'
			};

			try {
				bounding_box(params);
			} catch (e) {
				// console.log(e.message + ' **\n');
				expect(e.message).to.equal('Invalid distance');
			}
		});

	});
	describe('Good parameters', () => {
		it('should return an object', function() {
			var params = {
				lat: '50.8388481140',
				long: '-0.1175390035',
				distance: '2'
			};

			var lat = parseFloat(params.lat);
			var long = parseFloat(params.long);
			var testLocation = new Geopoint(lat,long);
			var latRads = testLocation._radLat;
			var longRads = testLocation._radLon;

			var bb = bounding_box(params);
			expect(bb).to.have.own.property('minLat');
			expect(bb).to.have.own.property('minLong');
			expect(bb).to.have.own.property('maxLat');
			expect(bb).to.have.own.property('maxLong');
		});
	});
});
