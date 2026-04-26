package com.prabhas.ecommerce.service;

import com.prabhas.ecommerce.beans.CartItemDto;
import com.prabhas.ecommerce.beans.CartResponseDto;
import com.prabhas.ecommerce.models.*;
import com.prabhas.ecommerce.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsumerService {

    @Autowired
    UsersRepository usersRepository;


    @Autowired
    SellerRequestRepository sellerRequestRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Transactional
    public ResponseEntity<?> requestSeller(String username) {

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username cannot be null or empty");
        }

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Roles sellerRole = rolesRepository.findByName("ROLE_SELLER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Already seller
        if (user.getRoles().contains(sellerRole)) {
            return ResponseEntity.badRequest().body("User is already a seller");
        }

        SellerRequest existing = user.getSellerRequest();

        if (existing != null) {

            if (existing.getRequestStatus() == RequestStatus.PENDING) {
                return ResponseEntity.badRequest().body("Seller request already pending");
            }

            if (existing.getRequestStatus() == RequestStatus.REJECTED) {
                existing.setRequestStatus(RequestStatus.PENDING);
                return ResponseEntity.ok("Seller request submitted again");
            }
        }

        // First time request
        SellerRequest newRequest = new SellerRequest();
        newRequest.setConsumer(user);
        newRequest.setRequestStatus(RequestStatus.PENDING);

        user.setSellerRequest(newRequest);

        return ResponseEntity.ok("Seller request accepted");
    }


    @Transactional
    public ResponseEntity<?> addToCart(String username, Long productId, Integer quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        validateProductAvailability(product, quantity);

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Cart cart = user.getCart();
        if (cart == null) {
            cart = createNewCart(user);
        }

        updateCartItem(cart, product, quantity);

        return ResponseEntity.ok("Product added to cart");
    }


    private void validateProductAvailability(Product product, Integer quantity) {
        if (!product.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is not active");
        }

        if (product.getStock() < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product is currently out of stock");
        }

        if (product.getStock() < quantity) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Currently only %d units of this product are available", product.getStock()));
        }
    }

    private Cart createNewCart(Users user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        user.setCart(cart);
        return cart;
    }


    private void updateCartItem(Cart cart, Product product, Integer quantity) {

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseGet(() -> createNewCartItem(cart, product));

        cartItem.setQuantity(cartItem.getQuantity() + quantity);

        // Recalculate total price (correct way)
        double total = cart.getCartItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        cart.setTotalPrice(total);
    }

    private CartItem createNewCartItem(Cart cart, Product product) {
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setQuantity(0);
        cartItem.setCart(cart);

        cart.getCartItems().add(cartItem);
        return cartItem;
    }

    @Transactional
    public ResponseEntity<?> updateCartItem(String username, Long productId, Integer quantity) {

        if (quantity == null || quantity <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be greater than 0");
        }

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = user.getCart();
        if (cart == null) {
            return ResponseEntity.badRequest().body("Cart is empty");
        }

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        Product product = cartItem.getProduct();

        // optional stock validation
        if (product.getStock() < quantity) {
            return ResponseEntity.badRequest()
                    .body("Only " + product.getStock() + " units available");
        }

        cartItem.setQuantity(quantity);

        // ✅ recompute total (important)
        recalculateCartTotal(cart);

        return ResponseEntity.ok("Cart updated successfully");
    }

    @Transactional
    public ResponseEntity<?> removeCartItem(String username, Long productId) {

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = user.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Cart is empty");
        }

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        // ✅ remove from collection
        cart.getCartItems().remove(cartItem);

        // if NO orphanRemoval → you MUST delete manually
        // cartItemRepository.delete(cartItem);

        recalculateCartTotal(cart);

        return ResponseEntity.ok("Item removed from cart");
    }

    private void recalculateCartTotal(Cart cart) {
        double total = cart.getCartItems().stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();

        cart.setTotalPrice(total);
    }


    public ResponseEntity<?> getCart(String username) {

        Users dbUser = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = dbUser.getCart();

        if (cart == null) {
            return new ResponseEntity<>( new Cart(),HttpStatus.NOT_FOUND);// empty cart
        }

        return ResponseEntity.ok(mapToCartDto(cart));

    }


    private CartResponseDto mapToCartDto(Cart cart) {

        CartResponseDto dto = new CartResponseDto();
        dto.setCartId(cart.getId());
        dto.setTotalPrice(cart.getTotalPrice());

        List<CartItemDto> items = cart.getCartItems().stream().map(item -> {
            CartItemDto itemDto = new CartItemDto();
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setImageUrl(item.getProduct().getImageUrl());
            itemDto.setUnitPrice(item.getUnitPrice());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setTotalPrice(item.getUnitPrice() * item.getQuantity());
            return itemDto;
        }).toList();

        dto.setItems(items);

        return dto;
    }
}
