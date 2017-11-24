// Function to compute the required points on the circumference of the circle
// with radius 'distance' from center point at 'lat','long'
var Geopoint = require('geopoint');

exports.bounding_box = function(params) {

	lat = parseFloat(params.lat);
	long = parseFloat(params.long);
	distance = parseFloat(params.distance);
	console.log(lat,long,distance);
	curLocation = new Geopoint(lat,long)
	boundingCoords = curLocation.boundingCoordinates(distance, inKilometers=true);
	// console.log(JSON.stringify(boundingBox[0]._degLat) + '\n');

	boundingBox = {
		"minLat": boundingCoords[0]._radLat,
		"minLong": boundingCoords[0]._radLon,
		"maxLat": boundingCoords[1]._radLat,
		"maxLong": boundingCoords[1]._radLon,
	}

	return boundingBox;
}
