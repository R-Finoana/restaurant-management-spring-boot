package spring.restaurantmanagement.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.restaurantmanagement.entity.Dish;
import spring.restaurantmanagement.service.DishService;

import java.util.List;

@RestController
@AllArgsConstructor
public class DishController {
    private final DishService service;

    @GetMapping("/dishes")
    public List<Dish> getDishes(){
        return service.getAllDishes();
    }
}
