// GET /debug
response.setStatus(200);
response.setContentType('application/json');
response.setBody(JSON.stringify(request));