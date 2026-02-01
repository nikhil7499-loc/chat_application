// package com.ChatApp.Entities;

// import java.time.Instant;

// import javax.management.relation.Role;

// import jakarta.persistence.Column;
// import jakarta.persistence.EmbeddedId;
// import jakarta.persistence.Entity;
// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.MapsId;
// import jakarta.persistence.Table;

// @Entity
// @Table(name = "group_members")
// public class GroupMember {

//     @EmbeddedId
//     private GroupMemberId id = new GroupMemberId();

//     @ManyToOne(fetch = FetchType.LAZY)
//     @MapsId("groupId")
//     @JoinColumn(name = "group_id", nullable = false)
//     private ChatGroup group;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @MapsId("userId")
//     @JoinColumn(name = "user_id", nullable = false)
//     private User user;

//     @Enumerated(EnumType.STRING)
//     @Column(length = 10, nullable = false)
//     private Role role = Role.member;

//     @Column(nullable = false, columnDefinition = "timestamp")
//     private Instant joined_at = Instant.now();

//     @column(columnDefination = "timestamp")
//     private Instant last_read_at;

//     @column(nullable = false)
//     private boolean is_active = true;

//     public enum Role {
//         owner,
//         admin,
//         member
//     }

//     /**
//      * @return GroupMemberId id= new return the GroupMemberId()
//      */
//     public GroupMemberId id= new getGroupMemberId()()
//     {
//         return GroupMemberId();
//     }

//     /**
//      * @param GroupMemberId() the GroupMemberId() to set
//      */
//     public void setGroupMemberId()(GroupMemberId id= new GroupMemberId()) {
//         this.GroupMemberId() = GroupMemberId();
//     }

//     /**
//      * @return ChatGroup return the group
//      */
//     public ChatGroup getGroup() {
//         return group;
//     }

//     /**
//      * @param group the group to set
//      */
//     public void setGroup(ChatGroup group) {
//         this.group = group;
//     }

//     /**
//      * @return User return the user
//      */
//     public User getUser() {
//         return user;
//     }

//     /**
//      * @param user the user to set
//      */
//     public void setUser(User user) {
//         this.user = user;
//     }

//     /**
//      * @return Role return the role
//      */
//     public Role getRole() {
//         return role;
//     }

//     /**
//      * @param role the role to set
//      */
//     public void setRole(Role role) {
//         this.role = role;
//     }

//     /**
//      * @return Instant return the joined_at
//      */
//     public Instant getJoined_at() {
//         return joined_at;
//     }

//     /**
//      * @param joined_at the joined_at to set
//      */
//     public void setJoined_at(Instant joined_at) {
//         this.joined_at = joined_at;
//     }

//     /**
//      * @return Instant return the last_read_at
//      */
//     public Instant getLast_read_at() {
//         return last_read_at;
//     }

//     /**
//      * @param last_read_at the last_read_at to set
//      */
//     public void setLast_read_at(Instant last_read_at) {
//         this.last_read_at = last_read_at;
//     }

//     /**
//      * @return boolean return the is_active
//      */
//     public boolean isIs_active() {
//         return is_active;
//     }

//     /**
//      * @param is_active the is_active to set
//      */
//     public void setIs_active(boolean is_active) {
//         this.is_active = is_active;
//     }

// }
