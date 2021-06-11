var express = require('express');
var router = express.Router();

var provider_middleware = require('../middleware/provider')

// MIDDLEWARE
router.use('/provider.*', provider_middleware)

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index');
});

router.get('/logout', function(req, res, next) {
    res.render('index');
});

// router.get('/:page/:id', function(req, res, next) {
//     res.render("m-" + req.params.page, req.params);
// });

router.get('/provider/signin\?', function(req, res, next) {
    res.render('signin_provider');
});

router.get('/provider/register\?', function(req, res, next) {
    res.render('register_provider');
});

router.get('/provider/homepage', function(req, res, next) {
    res.render('provider_homepage');
});

// Not found
router.get('*', function(req, res){
    // TODO not found page
    res.send('what???', 404);
});

module.exports = router;
