package com.ChatApp.Entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable


public class GroupMemberId implements  Serializable{
    @Column(name="group_id",length=36)
    private String groupId;

    @Column(name="user_id",length=36)
    private String UserId;

    public GroupMemberId(){}

    public GroupMemberId(String groupId,String userId){
        this.groupId=groupId;
        this.UserId=userId;
    }

    

    /**
     * @return String return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return String return the UserId
     */
    public String getUserId() {
        return UserId;
    }

    /**
     * @param UserId the UserId to set
     */
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    @Override
    public boolean equals(Object o){
        if(this==o)return true;
        if(!(o instanceof  GroupMemberId))return false;
        GroupMemberId that=(GroupMemberId)o;
        return Objects.equals(groupId,that.groupId)&& Objects.equals(UserId, that.UserId);
    }

    @Override
    public int hashCode(){
        return Objects.hash(groupId,UserId);
    }

}