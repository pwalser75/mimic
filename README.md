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
- a **reqest method** (GET, HEAD, POST, PUT, DELETE, OPTIONS or TRACE)
- a **request path** (can contain placeholders for path parameters)
- **JavaScript (ECMASCRIPT 5.1) code** which evaluates requests and returns responses.

Mappings are configurable at runtime. Rules are evaluated using the *Nashorn JavaScript Engine*, 
and have access to the *request* and *response* on evaluation.

Planned extensions:
* Provide (potentially pre-configured) environments which allow to set/get (environment scoped) values (thus providing state over requests). 
Scripts can access environments by name (or lazy-create them).
* Allow resources to be pre-configured on environments as well. Resources have an id, content type and blob content, 
and can therefore represent arbitrary web resources. Scripts can get and set resources.

## Build / Run

**Build** using *Gradle* (default targets: `clean,build`):
```
gradle
```
**Run** (Spring Boot, `HTTPS` only, base URL: `https://localhost`)
```
gradle bootRun
```
Once the server is started, you can start doing HTTP requests (if the path or method is not mapped, you will at least get a 404 with a meaningful message).