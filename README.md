# Personal Blog

This is my personal blog project. The blog will be available soon.

## Endpoints

### Post

```
GET /posts[?title][?tag][?drafts]

    Returns a page of posts (Security: UNAUTHENTICATED/ADMIN)

    Return status: 
        200 (OK)
        
    Query parameters:
        title - Matches all posts that have the value on its title
        tag - Filter posts of a specific tag
        drafts - If true returns not published posts (requires admin role). Defaults to false
    
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

    Creates a new post and returns it (Security: ADMIN)

    Return status:
        201 (CREATED) - Post created with success
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - When authentication is not valid or isn't present
        403 (FORBBIDEN) - User is not an admin
```

```
PUT /posts/{id}

    Updates an existing post (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Post updated with success
        404 (NOT_FOUND) - The post was not found
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - When authentication is not valid or isn't present
        403 (FORBBIDEN) - User is not an admin or is not the author of the post
```

```
DELETE /posts/{id}

    Deletes an existing post (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Post deleted with success
        404 (NOT_FOUND) - The post was not found
        401 (UNAUTHORIZED) - When authentication is not valid or isn't present
        403 (FORBBIDEN) - User is not an admin or is not the author of the post
```

```
PUT /posts/{postId}/tags/{tagId}

    Adds a new tag to an existing post (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Tag was successfully added to the post
        404 (NOT_FOUND) - The post or tag was not found
        401 (UNAUTHORIZED) - When authentication is not valid or isn't present
        403 (FORBBIDEN) - User is not an admin or is not the author of the post
```

```
DELETE /posts/{postId}/tags/{tagId}

    Deletes a tag from an existing post (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Tag was successfully deleted from the post
        404 (NOT_FOUND) - The post or tag was not found
        401 (UNAUTHORIZED) - When authentication is not valid or isn't present
        403 (FORBBIDEN) - User is not an admin or is not the author of the post
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

    Creates a new tag and returns it (Security: ADMIN)

    Return status:
        201 (CREATED) - Tag created with success
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - When authentication is not valid or isn't present
        403 (FORBBIDEN) - User is not an admin
```

```
PUT /tags/{id}

    Updates an existing tag (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Tag updated with success
        404 (NOT_FOUND) - The tag was not found
        400 (BAD_REQUEST) - In case of not valid request body
        401 (UNAUTHORIZED) - When authentication is not valid or isn't present
        403 (FORBBIDEN) - User is not an admin
```

```
DELETE /tags/{id}

    Deletes an existing tag (Security: ADMIN)

    Return status:
        204 (NO_CONTENT) - Tag deleted with success
        404 (NOT_FOUND) - The tag was not found
        401 (UNAUTHORIZED) - When authentication is not valid or isn't present
        403 (FORBBIDEN) - User is not an admin
```