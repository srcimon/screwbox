import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';
import Link from '@docusaurus/Link';

export default function Header() {
    const {siteConfig} = useDocusaurusContext();
    return (
        <header className={clsx('hero hero--secondary', styles.heroBanner)}>
            <div className="container">
                <Heading as="h1" className="hero__title">
                    ScrewBox
                </Heading>

                <p className="hero__subtitle">ScrewBox is a minimalist pure Java game engine. If you want to <b
                    class="hero__slogan">start building your own 2D game without leaving your cozy IDE</b> it might be a
                    fun choice. Source code is available at GitHub and published under MIT licence.</p>
                <Link
                    className="hero__button button button--primary button--lg"
                    to="/docs/fundamentals/getting-started/">
                    Getting Started →
                </Link>
                <Link
                    className="hero__button button button--secondary button--lg"
                    to="https://github.com/srcimon/screwbox">
                    Github
                </Link>
            </div>

        </header>
    );
}