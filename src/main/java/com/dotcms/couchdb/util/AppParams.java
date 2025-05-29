package com.dotcms.couchdb.util;

public enum AppParams {


    COUCH_DB_NAME("name"),
    COUCH_DB_CREATE_DB_IF_NOT_EXISTS("createDbIfNotExists"),
    COUCH_DB_PROTOCOL("protocol"),
    COUCH_DB_HOST("host"),
    COUCH_DB_PORT("port"),
    COUCH_DB_USERNAME("username"),
    COUCH_DB_PASSWORD("password"),
    COUCH_DB_HTTP_SOCKET_TIMEOUT("couchdb.http.socket.timeout"),
    COUCH_DB_HTTP_CONNECTION_TIMEOUT("couchdb.http.connection.timeout"),
    COUCH_DB_MAX_CONNECTIONS("couchdb.max.connections"),
    COUCH_DB_PROXY_HOST("couchdb.proxy.host"),
    COUCH_DB_PROXY_PORT("couchdb.proxy.port"),
    COUCH_DB_PATH("couchdb.path"),
    COUCHDB_LISTENER_ENABLED("listenerEnabled");

    public final String appValue;


    AppParams(String appValue) {
        this.appValue = appValue;

    }

    public String getAppValue() {
        return appValue;
    }
}
