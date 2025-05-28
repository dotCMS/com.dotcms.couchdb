package com.dotcms.couchdb.viewtool;

import org.apache.velocity.tools.view.context.ViewContext;
import org.apache.velocity.tools.view.servlet.ServletToolInfo;

public class CouchDBToolInfo extends ServletToolInfo {

    @Override
    public String getKey () {
        return "couchdb";
    }

    @Override
    public String getScope () {
        return ViewContext.REQUEST;
    }

    @Override
    public String getClassname () {
        return CouchDBTool.class.getName();
    }

    @Override
    public Object getInstance ( Object initData ) {

    	CouchDBTool viewTool = new CouchDBTool();
        viewTool.init( initData );

        setScope( ViewContext.REQUEST );

        return viewTool;
    }

}