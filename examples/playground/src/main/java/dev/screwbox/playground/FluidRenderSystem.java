//package dev.screwbox.playground;
//
//import dev.screwbox.core.Engine;
//import dev.screwbox.core.environment.Archetype;
//import dev.screwbox.core.environment.EntitySystem;
//import dev.screwbox.core.graphics.Sprite;
//import dev.screwbox.core.graphics.internal.ImageOperations;
//import dev.screwbox.core.graphics.options.SpriteDrawOptions;
//import dev.screwbox.playground.simulate.FluidSimulationComponent;
//
//import java.awt.image.BufferedImage;
//import java.awt.image.DataBufferInt;
//
//import static java.util.Objects.nonNull;
//
//public class FluidRenderSystem implements EntitySystem {
//
//    //TODO Optimization: reuse image
//    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidSimulationComponent.class, FluidRenderComponent.class);
//
//    @Override
//    public void update(Engine engine) {
//        for (final var fluid : engine.environment().fetchAll(FLUIDS)) {
//            var simulationComponent = fluid.get(FluidSimulationComponent.class);
//            if (nonNull(simulationComponent.simulation)) {
//                BufferedImage image = new BufferedImage(simulationComponent.simulation.width(), simulationComponent.simulation.height(), BufferedImage.TYPE_INT_ARGB);
//                var pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
//
//                int width = image.getWidth();
//
//                for (int y = 0; y < image.getHeight(); y++) {
//                    int pixelIndex = y * width;
//                    for (int x = 0; x < width; x++) {
//
//                        int r = (int) (Math.clamp(simulationComponent.simulation.density(x, y), 0, 1.0) * 255);
//                        int g = r;
//                        int b = r;
//                        pixels[pixelIndex + x] = (255 << 24) | (r << 16) | (g << 8) | b;
//                    }
//                }
//
//                ImageOperations.blurImage(image, 3);
//                engine.graphics().world().drawSprite(Sprite.fromImage(image), fluid.origin(), SpriteDrawOptions.scaled(fluid.bounds().width() / simulationComponent.simulation.width()));
//                //TODO scale x and y independently or throw exception when not implemented
//            }
//        }
//    }
//}
