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
      title="{`${siteConfig.title}`}"
      description="ScrewBox is a minimalist pure Java game engine. If you want to start building your own 2D game without leaving your cozy IDE it might be a fun choice.">
      <Header />
      <main>
        <About />
        <Features />
      </main>
    </Layout>
  );
}
