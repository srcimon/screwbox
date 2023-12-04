package io.github.srcimon.screwbox.core.environment.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.util.ArrayList;
import java.util.List;

public class ForwardSignalComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final List<Integer> listenerIds = new ArrayList<>();
}
