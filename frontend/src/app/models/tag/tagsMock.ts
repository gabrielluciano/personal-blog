import { Page } from '../page';
import { TagResponse } from './tagResponse';

export const tagsMock: TagResponse[] = [
  {
    id: 1,
    name: 'Technology',
    description: 'Posts related to technology',
    slug: 'technology',
  },
  {
    id: 2,
    name: 'Programming',
    description: 'Posts related to programming',
    slug: 'programming',
  },
  {
    id: 3,
    name: 'Artificial Intelligence',
    description: 'Posts related to artificial intelligence',
    slug: 'ai',
  },
  {
    id: 4,
    name: 'Web Development',
    description: 'Posts related to web development',
    slug: 'web-development',
  },
  {
    id: 5,
    name: 'Machine Learning',
    description: 'Posts related to machine learning',
    slug: 'machine-learning',
  },
];

export const tagsPageMock: Page<TagResponse> = {
  content: tagsMock,
  pageable: {
    sort: {
      empty: true,
      sorted: false,
      unsorted: true,
    },
    offset: 0,
    pageNumber: 0,
    pageSize: tagsMock.length,
    unpaged: false,
    paged: true,
  },
  last: true,
  totalPages: 1,
  totalElements: tagsMock.length,
  size: tagsMock.length,
  number: 0,
  sort: {
    empty: true,
    sorted: false,
    unsorted: true,
  },
  first: true,
  numberOfElements: tagsMock.length,
  empty: false,
};
