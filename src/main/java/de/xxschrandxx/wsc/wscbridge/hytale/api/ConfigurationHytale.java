package de.xxschrandxx.wsc.wscbridge.hytale.api;

import java.util.List;

import com.google.gson.internal.LinkedTreeMap;

import de.xxschrandxx.wsc.wscbridge.core.api.configuration.IConfiguration;

public class ConfigurationHytale implements IConfiguration<LinkedTreeMap> {
    protected final LinkedTreeMap configuration;
    public ConfigurationHytale() {
        this.configuration = new LinkedTreeMap();
    }
    public ConfigurationHytale(LinkedTreeMap configuration) {
        this.configuration = configuration;
    }
    public LinkedTreeMap getConfiguration() {
        return this.configuration;
    }
    public void set(String path, Object value) {
        if (path == null) return;
        String[] parts = path.split("\\.");
        if (parts.length == 1) {
            if (value == null) {
                this.configuration.remove(path);
            } else {
                this.configuration.put(path, value);
            }
            return;
        }
        LinkedTreeMap current = this.configuration;
        for (int i = 0; i < parts.length - 1; i++) {
            Object obj = current.get(parts[i]);
            if (!(obj instanceof LinkedTreeMap)) {
                LinkedTreeMap child = new LinkedTreeMap();
                current.put(parts[i], child);
                current = child;
            } else {
                current = (LinkedTreeMap) obj;
            }
        }
        String last = parts[parts.length - 1];
        if (value == null) {
            current.remove(last);
        } else {
            current.put(last, value);
        }
    }

    public Object get(String path) {
        if (path == null) return null;
        String[] parts = path.split("\\.");
        LinkedTreeMap current = this.configuration;
        for (int i = 0; i < parts.length; i++) {
            Object obj = current.get(parts[i]);
            if (i == parts.length - 1) {
                return obj;
            }
            if (obj instanceof LinkedTreeMap) {
                current = (LinkedTreeMap) obj;
            } else {
                return null;
            }
        }
        return null;
    }

    public boolean getBoolean(String path) {
        Object val = this.get(path);
        return val instanceof Boolean && ((Boolean) val).booleanValue();
    }

    public String getString(String path) {
        Object val = this.get(path);
        return val == null ? null : String.valueOf(val);
    }

    public int getInt(String path) {
        Object val = this.get(path);
        return val == null ? 0 : ((Number) val).intValue();
    }

    public double getDouble(String path) {
        Object val = this.get(path);
        return val == null ? 0.0 : ((Number) val).doubleValue();
    }

    public float getFloat(String path) {
        Object val = this.get(path);
        return val == null ? 0f : ((Number) val).floatValue();
    }

    public long getLong(String path) {
        Object val = this.get(path);
        return val == null ? 0L : ((Number) val).longValue();
    }

    public List<String> getStringList(String path) {
        return (List<String>) this.get(path);
    }
}
