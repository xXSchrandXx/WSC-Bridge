package de.xxschrandxx.wsc.wscbridge.velocity.api.event;

import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public final class WSCBridgeConfigReloadEventVelocity extends AbstractWSCConfigReloadEventVelocity {
    public WSCBridgeConfigReloadEventVelocity(ISender<?> sender) {
        super(sender);
    }
}
