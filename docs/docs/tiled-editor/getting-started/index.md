# Getting started

ScrewBox supports importing maps and tilesets from [Tiled Editor](https://www.mapeditor.org).
Tiled Editor has a [large community](https://github.com/mapeditor/tiled) and is quite well known and already used in more professional projects.
Learn more on the [official site](https://www.mapeditor.org) of the project or have a look at this [tutorial series on youtube](https://www.youtube.com/playlist?list=PLu4oc9P-ABcOXNOyoAvnMyUwn_kkiVA5B).

![tiled-editor.png](tiled-editor.png)

## Project setup

To use this functionality please add the `screwbox-tiled` dependency to your project:

``` xml
<dependency>
    <groupId>dev.screwbox</groupId>
    <artifactId>screwbox-tiled</artifactId>
    <version><!-- same as screwbox-core --></version>
</dependency>
```

You can also use the `screwbox-bom` to manage your dependencies if you don't want to synchronize version numbers.

``` xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>dev.screwbox</groupId>a
            <artifactId>screwbox</artifactId>
            <version><!-- your version --></version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

After adding the dependency content from Tiled Editor can be imported.
Learn [how to import map](./../importing-maps.md) and [how to import tilesets](./../importing-tilesets.md).