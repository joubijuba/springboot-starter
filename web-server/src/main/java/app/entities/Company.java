package app.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "companies")
@Data
public class Company {
    @Id
    private UUID id;

    private String name;
}
