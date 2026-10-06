package com.jonahjayasingh.CRM.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonahjayasingh.CRM.product.dto.ProductRequest;
import com.jonahjayasingh.CRM.product.dto.ProductResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request, username));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest request, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        return ResponseEntity.ok(productService.updateProduct(id, request, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "ANONYMOUS";
        productService.deleteProduct(id, username);
        return ResponseEntity.ok("Product deleted successfully with ID: " + id);
    }
}
