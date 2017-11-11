const express = require('express')
const router = express.Router()

const price_controller = require('./controllers/get-prices.js')

// GET house price data 
router.get('/get_prices', price_controller.get_location_radius);
