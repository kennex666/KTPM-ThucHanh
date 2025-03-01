package iuh.fit.se.userservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tokens")
public class Token extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true, length = 10000)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
    public boolean revoked;
}
