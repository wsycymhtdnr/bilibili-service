package com.bugmaker.bilibili.domain;

import java.util.Date;
import java.util.List;

/**
 * @author ：liyunfei
 * @date ：2022/3/15 23:12
 * @description： 关注列表的信息
 */

public class FollowingGroup {
    private Long id;
    private Long userId;
    private String name;
    // 0:特别关注 1:悄悄关注 2:默认分组 3:用户自定义
    private String type;
    private Date createTime;
    private Date updateTime;
    // 关注分组中所有用户的信息
    private List<UserInfo> followingUserInfoList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<UserInfo> getFollowingUserInfoList() {
        return followingUserInfoList;
    }

    public void setFollowingUserInfoList(List<UserInfo> followingUserInfoList) {
        this.followingUserInfoList = followingUserInfoList;
    }
}
