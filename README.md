<img src="https://static.dotcms.com/assets/icons/apps/couch-db-logo.png" width="150">


# dotCMS + CouchDB 


Connect dotCMS with [CouchDB](https://couchdb.apache.org/) to allow for content to be pushed and deleted as content is published.  This plugin adds an App which can be configured to connect to a couchdb instance and publish content to it.  It uses the lightcouch java client - more information about that driver can be found here: https://github.com/lightcouch/LightCouch

## Use Case:
![image](https://github.com/user-attachments/assets/ca8f1c4f-a0fb-4ad8-9fa3-4a870611707e)



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
## Content Listner
Admins can specify which content they would like to sync with CouchDB in the App.  This is just a comma separated list of content types.


## Workflow Actionlet

The plugin also provides an actionlet to their workflow to Publish or Unpublish content to the configured couchdb instance.  If the user selects to "Sync" content, then content will be pushed into CouchDB if it is published and removed from CouchDB if there are no published versions.

## Running CouchDB Locally for testing

```
docker run -d --name my-couchdb -e COUCHDB_USER=admin -e COUCHDB_PASSWORD=password  -p 5984:5984 --rm couchdb:latest
```


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
This plugin is provided by dotCMS as an example only and is not warrentied or supported in any way.   

