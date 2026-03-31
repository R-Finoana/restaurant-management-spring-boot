package spring.restaurantmanagement.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.restaurantmanagement.entity.CreateDish;
import spring.restaurantmanagement.entity.Dish;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.exception.DIshNotFoundException;
import spring.restaurantmanagement.exception.DishAlreadyExistsException;
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
        if (ingredients == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Request body is required.");
        }
        List<Ingredient> list = service.updateDishIngredients(id, ingredients);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/dishes")
    public ResponseEntity<?> createDishesList(@RequestBody List<CreateDish> dishes){
        try {
            List<Dish> created = service.createDishes(dishes);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (DishAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
