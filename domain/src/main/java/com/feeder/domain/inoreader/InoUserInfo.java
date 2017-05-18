package com.feeder.domain.inoreader;

/**
 * @description:
 * @author: Match
 * @date: 14/05/2017
 */

public class InoUserInfo {
    public String userId;
    public String userName;
    public String userProfileId;
    public String userEmail;
    public String isBloggerUser;
    public String signupTimeSec;
    public String isMultiLoginEnabled;

    @Override
    public String toString() {
        return "InoUserInfo{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userProfileId='" + userProfileId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", isBloggerUser='" + isBloggerUser + '\'' +
                ", signupTimeSec='" + signupTimeSec + '\'' +
                ", isMultiLoginEnabled='" + isMultiLoginEnabled + '\'' +
                '}';
    }
}
