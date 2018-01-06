package com.reg;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Ciprian on 1/6/2018.
 */
@Entity
@Table(name = "user")
public class User2 {
    private Integer id;
    private String username;
    private String password;

    public User2() {
    }

    public User2(Integer id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public User2(String password, String username) {
        this.password = password;
        this.username = username;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id",nullable = false, unique = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
