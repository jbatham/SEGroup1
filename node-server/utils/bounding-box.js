// Function to compute the required points on the circumference of the circle
// with radius 'distance' from center point at 'lat','long'
var Geopoint = require('geopoint');

exports.bounding_box = function(params) {

	if (params == null) return 'Error: no params object';
	lat = parseFloat(params.lat);
	long = parseFloat(params.long);
	distance = parseFloat(params.distance);
	// console.log(lat,long,distance);
	curLocation = new Geopoint(lat,long);
	if (curLocation instanceof Error) {
		console.log('**** right ere'. curLocation.message);
		return curLocation.message;
	}
	boundingCoords = curLocation.boundingCoordinates(distance, inKilometers=true);
	if (boundingCoords instanceof Error) return boundingCoords;

	boundingBox = {
		'minLat': boundingCoords[0]._radLat,
		'minLong': boundingCoords[0]._radLon,
		'maxLat': boundingCoords[1]._radLat,
		'maxLong': boundingCoords[1]._radLon,
	}

	// console.log(boundingBox);
	return boundingBox;
}
