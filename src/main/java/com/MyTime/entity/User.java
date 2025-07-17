package com.MyTime.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;


@ToString
@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", unique = true, nullable = false)
    private Integer userId;
    @NotNull
    private String name;
    @NotNull
    @Column(unique = true)
    private String userName;
    @NotNull
    private String email;
    @NotNull
    private String password;

    @OneToOne(cascade = CascadeType.ALL)

    @JoinColumn(name = "company_id", referencedColumnName = "company_id")
    private Company company;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_rol", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();
    private String status;

    @Column(name = "TOKEN", length = 36)
    private String token;

    private Date expirationToken;


    public User() {
    }

    public User(String name, String userName) {
        this.name = name;
        this.userName = userName;
    }

    public User(String name, String userName, Company company) {
        this.name = name;
        this.userName = userName;
        this.company = company;
    }

    public User(String name, String userName, String email, String password, String status) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.status = status;
    }
}