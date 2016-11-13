package com.feeder.model;

import com.feeder.common.StringUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @description:
 * @author: Match
 * @date: 10/17/16
 */

@Entity
public class Article {
    @Id
    private Long id;

    private String title;

    private String link;

    private String description;

    private Boolean read;

    private Boolean trash;

    private String content;

    private Long subscriptionId;

    private Long published;

    public Long getPublished() {
        return this.published;
    }

    public void setPublished(Long published) {
        this.published = published;
    }

    public Long getSubscriptionId() {
        return this.subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getTrash() {
        return this.trash;
    }

    public void setTrash(Boolean trash) {
        this.trash = trash;
    }

    public Boolean getRead() {
        return this.read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
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

    @Generated(hash = 1082740550)
    public Article(Long id, String title, String link, String description,
            Boolean read, Boolean trash, String content, Long subscriptionId,
            Long published) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.read = read;
        this.trash = trash;
        this.content = content;
        this.subscriptionId = subscriptionId;
        this.published = published;
    }

    @Generated(hash = 742516792)
    public Article() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o.getClass() == Article.class) {
            Article article = (Article) o;
            return StringUtil.equals(article.getTitle(), getTitle());
        }
        return false;
    }
}
