package app.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dtos.CookieContentDto;
import app.dtos.ProductDto;
import app.services.ProductService;
import app.utils.HttpWrapper;
import core.utils.ResponseWrapper;
import core.dtos.WrappedResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import io.jsonwebtoken.Claims;

/**
 * Controller for products management 
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Extract decoded cookie content from HTTP request 
     * @param request
     * @return decoded cookie content (userId, companyId)
     */
    private CookieContentDto getCookieContent(HttpServletRequest request) { 
        Claims claims = (Claims) request.getAttribute("cookieContent");
        CookieContentDto cookieContent = new CookieContentDto(
            claims.get("userId", String.class),
            claims.get("companyId", String.class)
        );
        return cookieContent;
    }

    /**
     * Add a new product to db
     * @param product 
     * @param request
     * @return HTTP response
     */
    @PostMapping("/add-product")
    HttpEntity<WrappedResponseDto<?>> addProduct(@Valid @RequestBody ProductDto product, HttpServletRequest request) {
        UUID companyId = UUID.fromString((this.getCookieContent(request)).companyId());

        this.productService.addProduct(product, companyId);
        return HttpWrapper.fromWrToRes(ResponseWrapper.OK("You successfully added " + product.name() + " to db"));
    }

    /**
     * Update product 
     * @param productId
     * @param product
     * @param request
     * @return HTTP response
     */
    @PutMapping("/update-product/{productId}")
    HttpEntity<WrappedResponseDto<?>> updateProduct(@Valid @PathVariable UUID productId, @Valid @RequestBody ProductDto product, HttpServletRequest request) {
        this.productService.updateProduct(productId, product);
        return HttpWrapper.fromWrToRes(ResponseWrapper.OK("You successfully updated " + product.name()));
    }

    /**
     * Get products list with companyId
     * @param request
     * @return HTTP response wrapping products list
     */
    @GetMapping("/get-products")
    HttpEntity<WrappedResponseDto<?>> getProducts(HttpServletRequest request) {
        UUID companyId = UUID.fromString((this.getCookieContent(request)).companyId());
        
        List<ProductDto> products = this.productService.getProducts(companyId);
        return HttpWrapper.fromWrToRes(ResponseWrapper.OK("You successfully fetched products", products));
    }

}
