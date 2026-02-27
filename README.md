# Content API

A content management REST API. Store JSON elements, apply templates, render output.

- REST API for CRUD on elements, templates, and views
- Template rendering engine inspired by [json-template](https://code.google.com/archive/p/json-template/)
- Key-value persistence on H2
- Built on [turismo](https://github.com/ghosthack/turismo) web framework
- UTF-8 throughout
- Apache License 2.0


## Quick start

Requires Java 21+ and [Gradle](https://gradle.org/install/).

```sh
gradle run
```

The API starts on `http://localhost:8080/content/`.

Custom port:

```sh
gradle run --args="--port 9090"
```


## Build and test

```sh
gradle build       # compile + test
gradle test        # tests only
gradle compileJava # compile only
```


## Concepts

Content API manages three resources that combine to produce rendered output:

- **Element** -- A JSON document stored by key. Holds the data (e.g. `{"title":"Hello","name":"World"}`).
- **Template** -- A text string stored by key. Contains `{{placeholders}}` that reference element fields (e.g. `{{title}}, {{name}}!`).
- **View** -- A mapping from an element key to a template key. Defines which template is the default for a given element.
- **Render** -- Merges an element's data into a template, replacing placeholders with values. Can use the view mapping (`/render/:id`) or an explicit pair (`/render/:element/:template`).
- **Stream** -- Multipart upload/download for elements, useful for importing data from files.

Typical workflow:

1. Create an element with your JSON data
2. Create a template with placeholders matching the element's fields
3. Create a view to link the element to the template
4. Render to produce the final output

Content can link to other content by including render URLs in element data:

```json
{"title": "Home", "next": "/content/render/about"}
```

```html
<h1>{{title}}</h1>
<a href="{{next}}">About</a>
```

There is no server-side includes or transclusion -- templates cannot embed the
rendered output of other content. Links between content are client-side only.

Lists of content can be built using repeated sections within an element:

```json
{"pages": [
  {"title": "Page 1", "url": "/content/render/page1"},
  {"title": "Page 2", "url": "/content/render/page2"}
]}
```

```html
<ul>
{{.repeated section pages}}
  <li><a href="{{url}}">{{title}}</a></li>
{{.end}}
</ul>
```


## API

All examples assume the server is running on `http://localhost:8080`.

### Element

```sh
# create
curl -X POST http://localhost:8080/content/element/demo \
  -d '{"title":"Hello","name":"World"}'
# 201 (created)

# read
curl http://localhost:8080/content/element/demo
# {"title":"Hello","name":"World"}

# update
curl -X PUT http://localhost:8080/content/element/demo \
  -d '{"title":"Hi","name":"World"}'
# {"title":"Hi","name":"World"}

# delete
curl -X DELETE http://localhost:8080/content/element/demo
# 200
```

### Template

```sh
# create
curl -X POST http://localhost:8080/content/template/greeting \
  -d '{{title}}, {{name}}!'
# 201 (created)

# read
curl http://localhost:8080/content/template/greeting
# {{title}}, {{name}}!

# update
curl -X PUT http://localhost:8080/content/template/greeting \
  -d '{{title}} {{name}}.'
# {{title}} {{name}}.

# delete
curl -X DELETE http://localhost:8080/content/template/greeting
# 200
```

### View (element-to-template mapping)

```sh
# create mapping
curl -X POST http://localhost:8080/content/view/demo/greeting
# 201 (created)

# read
curl http://localhost:8080/content/view/demo
# greeting

# update mapping
curl -X PUT http://localhost:8080/content/view/demo/greeting
# greeting

# delete
curl -X DELETE http://localhost:8080/content/view/demo
# 200
```

### Render

```sh
# render element using its default template (via view)
curl http://localhost:8080/content/render/demo
# Hello, World!

# render element with a specific template
curl http://localhost:8080/content/render/demo/greeting
# Hello, World!

# render as HTML
curl -H "Accept: text/html" http://localhost:8080/content/render/demo
# Hello, World!
```

### Stream (multipart)

```sh
# create element from file
curl -X POST http://localhost:8080/content/element/doc/stream \
  -F "doc=@data.json"

# read as binary
curl http://localhost:8080/content/element/doc/stream -o out.bin

# update from file
curl -X PUT http://localhost:8080/content/element/doc/stream \
  -F "doc=@data.json"
```

### Admin

```sh
# health check
curl http://localhost:8080/content/check
# OK

# initialize database schema
curl http://localhost:8080/content/init
# initialized
```


## Template syntax

The template engine uses `{{ }}` delimiters:

| Syntax | Description |
|---|---|
| `{{name}}` | Variable substitution |
| `{{name\|html}}` | Apply formatter (`html`, `raw`, `str`, `html-attr-value`) |
| `{{.section name}}...{{.end}}` | Conditional section |
| `{{.section name}}...{{.or}}...{{.end}}` | Conditional with else |
| `{{.repeated section items}}...{{.end}}` | Iterate over a list |
| `{{.repeated section items}}...{{.alternates with}}, {{.end}}` | Iterate with separator |
| `{{@}}` | Current cursor value |
| `{{user.name}}` | Dot notation for nested access |
| `{{# comment}}` | Comment (discarded) |


## License

[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
