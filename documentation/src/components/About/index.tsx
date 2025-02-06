import type {ReactNode} from 'react';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import Link from '@docusaurus/Link';

export default function About(): ReactNode {
  return (
    <section>
      <div className="container">
        <Heading as="h1" className="subheading">Feature Overview</Heading>
        <p className="hero__subtitle subheading">
        <b class="hero__slogan">ScrewBox uses a purely code based approach on creating games to not force you into using a proprietary content editor.</b> These features will be available when coding with ScrewBox:
        </p>
      </div>
    </section>
  );
}
