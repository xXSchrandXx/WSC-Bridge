package de.xxschrandxx.wsc.wscbridge.velocity.api;

import com.google.gson.internal.LinkedTreeMap;

import de.xxschrandxx.wsc.wscbridge.hytale.api.ConfigurationHytale;

public class ConfigurationVelocity extends ConfigurationHytale {
    public ConfigurationVelocity() {
        super();
    }
    public ConfigurationVelocity(LinkedTreeMap configuration) {
        super(configuration);
    }
}
