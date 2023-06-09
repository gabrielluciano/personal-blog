import { Page } from "../models/page";
import { PostReponse } from "../models/post/postResponse";
import { postsMock } from "./postsMock";

export const postsPageMock: Page<PostReponse> = {
  content: postsMock,
  pageable: {
    sort: {
      empty: true,
      sorted: false,
      unsorted: true
    },
    offset: 0,
    pageNumber: 0,
    pageSize: 5,
    unpaged: false,
    paged: true
  },
  last: true,
  totalPages: 2,
  totalElements: 10,
  size: 10,
  number: 0,
  sort: {
    empty: true,
    sorted: false,
    unsorted: true
  },
  first: true,
  numberOfElements: 5,
  empty: false
};
