// GET /index

var html=
'<html>'+
'<head>'+
'<title>Example Servlet</title>'+
'</head>'+
'<style>'+
'@import url(\'https://fonts.googleapis.com/css?family=Lato:300,400,700,900\');'+
'* { font-family:Lato, Calibri, sans-serif; color: #444455; }'+
'body { padding: 20px; }'+
'h1, h2, h3 { font-weight: 900; }'+
'h1 { margin-top:0; }'+
'h4 { margin-bottom:0; }'+
'b { font-weight: 900; }'+
'</style>'+
'<body>'+
'<h4>MIMIC</h4>'+
'<h1>Welcome</h1>'+
'This is an example <b>HTML</b> page created by <b>Mimic</b> on '+
'<code>'+new Date()+'</code>'+
'<p><img src="http://frostnova.ch/temp/kitchen.jpg">';

var lastVisit = request.getSession().get("last-visit");
request.getSession().put("last-visit", new Date());

response.setStatus(200);
response.setContentType('text/html');
response.setBody(html);