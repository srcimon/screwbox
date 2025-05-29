# Audio

The `engine.audio()` component allows playback of sounds and retrieving microphone input level.

## Playing sounds

:::info
Currently only the wav and midi sound format is supported.
This might change in the future.
:::

The `Sound` class allows storing of sound data.
To create a sound place a supported sound file in the `src/main/resource` directory and import it.
It's highly recommended to load sounds using [Assets](assets) to avoid playback latency and stuttering.

``` java
// preferred way
Asset<Sound> soundAsset = Sound.assetFromFile("sounds/jump.wav");

engine.audio().playSound(soundAsset);

// async loading is also supported
Sound sound = Sound.fromFile("sounds/jump.wav");
engine.audio().playSound(sound);
```

You can also use the prepacked sounds using from the `SoundBundle`.

### Customizing playback

Sound playback can be customized using the `SoundOptions` parameter.

``` java
// play custom sound
audio.playSound(SoundBundle.SPLASH, SoundOptions
    .playTimes(4)
    .speed(1.2)
    .pan(-0.3)
    .volume(Percent.of(0.3)));
    
// play at a specific position
Playback playback = audio.playSound(SoundBundle.SPLASH, SoundOptions
    .position(playerPosition));
```

You can choose one or multiple of the following configuration options for playback:

| Option     | Description                                                                         |
|------------|-------------------------------------------------------------------------------------|
| times      | Number of times the sound is played                                                 |
| volume     | volume of the sound                                                                 |
| pan        | changes the panorama value (similar to left right balance) ranging from -1.0 to 1.0 |
| isMusic    | mark sound as music (configured music will apply)                                   |
| speed      | speed of the playback (0.1 to 10.0), also affects pitch                             |
| position   | set position of sound to automatically detect volume and pan                        |
| randomness | add randomness to the speed of the playback                                         |

:::info
Currently there is no way to preserve the configuration when quitting the game.
If you need this please tell me by commenting on https://github.com/srcimon/screwbox/issues/439.
:::

### Spatial effects

The position parameter uses the configured sound range [see below](#audio-configuration) to detect volume and pan.
Playing sound with a position is the recommended way to add some spatial feeling to your games audio.

### Playbacks

Playing a sound returns a `Playback` instance.
The `Playback` can be used to gain info on the sound or to change it's options after it started.

:::warning
Changing the speed of a sound after it started is not possible and will result in an exception.
To change playback speed ScrewBox creates audio lines with a higher playback rate.
This is kind of a hack and introduces this restriction.
:::

### Audio configuration

The `AudioConfiguration` can be used to change music and effect volumes and the sound range for [spacial audio effects](#spatial-effects).

### Using ECS for playback

To link a continuous playback to an entity add a `SoundComponent` to that entity.

## Microphone

Audio allows reading the current volume level of the microphone using `audio.microphoneLevel()`.
The volume can be used as special input device as can be seen [in this demo (Youtube)](https://www.youtube.com/shorts/YsotSkrkftk).