---
sidebar_position: 2
---

# Set up your first project

Setup your first runnable ScrewBox project.

:::note

Screenshots and examples in this documentation will use [IntelliJ IDEA](https://www.jetbrains.com/idea/).
But of course you can use your ide of choice.

:::

## Project setup

Create a new project in your Java IDE.
Make sure you use Java 21 or a newer version.
Add the dependency to `screwbox-core` to your project (Maven or Gradle).

#### Maven

``` xml
<dependency>
    <groupId>io.github.srcimon</groupId>
    <artifactId>screwbox-core</artifactId>
    <version>2.16.0</version>
</dependency>
```

#### Gradle

```
implementation group: 'io.github.srcimon', name: 'screwbox-core', version: '2.16.0'
```

Create a new class which will be used as the starter of your game and add a main-method.
Create a new instance of the ScrewBox engine using `ScrewBox.createEngine()`.
You can also specify a name for your engine.
Start the engine.
Your code will look something like the code below.
This is your first app using ScrewBox. Congrats!

``` java title="HelloWorld.java"
public static void main(String[] args) {
    Engine screwBox = ScrewBox.createEngine("Hello World");
    screwBox.start();
}
```

## Adding content

When you start your application a black window will open.
Not very impressive right now.
To finish your hello world application we will now add a particle effect.
The setup of your engine will always happen before executing the `.start()` method.
Content in ScrewBox is added via the so called `Environment`.
The `Environment` is a container for an entity component system.
What this means and how to use it will be explained in [this article](../ecs.md)
For now we will simply enable all capabilities of ScrewBox via calling `.enableAllFeatures()` and adding an entity.
The entity is just a data container which will be automatically processed by the features previously enabled.
In this case we will add four components to create the particle effect:

- `TransformComponent` sets the position and size of the `Entity`
- `CursorAttachmentComponent` binds the position of the `Entity` to the cursor.
- `ParticleEmitterComponent` emits particles from the `Entity`
- `ParticleInteractionComponent` allows the `Entity` to move the emitted particles by moving the cursor

The code will look something like this:

``` java
public class HelloWorld {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello world");

        screwBox.environment()
            .enableAllFeatures()
            .addEntity(
                new TransformComponent(),
                new CursorAttachmentComponent(),
                new ParticleEmitterComponent(Duration.ofMillis(100), ParticlesBundle.CONFETTI),
                new ParticleInteractionComponent(40, Percent.max()));

        screwBox.start();
    }
}
```

And this is your code in action:

![hello world app](hello-world.png)

## Dealing with console output

You might have noticed that your console will contains some warnings.

``` text
09:47:35 [WARNING] Please run application with the following JVM option to avoid massive fps drop: -Dsun.java2d.opengl=true
09:47:35 [WARNING] Please run application with the following JVM option to add full MacOs support: --add-opens=java.desktop/com.apple.eawt=ALL-UNNAMED
09:47:35 [INFO   ] 'Hello world' started using engine version 0.0.0 (dev-mode)
09:47:37 [INFO   ] engine stopped after running for 1s, 787ms and rendering 212 frames
```

These warnings will warn you about recommended JVM options that you should apply to have a nicer experience.
To set the JVM options when running your application go to *RunConfigurations → Modify options → Add VM Options*.
This will make the warnings disappear.

![idea-vm-options](vm-options.png)

:::warning

Not adding the `-Dsun.java2d.opengl=true` option is likely to kill the performance of your game completely.

:::

#### MacOs errors

MacOs users will also experience some red error messages like these below.
You can simply ignore those.
They won't affect your application.
Also these come and go with os updates.
And other open source project experience them as well.
Unfortunately there is currently no way to hide these.

``` text
2025-01-25 09:47:35.498 java[2300:79574] +[IMKClient subclass]: chose IMKClient_Modern
2025-01-25 09:47:35.498 java[2300:79574] +[IMKInputSession subclass]: chose IMKInputSession_Modern
```