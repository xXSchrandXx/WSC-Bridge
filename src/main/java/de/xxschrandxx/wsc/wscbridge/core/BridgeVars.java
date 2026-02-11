package de.xxschrandxx.wsc.wscbridge.core;

import java.util.List;

import de.xxschrandxx.wsc.wscbridge.core.api.IBridgeLogger;
import de.xxschrandxx.wsc.wscbridge.core.api.configuration.AbstractConfiguration;
import de.xxschrandxx.wsc.wscbridge.core.api.configuration.IConfiguration;

public class BridgeVars extends AbstractConfiguration {
    public static boolean startConfig(IConfiguration<?> configuration, IBridgeLogger logger) {
        return startConfig(configuration, Configuration.class, defaults.class, logger);
    }

    public static class Configuration {
        // universal
        public static final String Debug = "debug";
        // user
        public static final String User = "user";
        // password
        public static final String Password = "password";

        // cmdalias
        // cmdalias.enabled
        public static final String cmdAliasEnabled = "cmdalias.enabled";
        // cmdalias.aliases
        public static final String cmdAliases = "cmdalias.aliases";

        // permission
        // permission.command.wscbridge
        public static final String PermCmdWSCBridge = "permission.command.wscbridge";

        // language
        // language.command.noperm
        public static final String LangCmdNoPerm = "language.command.nopermission";
        // language.command.reload.usage
        public static final String LangCmdReloadUsage = "language.command.reload.usage";
        // language.command.reload.config.start
        public static final String LangCmdReloadConfigStart = "language.command.reload.config.start";
        // language.command.reload.config.error
        public static final String LangCmdReloadConfigError = "language.command.reload.config.error";
        // language.command.reload.config.success
        public static final String LangCmdReloadConfigSuccess = "language.command.reload.config.success";
        // language.command.reload.api.start
        public static final String LangCmdReloadAPIStart = "language.command.reload.api.start";
        // language.command.reload.api.success
        public static final String LangCmdReloadAPISuccess = "language.command.reload.api.success";
        // language.command.info.info
        public static final String LangCmdInfoInfo = "language.command.info.info";
        // language.command.info.copy
        public static final String LangCmdInfoCopy = "language.command.info.copy";
    }
    // Default values
    public static final class defaults {
        // universal
        // debug
        public static final Boolean Debug = false;
        // user
        public static final String User = "user";
        // password
        public static final String Password = "MySuperSecretPassword";

        // cmdalias
        // cmdalias.enabled
        public static final Boolean cmdAliasEnabled = false;
        // cmdalias.aliases
        public static final List<String> cmdAliases = List.of("wscbridge", "wscb");

        // permission
        // permission.command.wscbridge
        public static final String PermCmdWSCBridge = "wscbridge.command.wscbridge";

        // language
        // language.command.noperm
        public static final String LangCmdNoPerm = "&8[&6WSC-Bridge&8]&c You don't have permission to do this.";
        // language.command.reload.usage
        public static final String LangCmdReloadUsage = "&8[&6WSC-Bridge&8]&7 Usage: &e/wscbridge [reload/info]";
        // language.command.reload.config.start
        public static final String LangCmdReloadConfigStart = "&8[&6WSC-Bridge&8]&7 Reloading configuration.";
        // language.command.reload.config.error
        public static final String LangCmdReloadConfigError = "&8[&6WSC-Bridge&8]&e Reloading configuration failed.";
        // language.command.reload.config.success
        public static final String LangCmdReloadConfigSuccess = "&8[&6WSC-Bridge&8]&7 Configuration reloaded successfully.";
        // language.command.reload.api.start
        public static final String LangCmdReloadAPIStart = "&8[&6WSC-Bridge&8]&7 Reloading API.";
        // language.command.reload.api.success
        public static final String LangCmdReloadAPISuccess = "&8[&6WSC-Bridge&8]&7 API reloaded successfully.";
        // language.command.info.info
        public static final String LangCmdInfoInfo = "Server: %server%\nServer-Version: %serverversion%\nPlugin-Version: %pluginversion%";
        // language.command.info.copy
        public static final String LangCmdInfoCopy = "Click to copy";
    }
}
