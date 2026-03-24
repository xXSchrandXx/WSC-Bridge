package de.xxschrandxx.wsc.wscbridge.velocity.api.command;

import java.util.UUID;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class SenderVelocity implements ISender<CommandSource> {

    protected final IBridgePlugin<?> instance;
    protected final CommandSource sender;

    public SenderVelocity(CommandSource sender, IBridgePlugin<?> instance) {
        this.sender = sender;
        this.instance = instance;
    }

    public CommandSource getParent() {
        return this.sender;
    }

    public boolean isPlayer() {
        return getParent() instanceof Player;
    }

    public UUID getUniqueId() {
        if (isPlayer()) {
            Player player = (Player) getParent();
            return player.getUniqueId();
        }
        else {
            return null;
        }
    }

    public String getName() {
        if (isPlayer()) {
            Player player = (Player) getParent();
            return player.getUsername();
        }
        else {
            return "Console";
        }
    }

    public void sendMessage(String message) {
        this.sendMessage(message, null, null);
    }

    public void sendMessage(String message, String hoverMessage) {
        this.sendMessage(message, hoverMessage, null);
    }

    public void sendMessage(String text, String hoverText, String copyText) {
        Component message = LegacyComponentSerializer.legacy('&').deserialize(text);
        if (hoverText != null && !hoverText.isBlank()) {
            Component hoverMessage = LegacyComponentSerializer.legacy('&').deserialize(hoverText);
            message = message.hoverEvent(HoverEvent.showText(hoverMessage));
        }
        if (copyText != null && !copyText.isBlank()) {
            message = message.clickEvent(ClickEvent.copyToClipboard(copyText));
        }
        this.sender.sendMessage(message);
    }

    public void send(String path) {
        this.sendMessage(instance.getConfiguration().getString(path));
    }

    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    public boolean checkPermission(String path) {
        return this.hasPermission(instance.getConfiguration().getString(path));
    }
}
