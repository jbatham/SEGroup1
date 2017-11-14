// ** Functions to control the logic of the server interaction with db **
const prices = require('../models/prices.js');
const boundingBox = require('../utils/bounding-box.js')
// TODO: Write boundingBox function

exports.get_location_radius = function(req, res) {
	console.log("trying to sort stuff");
	lat = req.params.lat;
	long = req.params.long;
	distance = req.params.distance;
	boundingBox = boundingBox(req.params);
	prices.get_data(req.params,boundingBox,function(err, result) {
		if (err) res.send("ERROR IN FETCHING DATA FROM DB");
		res.send(result);
	});
}
