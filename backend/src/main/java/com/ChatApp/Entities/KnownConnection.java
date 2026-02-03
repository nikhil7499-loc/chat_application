
import com.ChatApp.Entities.User;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name="known_connection",
    uniqueConstraints={
        @UniqueConstraint(columnNames={
            "user_id","contact_id"
        })
    }
)
public class KnownConnection {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(nullable=false,updatable=false)
    private String id; 

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @ManyToOne(fetch=FetchType.LAZY,optional=false)
    @JoinColumn(name="contact_id",nullable=false)
    private User contact;

    @Column(name="last_message_at",nullable=false)
    private Instant lastMessageAt;

    @Column(name="is_blocked",nullable=false)
    private Boolean  isBlocked=false;

    @Column(name="blocked_by_user_id")
    private String blockedByuserId;

    @Column(name="blocked_by_user_id")
    private Instant blockedByUser;

    @Column(name="is_faavorite",nullable=false)
    private Boolean isFavorite=false;
    
    @Column(name="unread_count",nullable=false)
    private Integer unreadCount=0;

    public KnownConnection(){};

    public KnownConnection(User user, User contact) {
        this.user = user;
        this.contact = contact;
        this.lastMessageAt = Instant.now();
    }

    @PrePersist
    public void prePersist(){

        if(lastMessageAt==null){
            lastMessageAt=Instant.now();
        }

       if (isFavorite == null) {
            isFavorite = false;
        }
        if (unreadCount == null) {
            unreadCount = 0;
        }
        if (isBlocked == null) {
            isBlocked = false;
        }
    }
}
