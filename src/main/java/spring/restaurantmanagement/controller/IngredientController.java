package spring.restaurantmanagement.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.entity.UnitTypeEnum;
import spring.restaurantmanagement.exception.IngredientNotFoundException;
import spring.restaurantmanagement.service.IngredientService;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@RestController
@AllArgsConstructor
public class IngredientController {
    private final IngredientService service;

    @GetMapping("/ingredients")
    public List<Ingredient> getIngredients() throws SQLException {
        return service.getAllIngredients();
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable int id) throws IngredientNotFoundException {
        return ResponseEntity.ok(service.getIngredientById(id));

    }

    @GetMapping("/ingredients/{id}/stock")
    public ResponseEntity<?> getStockValueAt(@PathVariable int id, @RequestParam (name = "at")Instant at, @RequestParam UnitTypeEnum unit) throws IngredientNotFoundException {
        return ResponseEntity.ok(service.getStockValueAt(id, at, unit));
    }
}
