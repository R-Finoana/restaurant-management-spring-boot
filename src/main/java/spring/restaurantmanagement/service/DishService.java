package spring.restaurantmanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.restaurantmanagement.entity.CreateDish;
import spring.restaurantmanagement.entity.Dish;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.exception.DIshNotFoundException;
import spring.restaurantmanagement.exception.DishAlreadyExistsException;
import spring.restaurantmanagement.repository.DishRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class DishService {
    DishRepository repository;

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

    public List<Dish> createDishes(List<CreateDish> dishes) {
        for (CreateDish d : dishes) {
            if (repository.dishNameExists(d.getName())) {
                throw new DishAlreadyExistsException(d.getName());
            }
        }
        return repository.saveDishes(dishes);
    }

    public List<Dish> getFilteredDishes(Double priceOver, Double priceUnder, String name) {
        return repository.findFilteredDishes(priceOver, priceUnder, name);
    }
}
