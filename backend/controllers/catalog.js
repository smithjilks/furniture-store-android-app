const Catalog = require('../models/catalog');

exports.getCatalog = (req, res, next) => {
  const pageSize = +req.query.pagesize;
  const currentPage = +req.query.page;
  const catalogQuery = Catalog.find();
  let fetchedCatalog;

  if (pageSize && currentPage) {
    catalogQuery
      .sort({ createdAt: 1 })

      .skip(pageSize * (currentPage - 1))

      .limit(pageSize);
  }

  catalogQuery
    .then(documents => {
      fetchedCatalog = documents;
      return Catalog.countDocuments();
    })

    .then(count => {
      res.status(200).json(fetchedCatalog);
    })

    .catch(error => {
      res.status(500).json({
        message: 'Fetching catalog failed!',
        status: false
      });
    });
};

exports.getCatalogItem = (req, res, next) => {
  Catalog
    .findById(req.params.id)

    .then(catalog => {
      if (catalog) {
        res.status(200).json(catalog);
      } else {
        res.status(404).json({
          message: 'Catalog does not exist',
          status: false
        });
      }
    })

    .catch(error => {
      res.status(500).json({
        message: 'Fetching catalog failed!',
        status: false
      });
    });
};

exports.createCatalog = (req, res) => {
  if (req.body === {}) {
    res.status(400).json({
      message: 'Bad request',
      status: false
    });
  }
  const url = req.protocol + '://' + req.get('host');
  const catalog = new Catalog({
    title: req.body.title,
    shortDescription: req.body.shortDescription,
    longDescription: req.body.longDescription,
    price: req.body.price,
    imageUrl: url + '/images/catalog/' + req.file.filename,

  });


  catalog
    .save()

    .then(createdCatalog => {
      res.status(201).json({
        message: 'catalog added successfully',
        status: true
      });
    })

    .catch(error => {
      res.status(500).json({
        message: 'creating a catalog failed!',
        status: false
      });
    });
};

exports.updateCatalog = (req, res, next) => {
  const updateData = req.body;
  updateData._id = req.params.id;

  if (req.file) {
    const url = req.protocol + '://' + req.get('host');
    updateData.imageUrl = url + '/images/catalog/' + req.file.filename;
  }

  Catalog
    .updateOne(
      {
        _id: req.params.id
      },
      {
        $set: updateData
      })

    .then(result => {
      if (result.modifiedCount > 0) {
        res.status(200).json({ message: 'Update successful' });
      } else {
        res.status(401).json({
          message: 'Not Authorized',
          status: false
        });
      }
    })

    .catch(error => {
      res.status(500).json({
        message: "Couldn't update catalog!",
        status: false
      });
    });
};

exports.deleteCatalog = (req, res, next) => {
  Catalog

    .deleteOne({
      _id: req.params.id,
      creator: req.userData.userId
    })

    .then(result => {
      if (result.deletedCount >= 1) {
        res.status(200).json({ message: 'Deletion successful' });
      } else {
        res.status(401).json({ 
          message: 'Not Authorized',
          status: false
        });
      }
    })

    .catch(error => {
      res.status(500).json({
        message: 'Deleting catalog failed!',
        status: false
      });
    });
};