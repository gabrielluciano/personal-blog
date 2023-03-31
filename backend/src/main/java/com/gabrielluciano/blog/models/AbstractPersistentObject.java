package com.gabrielluciano.blog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabrielluciano.blog.util.UUIDGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@ToString
public abstract class AbstractPersistentObject {

    @Column(length = 32, updatable = false)
    @JsonIgnore
    private String uuid = UUIDGenerator.createUUID();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPersistentObject that = (AbstractPersistentObject) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
