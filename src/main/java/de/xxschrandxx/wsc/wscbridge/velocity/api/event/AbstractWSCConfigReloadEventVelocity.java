package de.xxschrandxx.wsc.wscbridge.velocity.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public abstract class AbstractWSCConfigReloadEventVelocity {
    protected final ISender<?> sender;
    public AbstractWSCConfigReloadEventVelocity(ISender<?> sender) {
        this.sender = sender;
    }
    public ISender<?> getSender() {
        return this.sender;
    }
}
