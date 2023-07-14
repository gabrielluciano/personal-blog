export interface PostUpdateRequest {
  title: string;
  subtitle: string;
  slug: string;
  metaTitle: string;
  metaDescription: string;
  imageUrl: string;
  content?: string;
}
