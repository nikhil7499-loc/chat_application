
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.Collate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "message_types")
public class MessageTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, length = 36)
    private Short id;

    @Column(nullable = false, updatable = true, length = 256)
    private String name;

    @Column(length = 256)
    private String description;

    

    @Column(nullable = false, columnDefinition = "TIMESTAMP", name = "created_at")
    private Instant created_at;

}
