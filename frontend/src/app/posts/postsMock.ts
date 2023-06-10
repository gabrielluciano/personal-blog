import { PostReponse } from '../models/post/postResponse';

export const postsMock: PostReponse[] = [
  // Post 1
  {
    id: 1,
    title: 'Post 1',
    subtitle: 'Subtitle for Post 1',
    content: 'Content for Post 1',
    metaTitle: 'Meta Title 1',
    metaDescription: 'Meta Description 1',
    slug: 'post-1',
    imageUrl: 'assets/computer-image-150.jpg',
    published: true,
    createdAt: '2023-06-01T10:00:00Z',
    updatedAt: '2023-06-02T15:30:00Z',
    publishedAt: '2023-06-02T16:00:00Z',
    tags: [
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
    ],
    author: {
      id: 1,
      name: 'John Doe',
      email: 'johndoe@example.com',
    },
  },
  // Post 2
  {
    id: 2,
    title: 'Post 2',
    subtitle: 'Subtitle for Post 2',
    content: 'Content for Post 2',
    metaTitle: 'Meta Title 2',
    metaDescription: 'Meta Description 2',
    slug: 'post-2',
    imageUrl: 'assets/computer-image-150.jpg',
    published: true,
    createdAt: '2023-06-03T08:00:00Z',
    updatedAt: '2023-06-04T13:45:00Z',
    publishedAt: '2023-06-04T14:00:00Z',
    tags: [
      {
        id: 1,
        name: 'Technology',
        description: 'Posts related to technology',
        slug: 'technology',
      },
      {
        id: 3,
        name: 'Artificial Intelligence',
        description: 'Posts related to artificial intelligence',
        slug: 'ai',
      },
    ],
    author: {
      id: 2,
      name: 'Jane Smith',
      email: 'janesmith@example.com',
    },
  },
  // Post 3
  {
    id: 3,
    title: 'Post 3',
    subtitle: 'Subtitle for Post 3',
    content: 'Content for Post 3',
    metaTitle: 'Meta Title 3',
    metaDescription: 'Meta Description 3',
    slug: 'post-3',
    imageUrl: 'assets/computer-image-150.jpg',
    published: true,
    createdAt: '2023-06-05T11:00:00Z',
    updatedAt: '2023-06-06T16:30:00Z',
    publishedAt: '2023-06-06T17:00:00Z',
    tags: [
      {
        id: 2,
        name: 'Programming',
        description: 'Posts related to programming',
        slug: 'programming',
      },
      {
        id: 4,
        name: 'Web Development',
        description: 'Posts related to web development',
        slug: 'web-development',
      },
    ],
    author: {
      id: 1,
      name: 'John Doe',
      email: 'johndoe@example.com',
    },
  },
  // Post 4
  {
    id: 4,
    title: 'Post 4',
    subtitle: 'Subtitle for Post 4',
    content: 'Content for Post 4',
    metaTitle: 'Meta Title 4',
    metaDescription: 'Meta Description 4',
    slug: 'post-4',
    imageUrl: 'assets/computer-image-150.jpg',
    published: true,
    createdAt: '2023-06-07T09:00:00Z',
    updatedAt: '2023-06-07T12:15:00Z',
    publishedAt: '2023-06-07T09:00:00Z',
    tags: [
      {
        id: 3,
        name: 'Artificial Intelligence',
        description: 'Posts related to artificial intelligence',
        slug: 'ai',
      },
      {
        id: 5,
        name: 'Machine Learning',
        description: 'Posts related to machine learning',
        slug: 'machine-learning',
      },
    ],
    author: {
      id: 2,
      name: 'Jane Smith',
      email: 'janesmith@example.com',
    },
  },
  // Post 5
  {
    id: 5,
    title: 'Post 5',
    subtitle: 'Subtitle for Post 5',
    content: 'Content for Post 5',
    metaTitle: 'Meta Title 5',
    metaDescription: 'Meta Description 5',
    slug: 'post-5',
    imageUrl: 'assets/computer-image-150.jpg',
    published: true,
    createdAt: '2023-06-07T14:30:00Z',
    updatedAt: '2023-06-07T17:45:00Z',
    publishedAt: '2023-06-07T18:00:00Z',
    tags: [
      {
        id: 1,
        name: 'Technology',
        description: 'Posts related to technology',
        slug: 'technology',
      },
      {
        id: 4,
        name: 'Web Development',
        description: 'Posts related to web development',
        slug: 'web-development',
      },
    ],
    author: {
      id: 1,
      name: 'John Doe',
      email: 'johndoe@example.com',
    },
  },
];
