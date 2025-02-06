import type {ReactNode} from 'react';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import Features from '@site/src/components/Features';
import Header from '@site/src/components/Header';
import About from '@site/src/components/About';
import Heading from '@theme/Heading';

export default function Home(): ReactNode {
  const {siteConfig} = useDocusaurusContext();
  return (
    <Layout
      title={`${siteConfig.title} - Minimalist 2D Java game engine.`}
      description="Minimalist 2D Java game engine.">
      <Header />
      <main>
        <About />
        <Features />
      </main>
    </Layout>
  );
}
