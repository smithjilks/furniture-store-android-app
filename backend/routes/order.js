
  
const express = require('express');
const router = express.Router();
const orderController = require('../controllers/order');
const checkAuth = require('../middleware/check-auth');

router.get('', orderController.getOrders);

router.get('/:id', orderController.getOrder);

router.get('/user/:id', orderController.getUserOrders);

router.post('', checkAuth, orderController.createOrder);

router.put('/:id', checkAuth, orderController.updateOrder);

router.delete('/:id', checkAuth, orderController.deleteOrder);

module.exports = router;