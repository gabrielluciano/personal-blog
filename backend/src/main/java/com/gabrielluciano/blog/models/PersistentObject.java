package com.gabrielluciano.blog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface PersistentObject {

    @JsonIgnore
    String getUUID();

    void setUUID(String UUID);

}
