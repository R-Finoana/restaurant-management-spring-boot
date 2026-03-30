package spring.restaurantmanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.restaurantmanagement.entity.Dish;
import spring.restaurantmanagement.repository.DishRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
    private final DishRepository repository;

    public List<Dish> getAllDishes(){
        return repository.findDish();
    }
}
