package de.suzufa.screwbox.core.entityengine.components;

import java.util.ArrayList;

import de.suzufa.screwbox.core.entityengine.Component;

public class ForwardSignalComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final ArrayList<Integer> listenerIds = new ArrayList<>();
}
