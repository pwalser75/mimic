// GET /hello

response.setStatus(200);
response.setContentType('text/plain');
response.setBody('Hello World ' + new Date()
    + ', you requested: ' + request.path
    + ' using ' + request.headers["user-agent"]);