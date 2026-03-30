package spring.restaurantmanagement.exception;

public class DIshNotFoundException extends RuntimeException {
    public DIshNotFoundException(int id) {
        super("Dish.id= "+id+" is not found");
    }
}
