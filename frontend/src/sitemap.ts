import express from 'express';
import { EnumChangefreq, SitemapStream } from 'sitemap';
import { createGzip } from 'zlib';
import { Page } from './app/models/page';
import { PostReponse } from './app/models/post/postResponse';
import { TagResponse } from './app/models/tag/tagResponse';
import { environment } from './environments/environment';

export default async function (_req: express.Request, res: express.Response) {
  res.header('Content-Type', 'application/xml');
  res.header('Content-Encoding', 'gzip');
  res.header('Cache-Control', 's-maxage=259200');

  try {
    const sitemapStream = new SitemapStream({ hostname: environment.siteUrl });
    const pipeline = sitemapStream.pipe(createGzip());

    sitemapStream.write({ url: '/posts', changefreq: EnumChangefreq.WEEKLY, priority: 1.0 });
    await appendPostUrls(sitemapStream);
    await appendTagUrls(sitemapStream);
    sitemapStream.end();

    pipeline.pipe(res).on('error', (e) => {
      throw e;
    });
  } catch (error) {
    console.error(error);
    res.status(500).end();
  }
}

async function appendPostUrls(sitemapStream: SitemapStream): Promise<void> {
  try {
    const res = await fetch(`${environment.apiUrl}posts?size=2000`);
    const data = (await res.json()) as Page<PostReponse>;
    const posts = data.content;

    posts.forEach((post) =>
      sitemapStream.write({
        url: `/posts/${post.slug}`,
        changeFreq: EnumChangefreq.YEARLY,
        priority: 0.8,
      }),
    );
  } catch (error) {
    console.error(error);
  }
}

async function appendTagUrls(sitemapStream: SitemapStream): Promise<void> {
  try {
    const res = await fetch(`${environment.apiUrl}tags?size=2000`);
    const data = (await res.json()) as Page<TagResponse>;
    const tags = data.content;

    tags.forEach((tag) =>
      sitemapStream.write({
        url: `/posts/tag/${tag.id}/${tag.slug}`,
        changeFreq: EnumChangefreq.WEEKLY,
        priority: 0.5,
      }),
    );
  } catch (error) {
    console.error(error);
  }
}
