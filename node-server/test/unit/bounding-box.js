const chai = require('chai');
const sinon = require('sinon');
const expect = chai.expect;
const bounding_box = require('../../utils/bounding-box');

describe('bounding-box', () => {
	describe('Erroneous parameters', () => {
		it('Geopoint should throw error message if null params');
		it('Geopoint should throw error if invalid lat');
		it('Geopoint should throw error if invalid long');
		it('Geopoint should throw error if lat out of bounds');
		it('Geopoint should throw error if long out of bounds');
		it('boundingCoordinates should throw error if invalid distance');

	});
	describe('Good parameters', () => {

		it('should return an object');
	});
});
