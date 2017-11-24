// ** Model for db statements related to obtaining the prices data **
const db = require('../db.js');
// const convert_to_rads = require('../utils/convert_to_rads.js');

// Takes the boundingbox parameters from the controller, and selects the set of
// db data within the distance constraints, computed via great circle calculations
// REF: http://janmatuschek.de/LatitudeLongitudeBoundingCoordinates
exports.get_data = function(params, boundingBox, done) {
	console.log('> Getting db data..');
	db.get().query(
		'SELECT * FROM BN WHERE' +
		' (RADIANS(`lat`) >= ? AND RADIANS(`lat`) <= ?) AND (RADIANS(`lng`) >= ? AND RADIANS(`lng`) <= ?)' +
		' AND' +
		' ACOS(SIN(RADIANS(?)) * SIN(RADIANS(`lat`)) + COS(RADIANS(?)) * COS(RADIANS(`lat`)) * COS(RADIANS(`lng`) - RADIANS(?))) <= ?;',
		[boundingBox.minLat, boundingBox.maxLat, boundingBox.minLong, boundingBox.maxLong,
		params.lat, params.lat, params.long, params.distance/6371],	// distance/6371 is angular radius
		function(err, result) {
			if (err) return done(err);
			done(null, result);
		}
	);
}
