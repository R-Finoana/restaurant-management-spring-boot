package spring.restaurantmanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.restaurantmanagement.entity.Dish;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.exception.DIshNotFoundException;
import spring.restaurantmanagement.repository.DishRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
    private final DishRepository repository;

    public List<Dish> getAllDishes(){
        return repository.findDish();
    }

    @Transactional
    public List<Ingredient> updateDishIngredients(int dishId, List<Ingredient> ingredients){
        if(!repository.dishExists(dishId)){
            throw new DIshNotFoundException(dishId);
        }
        repository.detachIngredients(dishId);
        if(!ingredients.isEmpty()){
            repository.attachIngredients(dishId, ingredients);
        }
        return repository.findIngredientsByDishId(dishId);
    }
}
