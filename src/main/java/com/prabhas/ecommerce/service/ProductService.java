package com.prabhas.ecommerce.service;

import com.prabhas.ecommerce.beans.ProductCreateRequest;
import com.prabhas.ecommerce.beans.ProductUpdateRequest;
import com.prabhas.ecommerce.models.Category;
import com.prabhas.ecommerce.models.Product;
import com.prabhas.ecommerce.models.Users;
import com.prabhas.ecommerce.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CategoryRepository categoryRepository;

    /**
     * This method retrieves products for a specific seller with optional pagination
     *
     * @param username The username of the seller whose products are to be retrieved
     * @param pageNo   The page number for pagination (optional)
     * @param size     The number of items per page for pagination (optional)
     * @return ResponseEntity containing either paginated product data or all products if no pagination parameters provided
     */
    public ResponseEntity<?> getsellerProducts(String username, Integer pageNo, Integer size) {
        // Check if pagination parameters are provided
        if (pageNo != null && size != null) {
            // Retrieve paginated results from the repository
            Page<Product> page = productRepository.findBySeller_username(username, PageRequest.of(pageNo, size));
            // Return paginated response with metadata
            return ResponseEntity.ok(Map.of(
                    "data", page.getContent(),                // List of products in the current page
                    "currentPage", page.getNumber(),          // Current page number (0-based)
                    "totalPages", page.getTotalPages(),      // Total number of pages available
                    "totalItems", page.getTotalElements()    // Total number of products available
            ));
        }
        // If no pagination parameters, retrieve all products for the seller
        List<Product> sellerProducts = productRepository.findBySeller_username(username);
        // Return all products without pagination metadata
        return ResponseEntity.ok(sellerProducts);
    }

    /**
     * Adds a new product to the system with the provided details
     *
     * @param request  The ProductCreateRequest containing product details
     * @param username The username of the seller adding the product
     * @return ResponseEntity with success message if product is added successfully
     * @throws RuntimeException if seller or category is not found
     */
    public ResponseEntity<?> addProduct(ProductCreateRequest request, String username) {

        // Find the seller by username, throw exception if not found
        Users seller = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // Find the category by ID, throw exception if not found
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Create a new product instance
        Product product = new Product();

        // Set basic product information
        product.setName(request.getName());
        product.setDescription(request.getDescription());

        // Set pricing and stock information
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        // Set product image URL
        product.setImageUrl(request.getImageUrl());

        // Set seller and category associations
        product.setSeller(seller);
        product.setCategory(category);

        // Set product as active
        product.setActive(true);

        // Save the product to the database
        productRepository.save(product);

        // Return success response
        return ResponseEntity.ok("Product added successfully");
    }

    /**
     * Updates an existing product's details after performing several validation checks.
     *
     * @param id       The ID of the product to be updated
     * @param request  The ProductUpdateRequest containing the new product details
     * @param username The username of the user attempting to update the product
     * @return ResponseEntity with appropriate status and message based on the operation result
     */
    public ResponseEntity<?> updateProduct(Long id, ProductUpdateRequest request, String username) {

        // Retrieve product from repository by ID, return null if not found
        Product product = productRepository.findById(id).orElse(null);

        // 1. Check product existence
        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        // 2. Authorization check - verify the user is the seller of the product
        if (!product.getSeller().getUsername().equals(username)) {
            return ResponseEntity.status(403).body("Not authorized");
        }

        // 3. Business rule: inactive product cannot be updated
        if (!product.isActive()) {
            return ResponseEntity.badRequest().body("Cannot update inactive product");
        }

        // 4. Validations + updates (safe null handling)

        // Update product name only if provided and not blank
        if (request.getName() != null && !request.getName().isBlank()) {
            product.setName(request.getName());
        }

        // Update product description if provided
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }

        // Update product price only if provided and positive
        if (request.getPrice() != null && request.getPrice() > 0) {
            product.setPrice(request.getPrice());
        }

        // Update product stock only if provided and non-negative
        if (request.getStock() != null && request.getStock() >= 0) {
            product.setStock(request.getStock());
        }

        // Update product image URL only if provided and not blank
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            product.setImageUrl(request.getImageUrl());
        }

        // 5. Save updated entity to the database
        productRepository.save(product);

        // Return success response with 200 OK status
        return ResponseEntity.ok("Product updated successfully");
    }

    /**
     * Deletes a product by deactivating it and setting its stock to zero
     *
     * @param id       The ID of the product to be deleted
     * @param username The username of the user attempting to delete the product
     * @return ResponseEntity with appropriate status and message
     */
    public ResponseEntity<?> deleteProduct(Long id, String username) {

        // Find the product by ID, return null if not found
        Product product = productRepository.findById(id).orElse(null);

        // If product doesn't exist, return bad request response
        if (product == null) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        // Check if the user trying to delete is the seller of the product
        if (!product.getSeller().getUsername().equals(username)) {
            return ResponseEntity.status(403).body("Not authorized");
        }

        // Check if the product is already deactivated
        if (!product.isActive()) {
            return ResponseEntity.badRequest().body("Product already deleted");
        }

        // OPTIONAL (recommended in real systems)
        // Check if the product has been ordered before deletion
        // In production systems, you might want to prevent deletion of ordered products
//        boolean hasOrders = orderItemRepository.existsByProduct(product);
//        if (hasOrders) {
//            return ResponseEntity.badRequest()
//                    .body("Cannot delete product already ordered");
//        }

        // Deactivate the product and set its stock to zero instead of hard deleting
        product.setActive(false);
        product.setStock(0);

        // Save the updated product to the database
        productRepository.save(product);

        // Return success response
        return ResponseEntity.ok("Product deactivated successfully");
    }
}
