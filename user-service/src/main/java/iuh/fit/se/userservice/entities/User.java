package iuh.fit.se.userservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_name"),
        @UniqueConstraint(columnNames = "email")
})
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;
    private String email;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}
