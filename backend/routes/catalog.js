
  
const express = require('express');
const router = express.Router();
const catalogController = require('../controllers/catalog');
const extractFile = require('../middleware/file')('catalog');
const checkAuth = require('../middleware/check-auth');

router.get('', catalogController.getCatalog);

router.get('/:id', catalogController.getCatalogItem);

router.post('', checkAuth, extractFile, catalogController.createCatalog);

router.put('/:id', checkAuth, extractFile, catalogController.updateCatalog);

router.delete('/:id', checkAuth, catalogController.deleteCatalog);

module.exports = router;