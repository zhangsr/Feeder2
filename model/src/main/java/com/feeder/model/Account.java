package com.feeder.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @description:
 * @author: Match
 * @date: 7/21/16
 */
@Entity
public class Account {
    @Id
    private Long id;

    private String name;

    private String avatarUrl;

    private String email;

    private Long signUpTime;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getSignUpTime() {
        return this.signUpTime;
    }

    public void setSignUpTime(Long signUpTime) {
        this.signUpTime = signUpTime;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Generated(hash = 852110296)
    public Account(Long id, String name, String avatarUrl, String email,
            Long signUpTime) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.signUpTime = signUpTime;
    }

    @Generated(hash = 882125521)
    public Account() {
    }
}
