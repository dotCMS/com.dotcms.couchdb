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
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.UtilMethods;

import io.vavr.control.Try;

class CouchDB {

    private static Map<String, Optional<CouchDbClient>> clients = new ConcurrentHashMap<>();

    private static volatile boolean running = true;
    
    static Optional<CouchDbClient> instance(Host host) {

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
            Logger.debug(CouchDbClient.class,"There is no config set, please set it via Apps Tool");
            return Optional.empty();
        }

        Map<String, Secret> secrets = appSecrets.get().getSecrets();
        String dbName = secrets.get(AppParams.COUCH_DB_NAME.getAppValue()).getString();
        if(UtilMethods.isEmpty(dbName)){
            Logger.warn(CouchDbClient.class,"There is no CouchDB name set, please set it via Apps Tool, using default value: couchdb");
            return Optional.empty();
        }



        return clients.computeIfAbsent(host.getIdentifier(), s -> {

            CouchDbProperties properties = new CouchDbProperties();
            properties.setDbName(dbName);
            
            String protocol = UtilMethods.isEmpty(()->secrets.get(AppParams.COUCH_DB_PROTOCOL.getAppValue())) ? "http" : secrets.get(AppParams.COUCH_DB_PROTOCOL.getAppValue()).getString();
            properties.setProtocol(protocol);

            String couchHost = UtilMethods.isEmpty(()->secrets.get(AppParams.COUCH_DB_HOST.getAppValue()).getString()) ? "localhost" : secrets.get(AppParams.COUCH_DB_HOST.getAppValue()).getString();
            properties.setHost(couchHost);

            String port = UtilMethods.isEmpty(()->secrets.get(AppParams.COUCH_DB_PORT.getAppValue()).getString()) ? "5984" : secrets.get(AppParams.COUCH_DB_PORT.getAppValue()).getString();
            properties.setPort(Integer.parseInt(port));

            String username = UtilMethods.isEmpty(()->secrets.get(AppParams.COUCH_DB_USERNAME.getAppValue()).getString()) ? "admin" : secrets.get(AppParams.COUCH_DB_USERNAME.getAppValue()).getString();
            properties.setUsername(username);

            String password = UtilMethods.isEmpty(()->secrets.get(AppParams.COUCH_DB_PASSWORD.getAppValue()).getString()) ? "password" : secrets.get(AppParams.COUCH_DB_PASSWORD.getAppValue()).getString();
            properties.setPassword(password);

            boolean createDbIfNotExist = UtilMethods.isSet(()->AppParams.COUCH_DB_CREATE_DB_IF_NOT_EXISTS.getAppValue()) ? secrets.get(AppParams.COUCH_DB_CREATE_DB_IF_NOT_EXISTS.getAppValue()).getBoolean():true;
            properties.setCreateDbIfNotExist(createDbIfNotExist);

            if(UtilMethods.isSet(()->secrets.get(AppParams.COUCH_DB_MAX_CONNECTIONS.getAppValue()).getString())){
                properties.setMaxConnections(Integer.parseInt(secrets.get(AppParams.COUCH_DB_MAX_CONNECTIONS.getAppValue()).getString()));
            }

            if(UtilMethods.isSet(()->secrets.get(AppParams.COUCH_DB_HTTP_CONNECTION_TIMEOUT.getAppValue()).getString())){
                properties.setConnectionTimeout(Integer.parseInt(secrets.get(AppParams.COUCH_DB_HTTP_CONNECTION_TIMEOUT.getAppValue()).getString()));
            }

            if(UtilMethods.isSet(()->secrets.get(AppParams.COUCH_DB_HTTP_SOCKET_TIMEOUT.getAppValue()).getString())){
                properties.setSocketTimeout(Integer.parseInt(secrets.get(AppParams.COUCH_DB_HTTP_SOCKET_TIMEOUT.getAppValue()).getString()));
            }

            if(UtilMethods.isSet(()->secrets.get(AppParams.COUCH_DB_PROXY_HOST.getAppValue()).getString())){
                properties.setProxyHost(secrets.get(AppParams.COUCH_DB_PROXY_HOST.getAppValue()).getString());
            }

            if(UtilMethods.isSet(()->secrets.get(AppParams.COUCH_DB_PROXY_PORT.getAppValue()).getString())){
                properties.setProxyPort(Integer.parseInt(secrets.get(AppParams.COUCH_DB_PROXY_PORT.getAppValue()).getString()));
            }
            if(UtilMethods.isSet(()->secrets.get(AppParams.COUCH_DB_PATH.getAppValue()).getString())){
                properties.setPath(secrets.get(AppParams.COUCH_DB_PATH.getAppValue()).getString());
            }

        return Optional.ofNullable(new CouchDbClient(properties));
    });



    }




    static Optional<CouchDbClient> instance(String siteId) {
        Host host = new Host();
        host.setIdentifier(siteId);
        return instance(host);

    }

    static synchronized void shutdown() {
        running = false;
        reload();
    }

    static  void reload() {
        clients.values().forEach(c->Try.run(()->c.get().shutdown()).onFailure(e -> Logger.warnAndDebug(CouchDB.class, "Issue shutting down CouchDB client:" + e.getMessage(), e)));
        clients.clear();
    }



    
}