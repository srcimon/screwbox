package dev.screwbox.playground.world;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.fluids.FluidComponent;
import dev.screwbox.core.environment.fluids.FluidEffectsComponent;
import dev.screwbox.core.environment.fluids.FluidRenderComponent;
import dev.screwbox.core.environment.fluids.FluidTurbulenceComponent;
import dev.screwbox.core.environment.rendering.ReflectionComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.TileMap;

public class Water implements SourceImport.Converter<TileMap.Block<Color>> {

    @Override
    public Entity convert(TileMap.Block<Color> block) {
        return new Entity().name("water")
                .bounds(block.bounds().expandTop(-8))
                .add(new FluidEffectsComponent())
                .add(new FluidRenderComponent())
                .add(new FluidComponent((int)(block.bounds().width() / 16.0)))
                .add(new FluidTurbulenceComponent(80))
                .add(new ReflectionComponent(Percent.half(), 0), config -> {
                    config.applyWaveDistortionPostFilter = true;
                    config.frequencyX = 0.2;
                    config.frequencyY = 0.4;
                    config.speed = 0.002;
                });
    }
}
