// ** Defines the routes related to price data stuff **
const express = require('express')
const router = express.Router()

const price_controller = require('../controllers/get-prices.js')

// // Test route
// router.get('/', function(req,res) {
// 	res.send('Inside prices route!');
// });

// GET house price data
router.post('/get', price_controller.get_location_radius);

module.exports = router;

// TODO: verify this function routing is correct
