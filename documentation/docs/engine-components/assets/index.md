# Assets

## Using assets

To reduce loading time of your application and to avoid stuttering graphic and sound resources should always be loaded
lazy using assets.
Assets are contants within your game classes.
Different ways to define game assets:

``` java
private static final Asset<Sprite> PLAYER = Sprite.assetFromFile("player.png");
private static final Asset<Sprite> ENEMY = Asset.asset(() -> Sprite.fromFile("enemy.png"));
```

The `Asset` class is a wrapper that will load and provide it's content when it's used for the first time.
So anything that might take some time to load can and should be wrapped in an `Asset`.
To use an `Asset` simply pass it to one of the engines methods or call `asset.get()` to receive its content.

``` java
engine.graphics().world().drawSprite(PLAYER, $(10, 20), originalSize());
```

To avoid stuttering when the assets are loaded, simply start preloading the assets before accessing them.
The preloading will be done in the background without interrupting the game.

``` java
// load all assets within the same package as the PlatformerApp.class
engine.assets().prepareClassPackageAsync(PlatformerApp.class);
```

## Asset bundles

ScrewBox ships some example assets like sounds, fonts and sprites.
The prepacked assets are called `AssetBundle`.
If you are using these bundled assets, you can also preload these using `assets.prepareEngineAssetsAsync()`.
You can also create your own asset bundles via creating an enum implementing the `AssetBundle` interface.

``` java title="using a prepacked asset bundle"
engine.audio().playSound(SoundBundle.ZISCH);
```

``` java title="custom asset bundle"
public enum PlayerSounds implements AssetBundle<Sound> {

    JUMP,
    WALK,
    DIE;

    private final Asset<Sound> sound;

    PlayerSounds() {
        this.sound = Sound.assetFromFile(this.name() + ".wav");
    }

    @Override
    public Asset<Sound> asset() {
        return sound;
    }
}
```

### Prepacked Asset bundles

These asset bundles are prepacked with the engine.

- `FontBundle` packs some pixelfonts
- `SoundBundle` packs sounds
- `SpriteBundle` packs animated and still graphics
- `ParticlesBundle` packs some particle effects