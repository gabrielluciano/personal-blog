package com.gabrielluciano.blog.exceptions;

public class PaginationException extends RuntimeException {

    public PaginationException(Integer page, Integer size) {
        super("Invalid argument for limit " + size + " or offset " + page);
    }

}
