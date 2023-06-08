import { TagResponse } from '../tag/tagResponse';
import { UserResponse } from '../user/userResponse';

export interface PostReponse {
  id: number;
  title: string;
  subtitle: string;
  content: string;
  metaTitle: string;
  metaDescription: string;
  slug: string;
  imageUrl: string;
  published: boolean;
  createdAt: string;
  updatedAt: string;
  publishedAt: string;
  tags: TagResponse[];
  author: UserResponse;
}
