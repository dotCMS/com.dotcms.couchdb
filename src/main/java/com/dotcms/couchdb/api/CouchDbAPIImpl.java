package com.dotcms.couchdb.api;

import com.dotcms.couchdb.util.AppParams;
import com.dotcms.couchdb.util.AppUtil;
import com.dotcms.security.apps.AppSecrets;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.util.UtilMethods;
import com.google.gson.JsonObject;
import io.vavr.control.Try;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.lightcouch.CouchDbClient;

import com.dotmarketing.beans.Host;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.Logger;

public class CouchDbAPIImpl implements CouchDbAPI {


    private final Host host;
    /**
     * Method to load the app secrets into the variables.
     * @param host if is not sent will throw IllegalArgumentException, if sent will try to
     * find the secrets for it, if there is no secrets for the host will use the ones for the System_Host
     *
     */
    public CouchDbAPIImpl(final Host host) {

        this.host = host;
    }

    /**
     * Pushes content to couchdb
     * @param contentlet
     */
    @Override
    public void pushContentlet(Contentlet contentlet) {
        if(UtilMethods.isEmpty(()-> contentlet.getIdentifier())){
            return;
        }
        if(CouchDB.instance(host).isEmpty()){
            Logger.error(this.getClass().getName(), "CouchDB is not configured for host: " + host.getIdentifier());
            return;
        }
        CouchDbClient client = CouchDB.instance(host).get();


        ContentModel contentModel = new ContentModel(contentlet);

        Logger.info(this.getClass().getName(), "Pushing contentlet to CouchDB: " + contentModel.id);
        client.save(contentModel);
        Logger.info(this.getClass().getName(), "Pushed contentlet to CouchDB: " + contentModel.title);


    }

    /**
     * Removes the content from couchdb - if it exists there
     * @param contentlet
     */
    @Override
    public void removeContentlet(Contentlet contentlet) {
        if(UtilMethods.isEmpty(()-> contentlet.getIdentifier())){
            return;
        }
        if(CouchDB.instance(host).isEmpty()){
            Logger.error(this.getClass().getName(), "CouchDB is not configured for host: " + host.getIdentifier());
            return;
        }
        CouchDbClient client = CouchDB.instance(host).get();


        ContentModel contentModel = new ContentModel(contentlet);
        Optional<JsonObject> object = Try.of(()->client.find(JsonObject.class, contentModel._id)).toJavaOptional();

        if(object.isEmpty()){
            Logger.info(this.getClass(),"Could not find document for id:" + contentModel._id + ", title:" + contentModel.title);
            return;
        }
        client.remove(object.get());
        Logger.info(this.getClass().getName(), "Removed contentlet to CouchDB: " + contentModel.title);


    }


    /***
     * This will add the contentlet to couch if it is published in dotCMS and remove it if it is unpublished.
     * @param contentlet
     */
    @Override
    public void syncContentlet(Contentlet contentlet) {

        // publish if live
        if(Try.of(()->contentlet.isLive()).getOrElse(false)) {
            this.pushContentlet(contentlet);
            return;
        }

        // remove if not live
        if(!Try.of(()->contentlet.hasLiveVersion()).getOrElse(false)) {
            this.removeContentlet(contentlet);
        }



    }

    @Override
    public boolean configured() {
        return CouchDB.instance(host).isPresent();
    }

    @Override
    public boolean syncContentTypeListener(String type) {
        if(!configured()){
            return false;
        }


        final Optional<AppSecrets> appSecrets = Try.of(() -> APILocator.getAppsAPI().getSecrets(AppUtil.COUCHDB_APP_KEY, true, host, APILocator.systemUser())).getOrElse(Optional.empty());
        if(appSecrets.isEmpty()){
            return false;
        }

        String[] contentTypes = Try.of(()->appSecrets.get().getSecrets().get(AppParams.COUCHDB_LISTENER_ENABLED.getAppValue()).getString().toLowerCase().split(",")).getOrElse(new String[0]);

        return Arrays.stream(contentTypes).collect(Collectors.toList()).contains(type.toLowerCase());



    }

}
