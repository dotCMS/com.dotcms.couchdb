# dotCMS + CouchDB 

Connect dotCMS with [CouchDB](https://couchdb.apache.org/) to allow for content to be pushed and deleted as content is published.  This plugin uses the lightcouch Couchdb java client.  More information about that driver can be found here: https://github.com/lightcouch/LightCouch

---


## App Config

The Couchdb app config :

The lightcouch `couchdb.properties` are all supported.  These include the required values:
```
### Required 
couchdb.name=db-test
couchdb.createdb.if-not-exist=true 
# The protocol: http | https
couchdb.protocol=http 
couchdb.host=127.0.0.1 
# The port e.g: 5984 | 6984
couchdb.port=5984 
# Blank username/password for no login 
couchdb.username=
couchdb.password= 
```
And the Optional/Advanced properties

```
### Optional/Advanced 
# Timeout to wait for a response in ms. Defaults to 0 (no timeout). 
couchdb.http.socket.timeout=
# Timeout to establish a connection in ms. Defaults to 0 (no timeout).
couchdb.http.connection.timeout=
# Max connections. 
couchdb.max.connections=100
# Connect through proxy
couchdb.proxy.host=
couchdb.proxy.port=
# path to append to DB URI
couchdb.path=
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
