module.exports = function (req, res, next) {
    // TODO security
    console.log('Request Type:', req.method);
    next();
}