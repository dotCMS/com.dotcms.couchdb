package com.dotcms.couchdb.api;

import com.dotmarketing.beans.Host;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import java.util.List;


public interface CouchDbAPI {

    static CouchDbAPI api(Host host) {
        return new CouchDbAPIImpl(host);
    }

    boolean configured();

    void pushContentlet(Contentlet contentlet);
    
    void removeContentlet(Contentlet contentlet);


    default void shutdown() {
        CouchDB.shutdown();
    }

    default void reload() {
        CouchDB.reload();
    }


    void syncContentlet(Contentlet contentlet);

    boolean syncContentTypeListener(String contentType);
}
