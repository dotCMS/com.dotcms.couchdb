package com.dotcms.couchdb.pushpublish.receiver.event;



import com.dotcms.content.elasticsearch.business.event.ContentletArchiveEvent;
import com.dotcms.content.elasticsearch.business.event.ContentletDeletedEvent;
import com.dotcms.content.elasticsearch.business.event.ContentletPublishEvent;
import com.dotcms.system.event.local.model.Subscriber;

import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.contentlet.model.ContentletListener;
import com.dotmarketing.util.Logger;


/**
 * This subscriber is in charge of invalidated
 * @author jsanca
 */
public class CouchDBContentListener implements ContentletListener<Contentlet> {


    @Override
    public String getId() {
        return getClass().getCanonicalName();
    }

    @Override
    public void onModified(final ContentletPublishEvent<Contentlet> contentletPublishEvent) {
        final Contentlet contentlet = contentletPublishEvent.getContentlet();
        if (contentletPublishEvent.isPublish()) {
            Logger.info(this.getClass(), "onModified - PublishEvent:true " + contentlet.getTitle());
        } else {
            Logger.info(this.getClass(), "onModified - PublishEvent:false " + contentlet.getTitle());
        }
    }

    @Subscriber
    public void onPublish(final ContentletPublishEvent<Contentlet> contentletPublishEvent) {
        final Contentlet contentlet = contentletPublishEvent.getContentlet();
        if (contentletPublishEvent.isPublish()) {
            Logger.info(this.getClass(), "onPublish - PublishEvent:true " + contentlet.getTitle());
        } else {
            Logger.info(this.getClass(), "onPublish - PublishEvent:false " + contentlet.getTitle());
        }
    }

    @Subscriber
    @Override
    public void onArchive(final ContentletArchiveEvent<Contentlet> contentletArchiveEvent) {
        final Contentlet contentlet = contentletArchiveEvent.getContentlet();
        Logger.info(this.getClass(), "onArchive -  " + contentlet.getTitle());
    }

    @Subscriber
    @Override
    public void onDeleted(final ContentletDeletedEvent<Contentlet> contentletDeletedEvent) {
        final Contentlet contentlet = contentletDeletedEvent.getContentlet();
        Logger.info(this.getClass(), "onDeleted -  " + contentlet.getTitle());
    }

} 
