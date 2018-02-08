// GET /hello

var lastVisit = request.getSession().get("last-visit");
request.getSession().put("last-visit", new Date());

response.setStatus(200);
response.setContentType('text/plain');
response.setBody('Hello World ' + new Date()
    + ', you requested: ' + request.path
    + ' using ' + request.headers["user-agent"] + ".\n\n"
    + (lastVisit
        ? "Your last visit was on " + lastVisit
        : "This is your first visit with this session"));