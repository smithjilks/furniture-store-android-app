const Order = require('../models/order');
const mongoose = require('mongoose');

exports.getOrders = (req, res, next) => {
  const pageSize = +req.query.pagesize;
  const currentPage = +req.query.page;

  // const orderQuery = Order.find()

  const orderQuery = Order
    .aggregate()

    .lookup({
      from: 'users',
      localField: 'creator',
      foreignField: '_id',
      as: 'userDetails'
    });

  let fetchedOrders;

  if (pageSize && currentPage) {
    orderQuery
      .sort({ createdAt: 1 })

      .skip(pageSize * (currentPage - 1))

      .limit(pageSize);
  }

  orderQuery
    .then(documents => {
      fetchedOrders = documents;

      fetchedOrders.map(fetchedOrder => {
        fetchedUserDetails = fetchedOrder.userDetails[0] 
        
        fetchedOrder.userDetails = {
          "_id": fetchedUserDetails._id,
          "firstName": fetchedUserDetails.firstName,
          "lastName": fetchedUserDetails.lastName,
          "phone": fetchedUserDetails.phone,
          "email": fetchedUserDetails.email,
          "userType": fetchedUserDetails.userType,
          "token": "",
          "createdAt": "2022-04-11T12:19:31.428Z",
          "updatedAt": "2022-04-11T12:19:31.428Z"
      }
        return fetchedOrder
      });


      return Order.countDocuments();
    })

    .then(count => {
      res.status(200).json(
        fetchedOrders
      );
    })

    .catch(error => {
      console.log(error)
      res.status(500).json({
        message: 'Fetching order failed!',
        status: false
      });
    });
};

exports.getOrder = (req, res, next) => {
  Order
    .findById(req.params.id)
    .then(order => {
      if (order) {
        res.status(200).json(order);
      } else {
        res.status(404).json({
          message: 'Order does not exist',
          status: false
        });
      }
    })

    .catch(error => {
      res.status(500).json({
        message: 'Fetching order failed!',
        status: false
      });
    });
};

exports.getUserOrders = (req, res, next) => {
  const pageSize = +req.query.pagesize;
  const currentPage = +req.query.page;
  const orderQuery = Order.find({ creator: req.params.id });

  let fetchedOrders;

  if (pageSize && currentPage) {
    orderQuery
      .sort({ createdAt: 1 })

      .skip(pageSize * (currentPage - 1))

      .limit(pageSize);
  }

  orderQuery
    .then(documents => {
      fetchedOrders = documents;
      return fetchedOrders.length;
    })

    .then(count => {
      res.status(200).json(
        fetchedOrders
      );
    })

    .catch(error => {
      res.status(500).json({
        message: 'Fetching order failed!',
        status: false
      });
    });
};

exports.createOrder = (req, res) => {
  const order = new Order({
    creator: req.userData.userId,
    totalCost: req.body.totalCost,
    items: req.body.items,
    deliveryLat: req.body.deliveryLat,
    deliveryLong: req.body.deliveryLong
  });

  order
    .save()

    .then(createdOrder => {
      res.status(201).json({
        message: 'order added successfully',
        status: false
      });
    })

    .catch(error => {
      console.log(error)
      res.status(500).json({
        message: 'creating a order failed!',
        error: error
      });
    });
};

exports.updateOrder = (req, res, next) => {
  const updateData = req.body;
  updateData._id = req.params.id;

  Order
    .updateOne(
      {
        _id: req.params.id
      },
      {
        $set: updateData
      })

    .then(result => {
      console.log(result);
      if (result.modifiedCount > 0) {
        res.status(200).json({ 
          message: 'Update successful',
          status: false
        });
      } else {
        res.status(401).json({ 
          message: 'Not Authorized',
          status: false
        });
      }
    })

    .catch(error => {
      res.status(500).json({
        message: "Couldn't update order!",
        error: error
      });
    });
};

exports.deleteOrder = (req, res, next) => {
  Order

    .deleteOne({
      _id: req.params.id,
      creator: req.userData.userId
    })

    .then(result => {
      if (result.deletedCount >= 1) {
        res.status(200).json({ message: 'Deletion successful' });
      } else {
        res.status(401).json({ message: 'Not Authorized' });
      }
    })

    .catch(error => {
      res.status(500).json({
        message: 'Deleting order failed!',
        error: error
      });
    });
};