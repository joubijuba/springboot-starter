package app.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import app.dtos.ProductDto;
import app.entities.Product;
import app.exceptions.ConflictException;
import app.exceptions.NotFoundException;
import app.repositories.ProductRepository;

/**
 * Logic for products management in db (CRUD)
 */
@Service
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Insert a new product in db
     * @param product 
     * @param companyId
     */
    public void addProduct(ProductDto product, UUID companyId) {
        // check if there isn't a product with the same name first
        Optional<Product> _product = this.productRepository.findByName(product.name());
        if (_product.isPresent()){
            throw new ConflictException("There is already a product using this name");
        }

        UUID productId = UUID.randomUUID(); // TO DO : use @GeneratedValue in Product entity class

        Product dbProduct = new Product();
        dbProduct.setId(productId);
        dbProduct.setName(product.name());
        dbProduct.setCompanyId(companyId);

        this.productRepository.save(dbProduct);
    }

    public void updateProduct(UUID productId, ProductDto product) {
        // check first if product exists
        Optional<Product> dbProduct = this.productRepository.findById(productId);
        if (dbProduct.isEmpty()){
            throw new NotFoundException("This product doesn't exist");
        }

        // check if there is already a product with the new name
        Optional<Product> _product = this.productRepository.findByName(product.name());
        if (_product.isPresent()){
            throw new ConflictException("There is already a product with " + "'" + product.name() + "'" + " as a name");
        }

        // update the product
        Product newDpProduct = dbProduct.get();
        newDpProduct.setName(product.name());
        this.productRepository.save(newDpProduct);
    }

	public List<ProductDto> getProducts(UUID companyId) {
        List<Product> products = this.productRepository.findByCompanyId(companyId);

        if (products.isEmpty()){
            throw new NotFoundException("No products yet, start by adding one please");
        }

        List<ProductDto> productsArr =  products.stream()
            .map(item -> new ProductDto(item.getId(), item.getName()))
            .collect(Collectors.toList());

        return productsArr;
	}
}
