package spring.restaurantmanagement.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.restaurantmanagement.entity.Dish;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.exception.DIshNotFoundException;
import spring.restaurantmanagement.service.DishService;

import java.util.List;

@RestController
@AllArgsConstructor
public class DishController {
    private final DishService service;

    @GetMapping("/dishes")
    public ResponseEntity<List<Dish>> getDishes(){
        return ResponseEntity.ok(service.getAllDishes());
    }

    @PutMapping("/dishes/{id}/ingredients")
    public ResponseEntity<?> updateDishIngredients(@PathVariable int id, @RequestBody List<Ingredient> ingredients) throws DIshNotFoundException {
        service.updateDishIngredients(id, ingredients);
        return ResponseEntity.ok().build();
    }
}
