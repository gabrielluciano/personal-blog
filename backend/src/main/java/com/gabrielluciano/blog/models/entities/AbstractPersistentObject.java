package com.gabrielluciano.blog.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabrielluciano.blog.models.PersistentObject;
import com.gabrielluciano.blog.util.UUIDGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.Objects;

@MappedSuperclass
public abstract class AbstractPersistentObject implements PersistentObject {

    @Column(length = 32, updatable = false)
    @JsonIgnore
    private String UUID = UUIDGenerator.createUUID();

    @Override
    public String getUUID() {
        return UUID;
    }

    @Override
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPersistentObject that = (AbstractPersistentObject) o;
        return UUID.equals(that.UUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UUID);
    }

    @Override
    public String toString() {
        return "AbstractPersistentObject{" +
                "UUID='" + UUID + '\'' +
                '}';
    }
}
