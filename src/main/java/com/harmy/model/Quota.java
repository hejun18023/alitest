package com.harmy.model;

/**
 * Description: 指标
 * Created by za-hejun on 2019/3/14
 */
public class Quota {
    /**
     * 指标ID
     */
    private String id;
    /**
     * 指标分组ID
     */
    private String groupId;
    /**
     * 指标值
     */
    private float quota;

    public Quota() {}

    public Quota(String id, String groupId, float quota) {
        this.id = id;
        this.groupId = groupId;
        this.quota = quota;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public float getQuota() {
        return quota;
    }

    public void setQuota(float quota) {
        this.quota = quota;
    }

    @Override
    public String toString(){
        return id + " " + groupId + " " + quota;
    }
}
