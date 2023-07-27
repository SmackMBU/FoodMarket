package ru.smackmbu.foodmarket.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.smackmbu.foodmarket.models.Product;
import ru.smackmbu.foodmarket.models.User;
import ru.smackmbu.foodmarket.repositories.ProductRepository;
import ru.smackmbu.foodmarket.repositories.UserRepository;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/addproduct")
    public String addProductPage(){
        return "addproduct";
    }

    @GetMapping("/balance")
    public String addBalancePage() { return "addbalance"; }

    @GetMapping("/menu")
    public String adminMenuPage() { return "adminmenu"; }

    @PostMapping("/addbalance")
    public String addBalance(@Qualifier("id") Long id,
                             @Qualifier("balance") Double balance){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){ return "redirect:/admin/balance?error"; }
        User user = optionalUser.get();
        user.setBalance(user.getBalance() + balance);
        userRepository.save(user);
        return "redirect:/admin/balance?success";
    }

    @PostMapping("/add")
    public String addProduct(@Qualifier("name") String name,
                             @Qualifier("cost") double cost,
                             @Qualifier("count") int count){
        createProduct(name, cost, count);
        return "redirect:/admin/addproduct?success";
    }

    public Product createProduct(String name, double cost, int count){
        Product product = new Product();
        product.setCost(cost);
        product.setName(name);
        product.setCount(count);
        productRepository.save(product);
        return product;
    }
}
