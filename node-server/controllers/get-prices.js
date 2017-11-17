// ** Functions to control the logic of the server interaction with db **
const prices = require('../models/prices.js');
const bounding_box = require('../utils/bounding-box.js').bounding_box;
// TODO: Write boundingBox function

exports.get_location_radius = function(req, res) {
	console.log("trying to sort stuff");
	// console.log(req.query);
	boundingBox = bounding_box(req.query);
	console.log(boundingBox)
	prices.get_data(req.query,boundingBox,function(err, result) {
		if (err) res.send("ERROR IN FETCHING DATA FROM DB: " + err);
		res.send(result);
	});
}
