package de.xxschrandxx.wsc.wscbridge.velocity.api.event;

import java.util.ArrayList;

public class WSCBridgeModuleEventVelocity {
    protected ArrayList<String> modules = new ArrayList<String>();

    public ArrayList<String> getModules() {
        return this.modules;
    }

    public boolean addModule(String name) {
        return this.modules.add(name);
    }
}
