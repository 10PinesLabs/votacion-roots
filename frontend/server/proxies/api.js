/*jshint node:true*/
var proxyPath = '/api';

//Generado usando el blueprint `ember g http-proxy /api http://127.0.0.1:9090/api
module.exports = function(app) {
  // For options, see:
  // https://github.com/nodejitsu/node-http-proxy
  var proxy = require('http-proxy').createProxyServer({});

  proxy.on('error', function(err, req) {
    console.error(err, req.url);
  });

  app.use(proxyPath, function(req, res, next){
    // include root path in proxied request
    req.url = proxyPath + '/' + req.url;
    proxy.web(req, res, { target: 'http://127.0.0.1:9090' });
  });
};
