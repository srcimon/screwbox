# Particles

Add particle effects to your game using `engine.particles()`.

## About particles

Particles are one of the easiest ways to make your game feel more polished.
Particles in ScrewBox are just entities marked with a `ParticleComponent`.
This component is used to calculate current particle count in the `Environment`.

## Spawning particles

Particles can be spawned using on of the spawn methods in `Particles`.
The particle look and behaviour can be specified using `ParticleOptions`.
The `ParticleOptions` provide a huge variety on customization options
that can also be extended using the `customize` method as seen below:

``` java
Entity player = //...
particles.spawn(player.bounds(), SpawnMode.BOTTOM_SIDE, ParticleOptions.source(player)
        .sprite(SpriteBundle.DOT_BLUE)
        .randomBaseSpeed(20)
        .animateOpacity()
        .customize("use-gravity", particle -> particle.get(PhysicsComponent.class).gravityModifier = 1));
```

Particle entities will be provided with a `PhysicsComponent` but will ignore gravity and collisions by default.
Particle entities will also self destroy using a `TweenDestroyComponent`.
Of cause the lifetime can be customized as well.

## Limiting particles

Particles are considered decoration an will therefore be limited to a maximum count to limit cpu usage.
The limit can also be customized.
Particles will also not spawn when they are too far away from the players attention area.
This will also work when using [split screen](../guides/split-screen/index.md).
The spawn distance can be customized.

``` java
paritcles.setParticleLimit(2000);
particles.setSpawnDistance(3000);
long currentParticleCount = particles.particleCount();
long allTimeParticleCount = particles.particlesSpawnCount();
```

## Using the entity system

The entity component system is also handy when working with particles.

To automatically spawn particles use the `ParticleComponent`.
The `ParticleBurstComponent` will shut down the spawn after a timeout.
Add a `TailwindComponent` to a physics entity to propel particles nearby in the direction of motion.


See [Components Overview](../reference/components-overview.md) for a full list of all particle related components.