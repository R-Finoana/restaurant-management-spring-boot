package spring.restaurantmanagement.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.restaurantmanagement.entity.CategoryEnum;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.entity.StockValue;
import spring.restaurantmanagement.entity.UnitTypeEnum;
import spring.restaurantmanagement.exception.IngredientNotFoundException;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class IngredientRepository {
    DataSource dataSource;

    public List<Ingredient> findIngredient() {
        String sql= """
                select id,
                       name,
                       category,
                       price
                from ingredient;
                """;

        List<Ingredient> result = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                ing.setPrice(rs.getDouble("price"));
                result.add(ing);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Ingredient findIngredientById(int id) throws IngredientNotFoundException {
        String sql= """
                select id,
                       name,
                       category,
                       price
                from ingredient
                where id = ?;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    ing.setPrice(rs.getDouble("price"));
                    return ing;
                } else {
                    throw new IngredientNotFoundException(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public StockValue getStockValueAt(int id, Instant t, UnitTypeEnum unit) throws IngredientNotFoundException {
        String sql= """
                select
                    stock_movement.id_ingredient,
                    sum(CASE WHEN stock_movement.type='OUT' THEN (stock_movement.quantity*(-1)) ELSE stock_movement.quantity END) as actual_quantity,
                    stock_movement.unit
                from stock_movement join ingredient on stock_movement.id_ingredient=ingredient.id
                where stock_movement.creation_datetime<= ?
                  and stock_movement.id_ingredient= ?
                group by stock_movement.id_ingredient, stock_movement.unit;
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.from(t));
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    StockValue stock = new StockValue();
                    stock.setQuantity(rs.getDouble("actual_quantity"));
                    stock.setUnit(UnitTypeEnum.valueOf(rs.getString("unit")));
                    return stock;
                } else {
                    throw new IngredientNotFoundException(id);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
