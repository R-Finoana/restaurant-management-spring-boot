package spring.restaurantmanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.repository.IngredientRepository;

import java.sql.SQLException;
import java.util.List;

@Service
@AllArgsConstructor
public class IngredientService {
    private final IngredientRepository repository;

    public List<Ingredient> getAllIngredients() throws SQLException {
        return repository.findIngredient();
    }
}
