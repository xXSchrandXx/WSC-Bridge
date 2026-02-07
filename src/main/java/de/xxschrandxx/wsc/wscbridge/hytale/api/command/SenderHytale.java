package de.xxschrandxx.wsc.wscbridge.hytale.api.command;

import java.awt.Color;
import java.util.UUID;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.permissions.PermissionsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;

import de.xxschrandxx.wsc.wscbridge.core.IBridgePlugin;
import de.xxschrandxx.wsc.wscbridge.core.api.command.ISender;

public class SenderHytale implements ISender<CommandSender> {

    protected final IBridgePlugin<?> instance;
    protected final CommandSender sender;

    public SenderHytale(CommandSender sender, IBridgePlugin<?> instance) {
        this.sender = sender;
        this.instance = instance;
    }

    public CommandSender getParent() {
        return this.sender;
    }

    public boolean isPlayer() {
        return getParent() instanceof Player || getParent() instanceof PlayerRef;
    }

    public UUID getUniqueId() {
        CommandSender sender = getParent();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            return player.getUuid();
        } else if (sender instanceof PlayerRef) {
            PlayerRef player = (PlayerRef) sender;
            return player.getUuid();
        }
        return null;
    }

    public String getName() {
        return this.sender.getDisplayName();
    }

    public void sendMessage(String message) {
        this.sendMessage(message, null, null);
    }

    public void sendMessage(String message, String hoverMessage) {
        this.sendMessage(message, hoverMessage, null);
    }

    public void sendMessage(String text, String hoverMessage, String copyText) {
        Message message = Message.empty();
        String[] parts = text.split("&");
        Message lastPart = null;
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            Message messagePart = Message.empty();
            if (lastPart != null) {
                messagePart.color(lastPart.getColor());
//                messagePart.bold(lastPart.isBold());
//                messagePart.italic(lastPart.isItalic());
//                messagePart.monospace(lastPart.isMonospace());
            }
            if (part.isBlank()) {
                message.insert(part);
                continue;
            }
            char first = part.charAt(0);
            switch (first) {
                case '0':
                    messagePart.color(new Color(0));
                    break;
                case '1':
                    messagePart.color(new Color(170));
                    break;
                case '2':
                    messagePart.color(new Color(43520));
                    break;
                case '3':
                    messagePart.color(new Color(43690));
                    break;
                case '4':
                    messagePart.color(new Color(11141120));
                    break;
                case '5':
                    messagePart.color(new Color(11141290));
                    break;
                case '6':
                    messagePart.color(new Color(16755200));
                    break;
                case '7':
                    messagePart.color(new Color(11184810));
                    break;
                case '8':
                    messagePart.color(new Color(5592405));
                    break;
                case '9':
                    messagePart.color(new Color(5592575));
                    break;
                case 'a':
                    messagePart.color(new Color(5635925));
                    break;
                case 'b':
                    messagePart.color(new Color(5636095));
                    break;
                case 'c':
                    messagePart.color(new Color(16733525));
                    break;
                case 'd':
                    messagePart.color(new Color(16733695));
                    break;
                case 'e':
                    messagePart.color(new Color(16777045));
                    break;
                case 'f':
                    messagePart.color(new Color(16777215));
                    break;
                case 'k':
                    // unsupported
                    break;
                case 'l':
                    messagePart.bold(true);
                    break;
                case 'm':
                    // unsupported
                    break;
                case 'n':
                    // unsupported
                    break;
                case 'o':
                    messagePart.italic(true);
                    break;
                case 'r':
                    messagePart.color(Color.WHITE);
                    messagePart.bold(false);
                    messagePart.italic(false);
                    messagePart.monospace(false);
                    break;
            }
            messagePart.insert(part.substring(1));
            lastPart = messagePart;
            message.insert(messagePart);
        }

        this.sender.sendMessage(message);
    }

    public void send(String path) {
        this.sendMessage(instance.getConfiguration().getString(path));
    }

    public boolean hasPermission(String permission) {
        boolean holder = this.sender.hasPermission(permission);
        if (holder) {
            return true;
        }
        return PermissionsModule.get().hasPermission(getUniqueId(), permission);
    }

    public boolean checkPermission(String path) {
        return this.hasPermission(instance.getConfiguration().getString(path));
    }
}
