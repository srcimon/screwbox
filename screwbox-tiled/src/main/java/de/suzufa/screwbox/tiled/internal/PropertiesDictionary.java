package de.suzufa.screwbox.tiled.internal;

import java.util.HashMap;
import java.util.Map;

import de.suzufa.screwbox.tiled.Properties;

public class PropertiesDictionary {

    private Map<Integer, Properties> properties = new HashMap<>();

    public void add(final int id, final Properties properties) {
        this.properties.put(id, properties);
    }

// TODO: test
    public Properties get(int id) {
        if (!properties.containsKey(id)) {
            return DefaultProperties.empty();
        }
        return properties.get(id);
    }

}
