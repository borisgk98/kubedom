var express = require('express');
var router = express.Router();

var provider_middleware = require('../middleware/provider')

// MIDDLEWARE
router.use('/provider.*', provider_middleware)

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('homepage');
});

// router.get('/:page/:id', function(req, res, next) {
//     res.render("m-" + req.params.page, req.params);
// });

router.get('/signin\?', function(req, res, next) {
    res.render('signin');
});

router.get('/register\?', function(req, res, next) {
    res.render('register');
});

router.get('/homepage', function(req, res, next) {
    res.render('homepage');
});

router.get('/create-node', function(req, res, next) {
    res.render('create-node');
});

router.get('/create-cluster', function(req, res, next) {
    res.render('create-cluster');
});

router.get('/cluster/:id', function(req, res, next) {
    res.render('cluster');
});


// Not found
router.get('*', function(req, res){
    // TODO not found page
    res.send('what???', 404);
});

module.exports = router;
