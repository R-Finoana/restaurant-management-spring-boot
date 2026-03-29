package spring.restaurantmanagement.exception;

public class IngredientNotFoundException extends Exception {
    public IngredientNotFoundException(int id){
        super("Ingredient.id= "+id+" is not found");
    }
}
