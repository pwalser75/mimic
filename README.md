# Mimic Web Server

Configurable web server for ad-hoc testing and mocking.

**HINT**: *Currently under development - check github commit messages for current status.*

## What is it

Mimic is a **generic web server** intended for
- **Rapid Prototyping**
- **Mocking web servers and test data**

It runs as a *Spring Boot* application and contains a generic *root dispatcher servlet* 
which delegates arbitrary requests to **configurable mappings**.

The mappings consist of:
- a **request method** (GET, HEAD, POST, PUT, DELETE, OPTIONS, TRACE, PATCH, COPY, LINK, UNLINK, PURGE, LOCK, UNLOCK, DEBUG, or VIEW)
- a **request path** (can contain placeholders for path parameters)
- **JavaScript (ECMASCRIPT 5.1) script** which evaluates requests and returns responses.

Mappings are configurable at runtime. Rules are evaluated using the *Nashorn JavaScript Engine*, 
and have access to the *request* and *response* on evaluation.

Planned extensions:
* Provide (potentially preconfigured) environments which allow to set/get (environment scoped) values (thus providing state over requests). 
Scripts can access environments by name (or lazy-create them).
* Allow resources to be preconfigured on environments as well. Resources have an id, content type and blob content, 
and can therefore represent arbitrary web resources. Scripts can get and set resources.

## Configuring request mappings

HTTP requests are dispatched to request mappings. Right now, these mappings are loaded from the **script directory**
configured in the `application.yml`:
```
mimic:
  scripts:
    dir: ./scripts
```

Creating scripts is simple: the first line is a comment that defines the path mapping as
 
    // {METHOD} {PATH-TEMPLATE}

The lines that follow are Javascript (ECMAScript 5.1) script that has access to the `request` and `response` context variables.
Responses are configured by setting the `status`, `contentType` and `body` of the request.
The request exposes all the information about the request (path, headers, path/form/query parameters, ...).

Example:

    // GET /hello
    response.setStatus(200);
    response.setContentType('text/plain');
    response.setBody('Hello World ' + new Date()
        + ', you requested: ' + request.path
        + ' using ' + request.headers["user-agent"]);

When requesting https://localhost/hello using this mapping, the Mimic server will dispatch the request to this mapping.

Some example scripts are already included in the script directory. Script files can be added/removed/modified anytime, the server will detect these changes and automatically reload the scripts.

- When no matching mapping was found, the Mimic server will respond with a **404** error.
- On script errors, the server will responed with a **500** error, which includes details on the error.

_Planned for later: store scripts in a DB and provide REST services to upload and manage them at runtime._

## HTTPS (TLS) configuration

The Mimic Server is configured to **redirect HTTP to HTTPS** (_seriously: forget about ever using HTTP without TLS again - no excuses accepted_).

It ships with a **server certificate** for local development (bound to `localhost/127.0.0.1`), which you can find
in `src/main/resources` (`server-keystore.jks` + configuration in `application.yml`). This certificate is signed by a test CA
which you can add as trusted CA within your user agent (browser or client app) for testing
purpose. You find the CA cert in `src/main/resources` as well (`test-ca-001.cer`).

## Build / Run

**Build** using *Gradle* (default targets: `clean,build`):

    gradle

**Run** (Spring Boot, `HTTPS` only, base URL: `https://localhost`)

    gradle bootRun

Once the server is started, you can start doing **HTTPS** requests (if the path or method is not mapped, you will at least get a 404 with a meaningful message).
