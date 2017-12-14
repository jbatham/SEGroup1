// ** Functions to control the logic of the server interaction with db **
const prices = require('../models/prices');
const bounding_box = require('../utils/bounding-box').bounding_box;

exports.get_location_radius = function(req, res) {
	console.log("> Getting house price data..");
	//console.log(req.query);
	boundingBox = bounding_box(req.query);
	prices.get_data(req.query,boundingBox,function(err, result) {
		if (err) {
			res.send("ERROR IN FETCHING DATA FROM DB: " + err);
		}
		else {
			var resultString = JSON.stringify(result);
			// console.log('Response sent: ', resultString);
			json = JSON.parse(resultString);
			res.json(json);
		}
	});
}
