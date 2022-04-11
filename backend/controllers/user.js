const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const User = require('../models/user');

exports.createUser = (req, res, next) => {
  const url = req.protocol + '://' + req.get('host');

  bcrypt
    .hash(req.body.password, 10)

    .then(hash => {
      const user = new User({
        firstName: req.body.firstName,
        lastName: req.body.lastName,
        phone: req.body.phone,
        email: req.body.email,
        password: hash
      });

      user
        .save()

        .then(result => {
          res.status(201).json({
            message: 'user created',
            status: true
          });
        })

        .catch(error => {
          res.status(400).json({
            message: 'Invalid user details',
            status: false
          });
        });
    })
    .catch(error => {
      console.log(error);
      res.status(400).json({
        message: 'Invalid user details',
        status: false
      });
    });
};

exports.loginUser = (req, res, next) => {
  let fetchedUser;

  User
    .findOne({
      email: req.body.email
    })

    .then(user => {
      if (!user) {
        return res.status(403).json({
          messsage: 'User does not exist',
          status: false
        });
      }

      fetchedUser = user;

      bcrypt
        .compare(req.body.password, user.password)

        .then(result => {
          if (!result) {
            return res.status(401).json({
              messsage: 'Auth failed',
              status: false
            });
          }

          const token = jwt.sign(
            {
              email: fetchedUser.email,
              userId: fetchedUser._id
            },
            process.env.JWT_KEY,
            {
              expiresIn: '1h'
            }
          );

          res.status(200).json({
            _id: fetchedUser._id,
            firstName: fetchedUser.firstName,
            lastName: fetchedUser.lastName,
            email: fetchedUser.email,
            phone: fetchedUser.phone,
            token: token,
            userType: fetchedUser.userType,
            createdAt: fetchedUser.createdAt,
            updatedAt: fetchedUser.updatedAt
          });
        });
    })

    .catch(error => {
      return res.status(401).json({
        messsage: 'Auth failed',
        status: false
      });
    });
};

exports.getUsers = (req, res, next) => {
  const pageSize = +req.query.pagesize;
  const currentPage = +req.query.page;
  const userQuery = User.find();
  let fetchedUsers;

  // User.find(userQuery, fields, { skip: 10, limit: 5 }, function(err, results) { ... });

  if (pageSize && currentPage) {
    userQuery
      .sort({ date: -1 })
      .skip(pageSize * (currentPage - 1))
      .limit(pageSize);
  }

  userQuery
    .then(documents => {
      fetchedUsers = documents;
      return User.countDocuments();
    })

    .then(count => {
      res.status(200).json({
        message: 'Succesfully sent from api',
        body: fetchedUsers,
        maxUsers: count
      });
    })

    .catch(error => {
      res.status(500).json({
        message: 'Fetching users failed!',
        status: false
      });
    });
};

exports.getUser = (req, res, next) => {
  User
    .findById(req.params.id)

    .then(user => {
      if (user) {
        res.status(200).json(user);
      } else {
        res.status(404).json({
          message: 'User does not exist',
          status: false,
        });
      }
    })
    .catch(error => {
      res.status(500).json({
        message: 'Fetching user failed!',
        status: false
      });
    });
};