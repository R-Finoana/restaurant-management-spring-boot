package spring.restaurantmanagement.validator;

import spring.restaurantmanagement.entity.Ingredient;

import java.util.List;

public class IngredientValidator {
    public void validate(List<Ingredient> ingredients){
        for (Ingredient ing : ingredients){
            if(ing.getName() == null || ing.getName().isBlank()){
                throw  new IllegalArgumentException("Ingredient name can't be null or blank");
            }
        }
    }
}
