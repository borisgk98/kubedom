fs = require("fs");

//build section
fs.copyFileSync("./node_modules/bootstrap/dist/css/bootstrap.css", "./public/stylesheets/bootstrap.css");
fs.copyFileSync("./node_modules/jquery/dist/jquery.min.js", "./public/js/jquery.min.js");
fs.copyFileSync("./node_modules/bootstrap/dist/js/bootstrap.js", "./public/js/bootstrap.js");
fs.copyFileSync("./node_modules/bootstrap-notify/bootstrap-notify.js", "./public/js/bootstrap-notify.js");

app = require("./app");
app.listen(8080);