package com.feeder.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
@Entity
public class Subscription {
    @Id
    private Long id;
    
    private String title;

    private String iconUrl;

    private String url;

    private Long time;

    private String siteUrl;

    private String desc;

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSiteUrl() {
        return this.siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 2026528081)
    public Subscription(Long id, String title, String iconUrl, String url,
            Long time, String siteUrl, String desc) {
        this.id = id;
        this.title = title;
        this.iconUrl = iconUrl;
        this.url = url;
        this.time = time;
        this.siteUrl = siteUrl;
        this.desc = desc;
    }

    @Generated(hash = 1800298428)
    public Subscription() {
    }
}
