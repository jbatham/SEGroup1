// ** Model for db statements related to obtaining the prices data **
const db = require('../db.js');
// const convert_to_rads = require('../utils/convert_to_rads.js');

// Takes the boundingbox parameters from the controller, and selects the set of
// db data within the distance constraints, computed via great circle calculations
// REF: http://janmatuschek.de/LatitudeLongitudeBoundingCoordinates
exports.get_data = function(params, boundingBox, done) {
	db.get().query(
		'SELECT * FROM prices WHERE' +
		'(RADIANS(Lat) => RADIANS(?) AND RADIANS(Lat) <= RADIANS(?)) AND (RADIANS(Long) >= RADIANS(?) AND RADIANS(Long) <= RADIANS(?))' +
		'AND' +
		'ACOS(SIN(RADIANS(?)) * SIN(RADIANS(Lat)) + COS(RADIANS(?)) * COS(RADIANS(Lat)) * COS(RADIANS(Long) - (RADIANS(?)))) <= ?',
		boundingBox[0].lat, boundingBox[1].lat, boundingBox[0].long, boundingBox[1].long,
		params.lat, params.lat, params.long, distance/6371,	// distance/6371 is angular radius
		function(err, result) {
			if (err) return done(err);
			done(null, result);
		}
	);
}

// TODO: work out format of db data, potentially add FOR JSON AUTO, or FOR JSON and format manually
// verify sending data through callback correctly
