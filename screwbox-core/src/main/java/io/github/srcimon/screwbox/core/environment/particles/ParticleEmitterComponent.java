package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.io.Serial;

public class ParticleEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public enum SpawnMode {
        POSITION,
        AREA
    }

    public boolean isEnabled = true;
    public SpawnMode spawnMode;
    public Sheduler sheduler;
    public ParticleDesigner designer;


    public ParticleEmitterComponent(final Sheduler sheduler, final ParticleDesigner designer) {
        this(sheduler, SpawnMode.AREA, designer);
    }

    public ParticleEmitterComponent(final Sheduler sheduler, final SpawnMode spawnMode, final ParticleDesigner designer) {
        this.sheduler = sheduler;
        this.spawnMode = spawnMode;
        this.designer = designer;
    }
}
