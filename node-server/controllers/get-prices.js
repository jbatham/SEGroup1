const Prices = require('./models/price-data');
// TODO: Write utils function to take lat, long and distance and generate values for sql statement

exports.get_location_radius = function(req, res) {
  lat = req.params.lat;
  long = req.params.long;
  distance = req.params.distance;
}
