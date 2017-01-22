package com.feeder.model;

import com.feeder.common.NumberUtil;
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

    private Boolean favorite;

    private String ext1;

    private String ext2;

    private String ext3;

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

    @Generated(hash = 19592054)
    public Article(Long id, String title, String link, String description, Boolean read,
            Boolean trash, String content, Long subscriptionId, Long published,
            Boolean favorite, String ext1, String ext2, String ext3) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.read = read;
        this.trash = trash;
        this.content = content;
        this.subscriptionId = subscriptionId;
        this.published = published;
        this.favorite = favorite;
        this.ext1 = ext1;
        this.ext2 = ext2;
        this.ext3 = ext3;
    }

    @Generated(hash = 742516792)
    public Article() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Article) {
            Article article = (Article) o;
            return StringUtil.equals(article.getTitle(), getTitle())
                    && NumberUtil.equals(article.getSubscriptionId(), subscriptionId);
        }
        return false;
    }

    public Boolean getFavorite() {
        return this.favorite == null ? false : this.favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
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
}
