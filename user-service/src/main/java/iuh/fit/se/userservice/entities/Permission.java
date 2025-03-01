package iuh.fit.se.userservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "permissions")
public class Permission
        extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
}
