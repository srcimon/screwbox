import type {ReactNode} from 'react';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';

type FeatureItem = {
    title: string;
    Svg: React.ComponentType<React.ComponentProps<'svg'>>;
    description: ReactNode;
};

const FeatureList: FeatureItem[] = [
    {
        title: 'Entity System',
        Svg: require('@site/static/img/features/ecs.png').default,
        description: "Use the embedded ECS to add and remove game objects or behaviour, save and load the game state.",
        readMore: '/docs/core-modules/environment',
    },
    {
        title: 'Rendering',
        Svg: require('@site/static/img/features/rendering.png').default,
        description: "Render animated sprites, text and shapes at 120 fps. Enjoy cheerful animated water reflections.",
        readMore: '/docs/core-modules/graphics',
    },
    {
        title: 'Particles',
        Svg: require('@site/static/img/features/particles.png').default,
        description: "Spawn particles from any surface or position. Utilize particles to create fire, smoke or any other effect of choice.",
        readMore: '/docs/core-modules/particles',
    },
    {
        title: 'Camera',
        Svg: require('@site/static/img/features/camera.png').default,
        description: "Let the camera automatically focus on the player. Apply shake, rotation and zoom. Create up to 64 split screens to allow local multiplayer.",
        readMore: '/docs/core-modules/graphics',
    },
    {
        title: 'Dynamic light',
        Svg: require('@site/static/img/features/light.png').default,
        description: "Illuminate your levels using dynamic light sources. Cast some moody shadows with walls.",
        readMore: '/docs/core-modules/graphics',
    },
    {
        title: 'Tweening',
        Svg: require('@site/static/img/features/tweening.png').default,
        description: "Animate any property a game object using tweens. Create spinning coins, flickering lights and sparks orbiting a power up.",
    },
    {
        title: 'Spatial Audio',
        Svg: require('@site/static/img/features/audio.png').default,
        description: "Dynamically change volume and pan with the position of the sound source. Use your microphone as an input device.",
        readMore: '/docs/core-modules/audio',
    },
    {
        title: 'Asset Management',
        Svg: require('@site/static/img/features/assets.png').default,
        description: "Lazy load larger game assets in the background without interrupting the game.",
        readMore: '/docs/core-modules/assets',
    },
    {
        title: 'Scenes',
        Svg: require('@site/static/img/features/scenes.png').default,
        description: "Split your game into different scenes. Add animated transitions to smoothly switch between the scenes.",
        readMore: '/docs/core-modules/scenes',
    },
    {
        title: 'Achievements',
        Svg: require('@site/static/img/features/achievements.png').default,
        description: "Add achievements to challenge players with custom goals.",
        readMore: '/docs/core-modules/achievements',
    },
    {
        title: 'UI',
        Svg: require('@site/static/img/features/ui.png').default,
        description: "Create an animated interactive game ui in an instant. Show in game notifications.",
        readMore: '/docs/core-modules/ui',
    },
    {
        title: 'Tiled Editor Support',
        Svg: require('@site/static/img/features/tiled.png').default,
        description: "Import your game maps and tilesets from the Tiled Editor.",
    },
    {
        title: 'Physics',
        Svg: require('@site/static/img/features/physics.png').default,
        description: "Detect and resolve collisions, create dynamic fluids and floating objects.",
        readMore: '/docs/guides/dynamic-fluids',
    },
    {
        title: 'Soft physics',
        Svg: require('@site/static/img/features/softphysics.png').default,
        description: "Create ropes and soft bodies. Build a jelly physics type of game.",
        readMore: '/docs/guides/soft-physics',
    },
    {
        title: 'Shaders',
        Svg: require('@site/static/img/features/shaders.png').default,
        description: "Let’s add some cool animated and still shaders to make your game look more vibrant.",
        readMore: '/docs/core-modules/graphics',
    },
    {
        title: 'AI and controls',
        Svg: require('@site/static/img/features/ai-and-controls.png').default,
        description: "Move, jump, double jump and use pathfinding for ai enemies as well.",
    },
];

function ReadMore({readMore}: FeatureItem) {
    return readMore
        ? (<a href={readMore}>Read more...</a>)
        : (<> </>);
}

function Feature({title, Svg, description, readMore}: FeatureItem) {
    return (
        <div className={clsx('col col--4')}>
            <div className="text--center">
                <img className={styles.featureSvg} role="img" src={Svg}/>
            </div>
            <div className="text--center padding-horiz--md">
                <Heading as="h3">{title}</Heading>
                <p>{description} <ReadMore readMore={readMore}/></p>
            </div>
        </div>
    );
}

export default function Features(): ReactNode {
    return (
        <section className={styles.features}>
            <div className="container">
                <div className="row">
                    {FeatureList.map((props, idx) => (
                        <Feature key={idx} {...props} />
                    ))}
                </div>
            </div>
        </section>
    );
}
