package com.dotcms.couchdb.api;

import com.google.gson.JsonObject;
import io.vavr.control.Try;

import java.util.Optional;
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

    @Override
    public void pushContentlet(Contentlet contentlet) {

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
    @Override
    public void removeContentlet(Contentlet contentlet) {
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




}
