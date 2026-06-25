package app.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table(name = "products")
@Data
@NoArgsConstructor // Needed for Hibernate to properly instanciate objects
public class Product {
    @Id
    private UUID id;

    private String name;

    @Column(name = "company_id")
    private UUID companyId;
}
