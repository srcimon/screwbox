package io.github.simonbas.screwbox.core.entities.components;

import io.github.simonbas.screwbox.core.entities.Component;

import java.util.ArrayList;
import java.util.List;

public class ForwardSignalComponent implements Component {

    private static final long serialVersionUID = 1L;

    public final List<Integer> listenerIds = new ArrayList<>();
}
