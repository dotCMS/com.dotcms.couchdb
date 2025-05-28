# dotCMS + CouchDB 

Connect dotCMS with [CouchDB](https://couchdb.apache.org/) to allow for content to be pushed and deleted as content is published.  This plugin adds an App which can be configured to connect to a couchdb instance and publish content to it.  It uses the lightcouch java client - more information about that driver can be found here: https://github.com/lightcouch/LightCouch

---


## App Config

Navigate to the CouchDB app and fill in the appropiate values. 

These include the required values:
  - name: the db name to store the content
  - createDbIfNotExists: true|false
  - protocol: http|https
  - host, e.g.  "127.0.0.1"
  - port: e.g. 5984
  - username: CouchDB username
  - password: CouchDB user password
    

And the Optional/Advanced properties which can be entered as key/value pairs in the app.

```
couchdb.http.socket.timeout: in millis, defaults to 0 (no timeout). 
couchdb.http.connection.timeout: in millis, defaults to 0 (no timeout).
couchdb.max.connections: defaults to 100
couchdb.proxy.host: defaults to none
couchdb.proxy.port: defaults to none
couchdb.path: path to append to DB URI, not generally needed
```

## Workflow Actionlet

Users will be able to add an actionlet to their workflow to Publish or Unpublish content to the configured couchdb instance.


## Building

Build using maven
```
./mvnw clean install
```


To skip tests, run

```
./mvnw clean install -DskipTests
```


**Note:**
The plugin works on 25.05+.
