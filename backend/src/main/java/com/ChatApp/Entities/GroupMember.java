package com.ChatApp.Entities;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "group_members")
public class GroupMember {

    @EmbeddedId
    private GroupMemberId id = new GroupMemberId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("groupId")
    @JoinColumn(name = "group_id", nullable = false)
    private ChatGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Role role = Role.member;

    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private Instant joined_at = Instant.now();

    @Column(columnDefinition = "TIMESTAMP")
    private Instant last_read_at;

    @Column(nullable = false)
    private boolean is_active = true;

    public enum Role {
        owner,
        admin,
        member
    }

    // Getters and setters

    public GroupMemberId getId() {
        return id;
    }

    public void setId(GroupMemberId id) {
        this.id = id;
    }

    public ChatGroup getGroup() {
        return group;
    }

    public void setGroup(ChatGroup group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getJoined_at() {
        return joined_at;
    }

    public void setJoined_at(Instant joined_at) {
        this.joined_at = joined_at;
    }

    public Instant getLast_read_at() {
        return last_read_at;
    }

    public void setLast_read_at(Instant last_read_at) {
        this.last_read_at = last_read_at;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}
