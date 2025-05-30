package com.dotcms.couchdb.listener;



import com.dotcms.ai.validator.AIAppValidator;
import com.dotcms.couchdb.api.CouchDbAPI;
import com.dotcms.couchdb.util.AppUtil;
import com.dotcms.security.apps.AppSecretSavedEvent;
import com.dotcms.system.event.local.model.EventSubscriber;
import com.dotcms.system.event.local.model.KeyFilterable;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.portlets.contentlet.business.HostAPI;
import com.dotmarketing.util.Logger;
import io.vavr.control.Try;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;




public final class CouchDBAppListener implements EventSubscriber<AppSecretSavedEvent>, KeyFilterable {

    private final HostAPI hostAPI;

    public CouchDBAppListener(final HostAPI hostAPI) {
        this.hostAPI = hostAPI;
    }

    public CouchDBAppListener() {
        this(APILocator.getHostAPI());
    }

    /**
     * Notifies the listener of an {@link AppSecretSavedEvent}.
     *
     * <p>
     * This method is called when an {@link AppSecretSavedEvent} occurs. It performs the following actions:
     * <ul>
     *   <li>Logs a debug message if the event is null or the event's host identifier is blank.</li>
     *   <li>Finds the host associated with the event's host identifier.</li>
     *   <li>Resets the AI models for the found host's hostname.</li>
     *   <li>Validates the AI configuration using the {@link AIAppValidator}.</li>
     * </ul>
     * </p>
     *
     * @param event the {@link AppSecretSavedEvent} that triggered the notification
     */
    @Override
    public void notify(final AppSecretSavedEvent event) {
        if (Objects.isNull(event)) {
            Logger.debug(this, "Missing event, aborting");
            return;
        }

        if (StringUtils.isBlank(event.getHostIdentifier())) {
            Logger.debug(this, "Missing event's host id, aborting");
            return;
        }

        final String hostId = event.getHostIdentifier();
        final Host host = Try.of(() -> hostAPI.find(hostId, APILocator.systemUser(), false)).getOrNull();

        CouchDbAPI.api(host).reload();
    }

    @Override
    public Comparable<String> getKey() {
        return AppUtil.COUCHDB_APP_KEY;
    }



}
