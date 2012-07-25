
Content API
===========

Allows storage and retrieval of elements (as JSON), rendering using templates (using json-template)


Create element
--------------

POST /content/element/:id

Content: JSON


Create template
---------------

POST /content/template/:id

Content: json-template


Configure a default template for an element
-------------------------------------------

POST /content/view/:element/:template

Update:

PUT /content/view/:element/:template


Render an element
-----------------

GET /content/render/:element

GET /content/render/:element/:template

Content-Type is based on "Accept" header:

Accept: text/html => Content-Type: text/html
Accept: application/json => Content-Type: application/json
...

Possible exceptions:
404 Not found
400 Bad request
500 Internal server error


Element operations
------------------

PUT    /content/element/:id

DELETE /content/element/:id

GET    /content/element/:id


Template operations
-------------------

PUT    /content/template/:id

DELETE /content/template/:id

GET    /content/template/:id


