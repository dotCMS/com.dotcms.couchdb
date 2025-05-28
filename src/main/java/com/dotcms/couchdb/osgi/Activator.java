package com.dotcms.couchdb.osgi;

import com.dotcms.system.event.local.business.LocalSystemEventsAPI;
import com.dotmarketing.filters.InterceptorFilter;

import org.osgi.framework.BundleContext;

import com.dotcms.couchdb.pushpublish.receiver.event.CouchDBContentListener;
import com.dotcms.couchdb.rest.CouchDBInterceptor;
import com.dotcms.couchdb.util.AppUtil;
import com.dotcms.couchdb.workflow.CouchDBActionlet;
import com.dotcms.filters.interceptor.FilterWebInterceptorProvider;
import com.dotcms.filters.interceptor.WebInterceptor;
import com.dotcms.filters.interceptor.WebInterceptorDelegate;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.osgi.GenericBundleActivator;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;


public class Activator extends GenericBundleActivator {

    private final WebInterceptor[] webInterceptors = {new CouchDBInterceptor()};

    final WebInterceptorDelegate delegate =
                    FilterWebInterceptorProvider.getInstance(Config.CONTEXT).getDelegate(
                            InterceptorFilter.class);




    public void start(final org.osgi.framework.BundleContext context) throws Exception {

        Logger.info(Activator.class.getName(), "Starting CouchDB Plugin");

        this.initializeServices(context);


        //Adding APP yaml
        //Logger.info(Activator.class.getName(), "Copying dotCDN APP");
        new AppUtil().copyAppYml();

        //Register Actionlet
        this.registerActionlet(context, new CouchDBActionlet());

        //Register Receiver PP listener events.
        this.registerPPListenerEvents();

    }

    private void registerPPListenerEvents() {

        CouchDBContentListener couchDBContentListener = new CouchDBContentListener();
        final LocalSystemEventsAPI localSystemEventsAPI = APILocator.getLocalSystemEventsAPI();
        localSystemEventsAPI.subscribe(couchDBContentListener);
    }


    @Override
    public void stop(BundleContext context) throws Exception {

        // remove portlet, viewtool, actionlet
        this.unregisterServices(context);

        Logger.info(Activator.class.getName(), "Stopping Interceptor");
        for (WebInterceptor webIn : webInterceptors) {
            Logger.info(Activator.class.getName(), "Removing the " + webIn.getClass().getName());
            delegate.remove(webIn.getName(), true);
        }



        final FilterWebInterceptorProvider filterWebInterceptorProvider =
                        FilterWebInterceptorProvider.getInstance(Config.CONTEXT);

        filterWebInterceptorProvider.getDelegate(InterceptorFilter.class);

        Logger.info(Activator.class.getName(), "Removing CouchDB APP");
        new AppUtil().deleteYml();

        final LocalSystemEventsAPI localSystemEventsAPI = APILocator.getLocalSystemEventsAPI();
        localSystemEventsAPI
                .unsubscribe(CouchDBContentListener.class, CouchDBContentListener.class.getName() + "#notify");




        Logger.info(Activator.class.getName(), "Stopping CouchDB Plugin");
    }








}
