package dev.screwbox.playground.world;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SourceImport;
import dev.screwbox.core.environment.rendering.ReflectionComponent;
import dev.screwbox.core.utils.TileMap;

public class Water implements SourceImport.Converter<TileMap.Block<Character>> {

    @Override
    public Entity convert(TileMap.Block<Character> block) {
        return new Entity().name("water")
                .bounds(block.bounds())
                .add(new ReflectionComponent(Percent.half(), 0), c -> {
                    c.applyWaveDistortionPostFilter = true;
                    c.frequencyX = 0.2;
                    c.frequencyY = 0.4;
                    c.speed = 0.002;
                });
    }
}
