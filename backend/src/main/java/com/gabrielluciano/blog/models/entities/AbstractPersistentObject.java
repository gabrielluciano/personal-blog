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
    private String uuid = UUIDGenerator.createUUID();

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPersistentObject that = (AbstractPersistentObject) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "AbstractPersistentObject{" +
                "UUID='" + uuid + '\'' +
                '}';
    }
}
