// GET /example

response.setStatus(200);
response.setContentType('application/xml');

response.setBody('<?xml version="1.0" encoding="UTF-8" standalone="yes"?>' +
    '<test>' +
    '<request path="' + request.path + '\"/>' +
    '<message from=\"Mimic\">Mimic greets you!</message>' +
    '</test>');