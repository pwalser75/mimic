// OPTIONS {requestedPath}
response.setStatus(777);
response.setContentType('application/json');
response.setBody(request.toJSON());