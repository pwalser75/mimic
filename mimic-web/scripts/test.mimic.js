// GET /test

var result = {
    title: "Blade Runner",
    year: 1982,
    genres: [
        "Sci-Fi",
        "Thriller"
    ],
    ratings: {
        "IMDB": 8.2,
        "Metacritic": 89.0
    }
}

response.setStatus(200);
response.setContentType('application/json');
response.setBody(JSON.stringify(result));