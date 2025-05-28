package com.dotcms.couchdb.api;

import com.dotmarketing.beans.Host;
import com.dotmarketing.portlets.contentlet.model.Contentlet;


public interface CouchDbAPI {

    static CouchDbAPI api(Host host) {
        return new CouchDbAPIImpl(host);
    }

    void pushContentlet(Contentlet contentlet);
    
    void removeContentlet(Contentlet contentlet);


    default void shutdown() {
        CouchDB.shutdown();
    }

   
}
