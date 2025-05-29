package com.dotcms.couchdb.api;

import java.io.IOException;
import java.util.Map;

import org.lightcouch.CouchDbClient;

import com.dotcms.workflow.helper.WorkflowHelper;
import com.dotmarketing.beans.Host;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.contentlet.transform.DotTransformerBuilder;
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
        try(final CouchDbClient client = CouchDB.instance(host).get()){

            ContentModel contentModel = new ContentModel(contentlet);

            Logger.info(this.getClass().getName(), "Pushing contentlet to CouchDB: " + contentModel.id);
            client.save(contentModel);
        } catch (IOException e) {
            Logger.error(this.getClass().getName(), "Error pushing contentlet to CouchDB", e);
        }

    }
    @Override
    public void removeContentlet(Contentlet contentlet) {
        if(CouchDB.instance(host).isEmpty()){
            Logger.error(this.getClass().getName(), "CouchDB is not configured for host: " + host.getIdentifier());
            return;
        }
        try(final CouchDbClient client = CouchDB.instance(host).get()){
            ContentModel contentModel = new ContentModel(contentlet);
            client.remove(contentModel.id);
        } catch (IOException e) {
            Logger.error(this.getClass().getName(), "Error removing contentlet from CouchDB", e);
        }
    }




}
