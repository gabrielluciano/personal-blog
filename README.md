# Personal Blog

This is my personal blog project. The blog will be available soon.

## Endpoints

### Post

```
GET /posts[?title][?tag][?drafts]

    Returns a page of posts (Security: UNAUTHENTICATED/EDITOR)

    Return status:
        200 (OK)

    Query parameters:
        title - Matches all posts that have the value on its title
        tag - Filter posts of a specific tag
        drafts - If true returns not published posts (requires editor role). Defaults to false

    Note: Spring Boot Pageable query parameters are also allowed (check spring docs)
```

```
GET /posts/{id}

    Find a post by id and returns it (Security: UNAUTHENTICATED)

    Return status:
        200 (OK) - The post was found and returned
        404 (NOT_FOUND) - The post was not found
```

```
GET /posts/slug/{slug}

    Find a post by slug and returns it (Security: UNAUTHENTICATED)

    Return status:
        200 (OK) - The post was found and returned
        404 (NOT_FOUND) - The post was not found
```

```
POST /posts

    Creates a new post and returns it (Security: EDITOR)

    Return status:
        201 (CREATED) - Post created with success
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /posts/{id}

    Updates an existing post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Post updated with success
        404 (NOT_FOUND) - The post was not found
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
DELETE /posts/{id}

    Deletes an existing post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Post deleted with success
        404 (NOT_FOUND) - The post was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /posts/{postId}/tags/{tagId}

    Adds a new tag to an existing post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Tag was successfully added to the post
        404 (NOT_FOUND) - The post or tag was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
DELETE /posts/{postId}/tags/{tagId}

    Deletes a tag from an existing post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Tag was successfully deleted from the post
        404 (NOT_FOUND) - The post or tag was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /posts/{postId}/publish

    Publish a post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - The command was successfuly executed
        404 (NOT_FOUND) - The post was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /posts/{postId}/unpublish

    Unpublish a post (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - The command was successfuly executed
        404 (NOT_FOUND) - The post was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

### Tag

```
GET /tags

    Returns a page of tags (Security: UNAUTHENTICATED)

    Return status:
        200 (OK)

    Note: Spring Boot Pageable query parameters are allowed (check spring docs)
```

```
GET /tags/{id}

    Find a tag by id and returns it (Security: UNAUTHENTICATED)

    Return status:
        200 (OK) - The tag was found and returned
        404 (NOT_FOUND) - The tag was not found
```

```
POST /tags

    Creates a new tag and returns it (Security: EDITOR)

    Return status:
        201 (CREATED) - Tag created with success
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
PUT /tags/{id}

    Updates an existing tag (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Tag updated with success
        404 (NOT_FOUND) - The tag was not found
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
DELETE /tags/{id}

    Deletes an existing tag (Security: EDITOR)

    Return status:
        204 (NO_CONTENT) - Tag deleted with success
        404 (NOT_FOUND) - The tag was not found
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

### User

```
POST /signup

    Allows user to create an account providing name, email and password

    Return status:
        201 (CREATED) - User created with success
        400 (BAD_REQUEST) - Request body is invalid
```

```
POST /login

    Allows user to login using its email and password returning a JWT Token

    Return status:
        200 (OK) - User successfully logged in
        401 (UNAUTHORIZED) - Wrong credentials were provided
```

```
PUT /users/{id}/roles/editor

    Grant EDITOR role to an user (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Role EDITOR was successfully granted to the user
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```

```
DELETE /users/{id}/roles/editor

    Removes EDITOR role from an user (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Role EDITOR was successfully removed from the user
        401 (UNAUTHORIZED) - User is not authorized to perform this action
```
