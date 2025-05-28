package com.dotcms.couchdb.api;

import java.util.Map;
import java.util.Optional;

import java.util.concurrent.ConcurrentHashMap;

import org.lightcouch.CouchDbClient;
import org.lightcouch.CouchDbProperties;

import com.dotcms.couchdb.util.AppParams;
import com.dotcms.security.apps.AppSecrets;
import com.dotcms.security.apps.Secret;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;

import io.vavr.control.Try;

class CouchDB {

    private static Map<String, CouchDbClient> clients = new ConcurrentHashMap<>();

    private static volatile boolean running = true;
    
    static CouchDbClient instance(Host host) {

        if(!running){
            throw new IllegalStateException("CouchDB is shutdown");
        }
        if(UtilMethods.isEmpty(()->host.getIdentifier())){
            Logger.warn(CouchDbClient.class,"There is no site sent or found");
            throw new IllegalArgumentException("There is no site sent or found");
        }
        final Optional<AppSecrets> appSecrets = Try.of(() -> APILocator.getAppsAPI().getSecrets("couchdb", false, host, APILocator.systemUser())).getOrElse(Optional.empty());
        if(appSecrets.isEmpty() && !host.isSystemHost()){
            return instance(APILocator.systemHost());
        }
        if(appSecrets.isEmpty()){
            Logger.warn(CouchDbClient.class,"There is no config set, please set it via Apps Tool");
            throw new DotRuntimeException("There is no CouchDB config set, please set it via Apps Tool");
        }
        return clients.computeIfAbsent(host.getIdentifier(), s -> {

            Map<String, Secret> secrets = appSecrets.get().getSecrets();
            CouchDbProperties properties = new CouchDbProperties();
            properties.setDbName(secrets.get(AppParams.COUCH_DB_NAME.getAppValue()).getString());
            properties.setProtocol(secrets.get(AppParams.COUCH_DB_PROTOCOL.getAppValue()).getString());
            properties.setHost(secrets.get(AppParams.COUCH_DB_HOST.getAppValue()).getString());
            properties.setPort(Integer.parseInt(secrets.get(AppParams.COUCH_DB_PORT.getAppValue()).getString()));
            properties.setUsername(secrets.get(AppParams.COUCH_DB_USERNAME.getAppValue()).getString());
            properties.setPassword(secrets.get(AppParams.COUCH_DB_PASSWORD.getAppValue()).getString());
            properties.setCreateDbIfNotExist(secrets.get(AppParams.COUCH_DB_CREATE_DB_IF_NOT_EXISTS.getAppValue()).getBoolean());
            properties.setMaxConnections(Integer.parseInt(secrets.get(AppParams.COUCH_DB_MAX_CONNECTIONS.getAppValue()).getString()));
            properties.setConnectionTimeout(Integer.parseInt(secrets.get(AppParams.COUCH_DB_HTTP_CONNECTION_TIMEOUT.getAppValue()).getString()));
            properties.setSocketTimeout(Integer.parseInt(secrets.get(AppParams.COUCH_DB_HTTP_SOCKET_TIMEOUT.getAppValue()).getString()));
            properties.setProxyHost(secrets.get(AppParams.COUCH_DB_PROXY_HOST.getAppValue()).getString());
            properties.setProxyPort(Integer.parseInt(secrets.get(AppParams.COUCH_DB_PROXY_PORT.getAppValue()).getString()));
            properties.setPath(secrets.get(AppParams.COUCH_DB_PATH.getAppValue()).getString());            


        return new CouchDbClient(properties);
    });



    }

    static CouchDbClient instance(String siteId) {
        Host host = new Host();
        host.setIdentifier(siteId);
        return instance(host);

    }

    static synchronized void shutdown() {
        running = false;
        clients.values().forEach(c->Try.run(()->c.shutdown()).onFailure(e -> Logger.warnAndDebug(CouchDB.class, "Issue shutting down CouchDB client:" + e.getMessage(), e)));

        clients.clear();
    }





    
}