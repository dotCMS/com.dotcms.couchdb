package com.dotcms.couchdb.workflow;


import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.workflows.actionlet.WorkFlowActionlet;
import com.dotmarketing.portlets.workflows.model.MultiKeyValue;
import com.dotmarketing.portlets.workflows.model.MultiSelectionWorkflowActionletParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowActionClassParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowActionFailureException;
import com.dotmarketing.portlets.workflows.model.WorkflowActionletParameter;
import com.dotmarketing.portlets.workflows.model.WorkflowProcessor;
import com.dotmarketing.util.Logger;
import io.vavr.control.Try;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CouchDBActionlet extends WorkFlowActionlet {

    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE_LIST = "contentTypeList";
    private static final String PUBLISH_UNPUBLISH = "publishUnpublish";

    @Override
    public List<WorkflowActionletParameter> getParameters() {
        WorkflowActionletParameter publishUnpublish = new MultiSelectionWorkflowActionletParameter(
            PUBLISH_UNPUBLISH,
                "Sync on Publish or Unpublish or Both", "BOTH", true,
                () -> List.of(
                        new MultiKeyValue("PUBLISH", "PUBLISH"),
                        new MultiKeyValue("UNPUBLISH", "UNPUBLISH"),
                        new MultiKeyValue("BOTH", "BOTH")
                        )
        );


        final List<WorkflowActionletParameter> params = new ArrayList<>();

        params.add(new WorkflowActionletParameter(CONTENT_TYPE_LIST, "Comma Separated list of Content Types to sync with CouchDB.  Leave blank to sync all content types.", "", false));
        params.add(publishUnpublish);

        return params;
    }

    @Override
    public String getName() {
        return "Publish to CouchDB";
    }

    @Override
    public String getHowTo() {
        return "This actionlet will sync contentlets to the CouchDB database.  It will sync all contentlets of the content types specified in the Content Type List parameter.  It will also sync the contentlets on Publish or Unpublish or Both.";
    }

    @Override
    public void executeAction(WorkflowProcessor processor, Map<String, WorkflowActionClassParameter> params)
                    throws WorkflowActionFailureException {

        final Contentlet contentlet = processor.getContentlet();

        //final boolean isPurgeContentlet = Try.of(()-> Boolean.parseBoolean(params.get(PURGE_CONTENTLET_PARAM).getValue().trim())).getOrElse(true);
        final Host host = Try.of(() -> APILocator.getHostAPI()
                .find(contentlet.getHost(), APILocator.systemUser(), false))
                .getOrNull();
        if (host == null) {
            Logger.warn(this.getClass().getName(),"Contentlet Host is Null");
            return;
        }


        



    }


}
