package com.feeder.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * @description:
 * @author: Match
 * @date: 7/22/16
 */
@Entity
public class Subscription {
    @Id
    private Long id;

    @Index(unique = true)
    private String key;
    
    private String title;

    private String iconUrl;

    private String url;

    private Long time;

    private String siteUrl;

    private String desc;

    private String category;

    private String sortid;

    private Long totalCount;

    private Long unreadCount;

    private Long accountId;

    private String ext1;

    private String ext2;

    private String ext3;

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

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSortid() {
        return this.sortid;
    }

    public void setSortid(String sortid) {
        this.sortid = sortid;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getUnreadCount() {
        return this.unreadCount;
    }

    public void setUnreadCount(Long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getExt1() {
        return this.ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return this.ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return this.ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    @Generated(hash = 1522689432)
    public Subscription(Long id, String key, String title, String iconUrl, String url,
            Long time, String siteUrl, String desc, String category, String sortid,
            Long totalCount, Long unreadCount, Long accountId, String ext1, String ext2,
            String ext3) {
        this.id = id;
        this.key = key;
        this.title = title;
        this.iconUrl = iconUrl;
        this.url = url;
        this.time = time;
        this.siteUrl = siteUrl;
        this.desc = desc;
        this.category = category;
        this.sortid = sortid;
        this.totalCount = totalCount;
        this.unreadCount = unreadCount;
        this.accountId = accountId;
        this.ext1 = ext1;
        this.ext2 = ext2;
        this.ext3 = ext3;
    }

    @Generated(hash = 1800298428)
    public Subscription() {
    }
}
