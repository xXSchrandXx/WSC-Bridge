package de.xxschrandxx.wsc.wscbridge.core.api.command;

import java.util.UUID;

public interface ISender<T> {
    /**
     * Sends message via given message
     * @param message
     */
    public void sendMessage(String message);
    /**
     * Sends message via given message and hoverMessage
     * @param message
     * @param hoverMessage
     */
    public void sendMessage(String message, String hoverMessage);
    /**
     * Sends message via given message, hoverMessage and copyText
     * @param message
     * @param hoverMessage
     * @param copyText
     */
    public void sendMessage(String message, String hoverMessage, String copyText);
    /**
     * Sends message via message in config under given path
     * @param path path in config
     */
    public void send(String path);
    /**
     * Checks permission via given string
     * @param permission
     * @return Weather the permission is granted
     */
    public boolean hasPermission(String permission);
    /**
     * Checks permission via permission in config under given path
     * @param path path in config
     * @return Weather the permission is granted
     */
    public boolean checkPermission(String path);
    /**
     * @return parent object {@link T}
     */
    public T getParent();
    /**
     * @return Weather the sender is a player
     */
    public boolean isPlayer();
    /**
     * @return Senders uuid
     */
    public UUID getUniqueId();
    /**
     * @return Sendes name
     */
    public String getName();
}
