package app.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor 
public class User {
    @Id
    private String id; // id <=> email

    private String hash;
    private String salt;

    @Column(name = "company_id") 
    private UUID companyId;
}