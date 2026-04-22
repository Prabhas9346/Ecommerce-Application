package com.prabhas.ecommerce.config;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.prabhas.ecommerce.models.Category;
import com.prabhas.ecommerce.models.Product;
import com.prabhas.ecommerce.models.Roles;
import com.prabhas.ecommerce.models.Users;
import com.prabhas.ecommerce.repositories.CategoryRepository;
import com.prabhas.ecommerce.repositories.ProductRepository;
import com.prabhas.ecommerce.repositories.RolesRepository;
import com.prabhas.ecommerce.repositories.UsersRepository;

@Configuration
public class DataLoader implements CommandLineRunner {

    private final RolesRepository roleRepo;
    private final UsersRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DataLoader(RolesRepository roleRepo,
                      UsersRepository userRepo,
                      CategoryRepository categoryRepo,
                      ProductRepository productRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    @Override
    public void run(String... args) {

        // ---------- ROLES ----------
        Roles adminRole = roleRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepo.save(new Roles("ROLE_ADMIN")));

        Roles sellerRole = roleRepo.findByName("ROLE_SELLER")
                .orElseGet(() -> roleRepo.save(new Roles("ROLE_SELLER")));

        Roles consumerRole = roleRepo.findByName("ROLE_CONSUMER")
                .orElseGet(() -> roleRepo.save(new Roles("ROLE_CONSUMER")));

        // ---------- USERS ----------
        Users adminUser = createUserIfNotExists(
                "admin",
                "admin@gmail.com",
                "admin123",
                Set.of(adminRole)
        );

        Users sellerUser = createUserIfNotExists(
                "seller",
                "seller@gmail.com",
                "seller123",
                Set.of(sellerRole, consumerRole) // seller can also buy
        );

        Users consumerUser = createUserIfNotExists(
                "consumer",
                "consumer@gmail.com",
                "consumer123",
                Set.of(consumerRole)
        );

        // ---------- CATEGORY ----------
        Category electronics = categoryRepo.findByName("Electronics")
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setName("Electronics");
                    c.setDescription("Electronic gadgets and accessories");
                    return categoryRepo.save(c);
                });

        // ---------- PRODUCT ----------
        if (productRepo.findByName("Smartphone").isEmpty()) {

            Product product = new Product();
            product.setName("Smartphone");
            product.setDescription("A sample smartphone product");
            product.setPrice(19999.00);
            product.setStock(50);
            product.setImageUrl("image-url-here");
            product.setActive(true);
            product.setCategory(electronics);

            // ✅ VERY IMPORTANT (seller relationship)
            product.setSeller(sellerUser);

            // optional bidirectional sync
            if (sellerUser.getProducts() == null) {
                sellerUser.setProducts(new ArrayList<>());
            }
            sellerUser.getProducts().add(product);

            productRepo.save(product);
        }

        System.out.println("✔ Initial Data Loaded Successfully");
    }

    // ---------- HELPER METHOD ----------
    private Users createUserIfNotExists(String username,
                                        String email,
                                        String password,
                                        Set<Roles> roles) {

        return userRepo.findByEmail(email).orElseGet(() -> {
            Users user = new Users();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(roles);
            user.setEnabled(true);

            // initialize products list to avoid null issues
            user.setProducts(new ArrayList<>());

            return userRepo.save(user);
        });
    }
}
