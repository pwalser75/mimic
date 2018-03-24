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

Mappings are configurable at runtime, and contain scripts that are evaluated using the *Nashorn JavaScript Engine*, 
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

## HTTPS (TLS) configuration

The Mimic Server is configured to **redirect HTTP to HTTPS**.

It ships with a **server certificate** for local development (bound to `localhost/127.0.0.1`), which you can find
in `src/main/resources` (`server-keystore.jks` + configuration in `application.yml`). This certificate is signed by a test CA
which you can add as trusted CA within your user agent (browser or client app) for testing
purpose. You find the CA cert in `src/main/resources` as well (`test-ca-001.cer`).

## Build / Run

**Build** using *Gradle* (default targets: `clean,build`):

    gradlew

**Run** (Spring Boot, base URL: `https://localhost`)

    gradlew start
	
or execute the JAR once you built it:

	cd mimic-web
	java -jar build/libs/mimic-web-1.0.0-SNAPSHOT.jar
	
By default, Mimic uses port 80 for HTTP (redirect to HTTPS) and port 443 for HTTPS (standard ports for both protocols).
If you want to **reconfigure the ports**, you can pass the specific ports using arguments:

	java -jar build/libs/mimic-1.0.0-SNAPSHOT.jar --server.port=8000 --http.server.port=7000

Once the server is started, you can start executing requests (if the path or method is not mapped, you will at least get a 404 with a meaningful message).

## Docker

Mimic includes a Docker build file (`Dockerfile`) as well as a Docker Compose configuration (`docker-compose.yml`) for running Mimic as a container.

**Preparation**: before building the image, you need to build Mimic (using `gradlew`)


**Run Mimic with Docker Compose** (optionally use the `-d` flag to run it in detached/background mode):

	docker-compose up
		
**Stop the docker container** (optionally use the `--rmi local` option to remove the local image afterwards):

	docker-compose down

## Sysops

Mimic uses **Spring Boot Actuator** to expose live information on the running server. Following endpoints are exposed over HTTPS, on **management port** `9000` on localhost:
- `/info`: get application information (name, description, version)
- `/health`: health information (system state, disk space, db connection)
- `/metrics`: runtime metrics information (CPU, memory, threads, sessions, ...)
- `/error`: errors
