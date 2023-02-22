package com.gabrielluciano.blog.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@SequenceGenerator(
        name = User.SEQUENCE_NAME,
        sequenceName = User.SEQUENCE_NAME
)
@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractPersistentObject {

    public static final String SEQUENCE_NAME = "SEQUENCE_USER";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    private Boolean admin = false;
    private Boolean writer;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        writer = true;
    }

    @JsonIgnore
    public Boolean isNotWriter() {
        return !getWriter();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", admin=" + admin +
                ", writer=" + writer +
                '}';
    }
}
