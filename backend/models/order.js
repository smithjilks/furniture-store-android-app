const mongoose = require('mongoose');
const uniqueValidator = require('mongoose-unique-validator');

const orderSchema = mongoose.Schema({
  totalCost: { type: Number, required: true },
  items: { type: [], required: true },
  creator: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true },
  deliveryLat: { type: Number, required: true },
  deliveryLong: { type: Number, required: true },
  orderStatus: { type: String, required: true, default: 'placed' },
},
{
  timestamps: true
});

orderSchema.plugin(uniqueValidator);

module.exports = mongoose.model('Order', orderSchema);