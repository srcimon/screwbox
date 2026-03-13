# Tilemaps

:::info about this guide
This guide shows how to use tilemaps to easily import entities from an image or a text.
:::

## Creation and usage

The `TileMap` provides support for creating entities out of an existing image or text.
It's main purpose is to provide quick help with prototyping a game or scene.
It only provides a limited set of functionality and when your are planning to create a more detailed
scene with multiple layers etc. a more specialized tool like the TiledEdior should be the preferred tool for content creation.

Tile maps can be created with a text input source or an image.

``` java
var map = TileMap.fromString("""
   ##############
   #....        #
   #     P      #
   #            #
   #     .....###
   ##############
    """);
    
var mapFromIamge = TileMap.fromImageFile("level.png");
```

The size of every tile also can be specified in the constructor calls.
It will use create 16x16 tiles when no other value is specified.

The `TileMap` will provide access to various sources that can be imported using the environment API:
- `map.tiles()` will return all tiles within the map with specific bounds and a value that is dependent on the import source.
In the example above there will be a single tile with value 'P' and lots of tiles with value '.' and '#'.
Every tile will also provide an `AutoTileMask` that can be used for auto tiling.

- `map.blocks()` will return all blocks within the map.
A block is defined by multiple aligning tiles with at least two tiles.
Blocks will be created preferred horizontally.
Blocks will also carry a value.
In the the example above will be two blocks with value '.' and multiple blocks with value '#'.

When using a bitmap instead of text the value will be the color of the corresponding pixel.

## Importing entities from a tilemap

The import API of the environment comes pretty handy when working with tilemaps.
Lets create some actual level from the tilemap above.

``` java
environment
    // import walls
    .importSource(ImportOptions.indexedSources(map.tiles(), TileMap.Tile::value)
        .assign('#', tile -> new Entity()
            .bounds(tile.bounds())
            .add(new RenderComponent(Sprite.placeholder(Color.RED, 16)))

    // import player
        .assign('P', tile -> new Entity()
            .bounds(tile.bounds())
            .add(..)))
        
    // import water
    .importSource(ImportOptions.indexedSources(map.blocks(), TileMap.Block::value)
        .assign('.', new Water());
```