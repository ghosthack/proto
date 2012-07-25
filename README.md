Content API
===========

An abstract content manager, which features:

 - REST API for storage and retrieval of elements as JSON [1][2].
 - Renders elements using json-template [3].
 - Persistence is based on key-value data stores [4].
 - Encoding is UTF-8.
 - Apache License 2.0: http://www.apache.org/licenses/LICENSE-2.0


[1] JSON: http://www.json.org/

[2] Uses JSON.simple: http://code.google.com/p/json-simple/

[3] JSON Template; http://code.google.com/p/json-template/

[4] Sample data stores implemented as key-value tables in MySQL


Render an element
-----------------

Using an element's default template:

    GET /content/render/:element

Or using a specific template:

    GET /content/render/:element/:template

Content-Type is based on "Accept" header:

    Accept: text/html => Content-Type: text/html
    Accept: application/json => Content-Type: application/json
    ...

Possible exceptions:

    404 Not found
    400 Bad request
    500 Internal server error


Create an element
-----------------

    POST /content/element/:id
    Content: JSON


Create a template
-----------------

    POST /content/template/:id
    Content: json-template


Configure a default template for an element
-------------------------------------------

    POST /content/view/:element/:template

Update:

    PUT /content/view/:element/:template


Element operations
------------------

    PUT     /content/element/:id
    DELETE  /content/element/:id
    GET     /content/element/:id


Template operations
-------------------

    PUT     /content/template/:id
    DELETE  /content/template/:id
    GET     /content/template/:id


Roadmap
-------

 * JSONP
 * Auth & Auth
 * Caching
 * Last-Modified, Expires
 * GAE Datastore Implementation
 * Template validation
 * UTF-8 validation
 * Better JSON valitation handling
 * Schemas
 * 100% Coverage

