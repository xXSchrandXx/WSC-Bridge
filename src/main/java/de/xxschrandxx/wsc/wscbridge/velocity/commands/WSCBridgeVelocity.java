package de.xxschrandxx.wsc.wscbridge.velocity.commands;

import com.velocitypowered.api.command.RawCommand;

import de.xxschrandxx.wsc.wscbridge.core.commands.WSCBridge;
import de.xxschrandxx.wsc.wscbridge.velocity.MinecraftBridgeVelocity;
import de.xxschrandxx.wsc.wscbridge.velocity.api.command.SenderVelocity;

public class WSCBridgeVelocity implements RawCommand {
    @Override
    public void execute(final Invocation invocation) {
        MinecraftBridgeVelocity instance = MinecraftBridgeVelocity.getInstance();
        SenderVelocity sv = new SenderVelocity(invocation.source(), instance);
        new WSCBridge(instance).execute(sv, invocation.arguments().split(" "));
    }
}
