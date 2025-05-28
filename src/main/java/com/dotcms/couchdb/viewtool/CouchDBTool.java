package com.dotcms.couchdb.viewtool;

import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.tools.view.context.ViewContext;
import org.apache.velocity.tools.view.tools.ViewTool;

import com.dotmarketing.beans.Host;
import com.dotmarketing.business.web.WebAPILocator;

public class CouchDBTool implements ViewTool {

    private HttpServletRequest request;
    private Host host;


	@Override
    public void init(Object initData) {
        this.request = ((ViewContext) initData).getRequest();
        this.host = WebAPILocator.getHostWebAPI().getCurrentHostNoThrow(this.request);
	}













}