package de.xxschrandxx.wsc.wscbridge.velocity.api;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.BridgeCoreAPI;
import de.xxschrandxx.wsc.wscbridge.core.api.MinecraftBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import de.xxschrandxx.wsc.wscbridge.velocity.api.command.SenderVelocity;

public class MinecraftBridgeVelocityAPI extends BridgeCoreAPI {

    public final ProxyServer server;

    public MinecraftBridgeVelocityAPI(String auth, MinecraftBridgeLogger logger, Boolean debug, ProxyServer server) {
        super(auth, logger, debug);
        this.server = server;
    }

    public MinecraftBridgeVelocityAPI(String user, String password, MinecraftBridgeLogger logger, Boolean debug, ProxyServer server) {
        super(user, password, logger, debug);
        this.server = server;
    }

    public MinecraftBridgeVelocityAPI(MinecraftBridgeVelocityAPI api, MinecraftBridgeLogger logger) {
        super(api, logger);
        this.server = api.server;
    }

    @Override
    public ISender<?> getSender(UUID uuid, IBridgePlugin<?> instance) {
        try {
            Player player = this.server.getPlayer(uuid).get();
            if (player == null) {
                return null;
            }
            return new SenderVelocity(player, instance);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public ISender<?> getSender(String name, IBridgePlugin<?> instance) {
        try {
            Player player = this.server.getPlayer(name).get();
            if (player == null) {
                return null;
            }
            return new SenderVelocity(player, instance);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public ArrayList<ISender<?>> getOnlineSender(IBridgePlugin<?> instance) {
        ArrayList<ISender<?>> sender = new ArrayList<ISender<?>>();
        for (Player player : this.server.getAllPlayers()) {
            sender.add(new SenderVelocity(player, instance));
        }
        return sender;
    }
}
