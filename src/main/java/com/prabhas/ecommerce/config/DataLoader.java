package com.prabhas.ecommerce.config;

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
    public void run(String... args) throws Exception {

        // ---------- CREATE ROLES ----------
        Roles admin = roleRepo.findByName("ROLE_ADMIN").orElseGet(() ->
                roleRepo.save(new Roles( "ROLE_ADMIN"))
        );

        Roles seller = roleRepo.findByName("ROLE_SELLER").orElseGet(() ->
                roleRepo.save(new Roles( "ROLE_SELLER"))
        );

        Roles consumer = roleRepo.findByName("ROLE_CONSUMER").orElseGet(() ->
                roleRepo.save(new Roles( "ROLE_CONSUMER"))
        );

        // ---------- CREATE USERS ----------
        if (userRepo.findByEmail("admin@gmail.com").isEmpty()) {
            Users adminUser = new Users();
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRoles(Set.of(admin));
            adminUser.setEnabled(true);
            userRepo.save(adminUser);
        }

        if (userRepo.findByEmail("seller@gmail.com").isEmpty()) {
            Users sellerUser = new Users();
            sellerUser.setUsername("seller");
            sellerUser.setEmail("seller@gmail.com");
            sellerUser.setPassword(passwordEncoder.encode("seller123"));
            sellerUser.setRoles(Set.of(seller));
            sellerUser.setEnabled(true);
            userRepo.save(sellerUser);
        }

        if (userRepo.findByEmail("consumer@gmail.com").isEmpty()) {
            Users consumerUser = new Users();
            consumerUser.setUsername("consumer");
            consumerUser.setEmail("consumer@gmail.com");
            consumerUser.setPassword(passwordEncoder.encode("consumer123"));
            consumerUser.setRoles(Set.of(consumer));
            consumerUser.setEnabled(true);
            userRepo.save(consumerUser);
        }

        // ---------- CREATE CATEGORY ----------
        Category electronics = categoryRepo.findByName("Electronics").orElseGet(() -> {
            Category c = new Category();
            c.setName("Electronics");
            c.setDescription("Electronic gadgets and accessories");
            return categoryRepo.save(c);
        });

        // ---------- CREATE SAMPLE PRODUCT ----------
        if (productRepo.findByName("Smartphone").isEmpty()) {
            Product p = new Product();
            p.setName("Smartphone");
            p.setDescription("A sample smartphone product");
            p.setPrice(19999.00);
            p.setStock(50);
            p.setImageUrl("image-url-here");
            p.setActive(true);
            p.setCategory(electronics);
            productRepo.save(p);
        }

        System.out.println("✔ Initial Data Loaded Successfully");
    }
}
