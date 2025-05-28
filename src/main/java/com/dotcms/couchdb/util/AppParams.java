package com.dotcms.couchdb.util;

public enum AppParams {


    COUCH_DB_NAME("name", "couchdb.name"),
    COUCH_DB_CREATE_DB_IF_NOT_EXISTS("createDbIfNotExists", "couchdb.createdb.if-not-exist"),
    COUCH_DB_PROTOCOL("protocol", "couchdb.protocol"),
    COUCH_DB_HOST("host", "couchdb.host"),
    COUCH_DB_PORT("port", "couchdb.port"),
    COUCH_DB_USERNAME("username", "couchdb.username"),
    COUCH_DB_PASSWORD("password", "couchdb.password"),
    COUCH_DB_HTTP_SOCKET_TIMEOUT("couchdb.http.socket.timeout", "couchdb.http.socket.timeout"),
    COUCH_DB_HTTP_CONNECTION_TIMEOUT("couchdb.http.connection.timeout", "couchdb.http.connection.timeout"),
    COUCH_DB_MAX_CONNECTIONS("couchdb.max.connections", "couchdb.max.connections"),
    COUCH_DB_PROXY_HOST("couchdb.proxy.host", "couchdb.proxy.host"),
    COUCH_DB_PROXY_PORT("couchdb.proxy.port", "couchdb.proxy.port"),
    COUCH_DB_PATH("couchdb.path", "couchdb.path");

    public final String appValue;
    public final String configValue;

    AppParams(String appValue, String configValue) {
        this.appValue = appValue;
        this.configValue = configValue;
    }

    public String getAppValue() {
        return appValue;
    }
}
