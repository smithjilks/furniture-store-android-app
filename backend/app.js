const express = require('express');
const path = require('path');

const mongoose = require('mongoose');

const userRoutes = require('./routes/user');
const orderRoutes = require('./routes/order');
const catalogRoutes = require('./routes/catalog');
const app = express();

mongoose.connect('mongodb+srv://' + process.env.MONGO_ATLAS_USER + ':' + process.env.MONGO_ATLAS_PW + '@cluster0.fvoeq.mongodb.net/myFirstDatabase?retryWrites=true&w=majority', { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => {
    console.log('Connected to Database');
  })
  .catch(() => {
    console.log('Connection failed');
  });

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Grant access to the images folder
app.use('/images', express.static(path.join(__dirname, 'images')));

app.use('/api/v1/users', userRoutes);
app.use('/api/v1/orders', orderRoutes);
app.use('/api/v1/catalog', catalogRoutes);

module.exports = app;