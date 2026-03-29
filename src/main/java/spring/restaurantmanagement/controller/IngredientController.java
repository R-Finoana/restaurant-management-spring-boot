package spring.restaurantmanagement.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.service.IngredientService;

import java.sql.SQLException;
import java.util.List;

@RestController
@AllArgsConstructor
public class IngredientController {
    private final IngredientService service;

    @GetMapping("/ingredients")
    public List<Ingredient> getIngredients() throws SQLException {
        return service.getAllIngredients();
    }
}
