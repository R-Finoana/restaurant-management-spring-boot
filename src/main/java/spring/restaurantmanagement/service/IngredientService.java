package spring.restaurantmanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.entity.StockValue;
import spring.restaurantmanagement.entity.UnitTypeEnum;
import spring.restaurantmanagement.exception.IngredientNotFoundException;
import spring.restaurantmanagement.repository.IngredientRepository;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class IngredientService {
    private final IngredientRepository repository;

    public List<Ingredient> getAllIngredients() throws SQLException {
        return repository.findIngredient();
    }

    public Ingredient getIngredientById(int id) throws IngredientNotFoundException {
        return repository.findIngredientById(id);
    }

    public StockValue getStockValueAt(int id, Instant t, UnitTypeEnum unit) throws IngredientNotFoundException {
        return repository.getStockValueAt(id, t, unit);
    }
}
