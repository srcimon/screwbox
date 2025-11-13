package dev.screwbox.playground.softbody;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class SoftbodyComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public transient List<Entity> nodes = new ArrayList<>();

    @Serial
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        nodes = new ArrayList<>();
    }
}
