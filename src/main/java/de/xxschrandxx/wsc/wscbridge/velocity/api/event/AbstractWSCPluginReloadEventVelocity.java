package de.xxschrandxx.wsc.wscbridge.velocity.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public abstract class AbstractWSCPluginReloadEventVelocity {
    protected final ISender<?> sender;
    public AbstractWSCPluginReloadEventVelocity(ISender<?> sender) {
        this.sender = sender;
    }
    public ISender<?> getSender() {
        return this.sender;
    }
}
