import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const config: Config = {
  title: 'ScrewBox - Minimalist pure Java game engine.',
  tagline: 'ScrewBox is a minimalist pure Java game engine. If you want to start building your own 2D game without leaving your cozy IDE it might be a fun choice.',
  favicon: 'img/favicon.ico',
  organizationName: 'srcimon',
  projectName: 'screwbox',
  url: `https://screwbox.dev`,
  baseUrl: '/',
  markdown: {
    mermaid: true,
  },
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'throw',
    themes: [
    [
      require.resolve("@easyops-cn/docusaurus-search-local"),
      ({
        hashed: true,
      }),
    ],
     '@docusaurus/theme-mermaid',
  ],
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          sidebarPath: './sidebars.ts',
          sidebarCollapsible: true,
        },
        blog: {
          showReadingTime: false,
          feedOptions: {
            type: ['rss', 'atom'],
            xslt: true,
          },
          onInlineTags: 'warn',
          onInlineAuthors: 'warn',
          onUntruncatedBlogPosts: 'warn',
        },
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themeConfig: {
   docs: {
        sidebar: {
          autoCollapseCategories: false,
        },
    },
    image: 'img/screenshot.png',
    navbar: {
      title: 'ScrewBox',
      logo: {
        alt: 'ScrewBox Logo',
        src: 'img/logo.png',
      },
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'docsSidebar',
          position: 'left',
          label: 'Documentation',
        },
        {to: '/blog', label: 'Blog', position: 'left'},
        {
          href: 'https://github.com/srcimon/screwbox/releases',
          label: 'Releases',
        },
        {
          href: 'https://github.com/srcimon/screwbox',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Knowledge',
          items: [
            {
              label: 'Documentation',
              to: '/docs/fundamentals/getting-started',
            },
            {
              label: 'JavaDoc',
              href: 'https://javadoc.io/doc/dev.screwbox/screwbox-core',
            },
            {
              label: 'Releases',
              href: 'https://github.com/srcimon/screwbox/releases',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/srcimon/screwbox',
            },
            {
              label: 'Sonar Cloud',
              href: 'https://sonarcloud.io/project/overview?id=srcimon_screwbox',
            }
          ],
        },
        {
          title: 'Legal',
          items: [
            {
              label: 'Disclaimer',
              to: '/disclaimer',
            },
            {
              label: 'Impressum',
              to: '/impressum',
            },
            {
              label: 'Datenschutzhinweis',
              to: '/datenschutzhinweis',
            },
          ],
        }
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Sebastian Simon`,
    },

    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
      additionalLanguages: ['java']
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
