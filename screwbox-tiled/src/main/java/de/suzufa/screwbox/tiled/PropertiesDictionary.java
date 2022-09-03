package de.suzufa.screwbox.tiled;

import static java.util.Collections.emptyList;

import java.util.HashMap;
import java.util.Map;

class PropertiesDictionary {

    private Map<Integer, Properties> properties = new HashMap<>();

    public void add(final int id, final Properties properties) {
        this.properties.put(id, properties);
    }

// TODO: test
    public Properties get(int id) {
        if (!properties.containsKey(id)) {
            return new Properties(emptyList());
        }
        return properties.get(id);
    }

}
