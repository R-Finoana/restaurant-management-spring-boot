package spring.restaurantmanagement.exception;

public class DishAlreadyExistsException extends RuntimeException {
    public DishAlreadyExistsException(String name) {
        super("Dish.name=" + name + " already exists");
    }
}