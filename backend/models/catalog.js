const mongoose = require('mongoose');
const uniqueValidator = require('mongoose-unique-validator');

const catalogSchema = mongoose.Schema({
  title: { type: String, required: true },
  shortDescription: { type: String, required: true },
  longDescription: { type: String, required: true },
  price: { type: Number, required: true },
  imageUrl: { type: String, required: true },
},
{
  timestamps: true
});

catalogSchema.plugin(uniqueValidator);

module.exports = mongoose.model('Catalog', catalogSchema);