package ru.smackmbu.foodmarket.controllers;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.smackmbu.foodmarket.models.Order;
import ru.smackmbu.foodmarket.models.Product;
import ru.smackmbu.foodmarket.models.User;
import ru.smackmbu.foodmarket.repositories.OrderRepository;
import ru.smackmbu.foodmarket.repositories.ProductRepository;
import ru.smackmbu.foodmarket.repositories.UserRepository;

import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/")
    public String mainPage(Model model){
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("user", getCurrentUser());
        return "main";
    }

    @GetMapping("/cart")
    public String cartPage(Model model){
        model.addAttribute("products", getCurrentUser().getProducts());
        model.addAttribute("balance", getCurrentUser().getBalance());
        return "cart";
    }

    @GetMapping("/makeorder")
    public String makeOrderPage(Model model){
        User user = getCurrentUser();
        model.addAttribute("products", user.getProducts());
        model.addAttribute("balance", user.getBalance());
        double totalCost = countTotalCost(user.getProducts());
        model.addAttribute("totalCost", totalCost);
        return "makeorder";
    }

    @GetMapping("/orders")
    public String ordersPage(Model model){
        User user = getCurrentUser();
        model.addAttribute("orders", user.getOrders());
        return "orders";
    }

    @PostMapping("/pay")
    public String pay(@Qualifier("totalCost") double totalCost){
        User user = getCurrentUser();
        if(user.getBalance() < totalCost){ return "redirect:/cart?error"; }
        for(Map.Entry<Product, Integer> entry : user.getProducts().entrySet()){
            if(entry.getValue() > entry.getKey().getCount()){ return "redirect:/cart?error"; }
        }
        user.setBalance(user.getBalance() - totalCost);
        Order order = new Order();
        for(Map.Entry<Product, Integer> entry : user.getProducts().entrySet()){
            order.getProductList().put(entry.getKey(), entry.getValue());
            Product product = productRepository.findById(entry.getKey().getId()).get();
            product.setCount(product.getCount() - entry.getValue());
            productRepository.save(product);
        }
        order.setTotalCost(totalCost);
        user.getProducts().clear();
        order.setUser(user);
        user.getOrders().add(order);
        orderRepository.save(order);
        userRepository.save(user);
        return "redirect:/cart";
    }

    @PostMapping("/buy")
    public String buy(@Qualifier("id") Long id,
                      @Qualifier("count") Integer count){
        if (addProductToCart(id, count)) {
            return "redirect:/";
        } else {
            return "redirect:/?error";
        }
    }

    @PostMapping("/addmore")
    public String addMore(@Qualifier("id") Long id,
                          @Qualifier("count") Integer count) {
        if (addProductToCart(id, count)) {
            return "redirect:/cart";
        } else {
            return "redirect:/cart?error";
        }
    }

    public double countTotalCost(Map<Product, Integer> products){
        double totalCost = 0.0;
        for(Map.Entry<Product, Integer> entry : products.entrySet()){
            totalCost = totalCost + (entry.getKey().getCost() * entry.getValue());
        }
        return totalCost;
    }

    public boolean addProductToCart(Long id, Integer count){
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product;
        if(optionalProduct.isPresent()) {
            product = optionalProduct.get();
        }else { return false; }
        User user = getCurrentUser();
        if(count == 0){
            user.getProducts().remove(product);
            userRepository.save(user);
            return true;
        }
        user.getProducts().put(product, count);
        userRepository.save(user);
        return true;
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(auth.getName());
        return user.orElse(null);
    }
}
