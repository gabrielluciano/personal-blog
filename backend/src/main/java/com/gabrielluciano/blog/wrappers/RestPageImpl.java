package com.gabrielluciano.blog.wrappers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Getter
public class RestPageImpl<T> extends PageImpl<T> {

    private int numberOfElements;
    private int totalPages;
    private boolean last;
    private boolean first;
    private boolean empty;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPageImpl(@JsonProperty("content") List<T> content,
                        @JsonProperty("pageable") JsonNode pageable,
                        @JsonProperty("sort") JsonNode sort,
                        @JsonProperty("totalElements") long totalElements,
                        @JsonProperty("number") int number,
                        @JsonProperty("size") int size,
                        @JsonProperty("numberOfElements") int numberOfElements,
                        @JsonProperty("totalPages") int totalPages,
                        @JsonProperty("last") boolean last,
                        @JsonProperty("first") boolean first,
                        @JsonProperty("empty") boolean empty) {
        super(content, PageRequest.of(number, size), totalElements);

        this.numberOfElements = numberOfElements;
        this.totalPages = totalPages;
        this.last = last;
        this.first = first;
        this.empty = empty;
    }
}
