import { DOCUMENT } from '@angular/common';
import { Inject, Injectable } from '@angular/core';
import { Meta, MetaDefinition, Title } from '@angular/platform-browser';

export interface MetaInfo {
  title: string;
  description: string;
  imageUrl: string;
  canonicalUrl: string;
}

@Injectable({
  providedIn: 'root',
})
export class MetaService {
  constructor(
    private meta: Meta,
    private title: Title,
    @Inject(DOCUMENT) private dom: Document,
  ) {}

  setTitle(title: string) {
    this.title.setTitle(title);
    this.createOrUpdateMetaTag('property', 'og:title', title);
  }

  setDescription(description: string) {
    this.createOrUpdateMetaTag('name', 'description', description);
    this.createOrUpdateMetaTag('property', 'og:description', description);
  }

  setImage(url: string) {
    this.createOrUpdateMetaTag('property', 'og:image', url);
  }

  setCanonicalUrl(url: string) {
    const head = this.dom.querySelector('head');
    if (!head) return;
    let canonical = head.querySelector('link[rel="canonical"]');
    if (canonical == null) {
      canonical = this.dom.createElement('link');
      head.appendChild(canonical);
    }
    canonical.setAttribute('rel', 'canonical');
    canonical.setAttribute('href', url);
  }

  setMetaInfo({ title, description, imageUrl, canonicalUrl }: MetaInfo) {
    this.setTitle(title);
    this.setDescription(description);
    this.setImage(imageUrl);
    this.setCanonicalUrl(canonicalUrl);
  }

  private createOrUpdateMetaTag(attribute: 'property' | 'name', value: string, content: string) {
    const tag: MetaDefinition = {};
    tag[attribute] = value;
    tag['content'] = content;

    if (this.meta.getTag(`${attribute}='${value}'`)) {
      this.meta.updateTag(tag);
      return;
    }
    this.meta.addTag(tag);
  }
}
