package com.dotcms.couchdb.api;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.dotcms.workflow.helper.WorkflowHelper;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.contentlet.transform.DotTransformerBuilder;

public class ContentModel implements Serializable{
    final String id,_id;
    final String title;
    final String contentType;
    final String inode;
    final Date modDate;
    final String modUser;
    final Map<String, Object> contentMap;

    public ContentModel(String id, String title, String contentType, String inode, Date modDate, String modUser, Map<String, Object> contentMap) {
        this.id = id;
        this._id=id;
        this.title = title;
        this.contentType = contentType;
        this.inode = inode;
        this.modDate = modDate;
        this.modUser = modUser;
        this.contentMap = contentMap;
    }

    public ContentModel(Contentlet contentlet) {
        this.id = contentlet.getIdentifier() + "_" + contentlet.getLanguageId() + "_" + contentlet.getVariantId();
        this._id=this.id;
        this.title = contentlet.getTitle();
        this.contentType = contentlet.getContentType().variable();
        this.inode = contentlet.getInode();
        this.modDate = contentlet.getModDate();
        this.modUser = contentlet.getModUser();
        this.contentMap = contentletToMap(contentlet);
    }
    private Map<String, Object> contentletToMap(Contentlet contentlet) {
        final String variant = contentlet.getVariantId();
        contentlet = new DotTransformerBuilder().contentResourceOptions(false).content(contentlet).build().hydrate().get(0);
        contentlet.setVariantId(variant);
        
        return WorkflowHelper.getInstance().contentletToMap(contentlet);
    }


}
