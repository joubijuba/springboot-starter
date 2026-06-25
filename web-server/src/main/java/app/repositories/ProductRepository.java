package app.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entities.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByName(String name);

    List<Product> findByCompanyId(UUID companyId);
}
