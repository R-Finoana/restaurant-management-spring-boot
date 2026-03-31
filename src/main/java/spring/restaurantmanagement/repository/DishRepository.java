package spring.restaurantmanagement.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import spring.restaurantmanagement.entity.CategoryEnum;
import spring.restaurantmanagement.entity.Dish;
import spring.restaurantmanagement.entity.Ingredient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class DishRepository {
    JdbcTemplate jdbcTemplate;

    public List<Dish> findDish(){
        String dishSql = "SELECT id, name, selling_price FROM dish";
        String ingredientSql = """
            select i.id, i.name, i.category, i.price
            from ingredient i
            join dish_ingredient di on di.id_ingredient = i.id
            where di.id_dish = ?
            """;

        List<Dish> dishes = jdbcTemplate.query(dishSql, (rs, rowNum) -> {
            Dish dish = new Dish();
            dish.setId(rs.getInt("id"));
            dish.setName(rs.getString("name"));
            dish.setSellingPrice(rs.getDouble("selling_price"));
            return dish;
        });

        for (Dish d : dishes) {
            List<Ingredient> ingredients = jdbcTemplate.query(ingredientSql, (rs, rowNum) -> {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                ing.setPrice(rs.getDouble("price"));
                return ing;
            }, d.getId());
            d.setIngredientList(ingredients);
        }

        return dishes;
    }

    public List<Ingredient> findIngredientsByDishId(int dishId) {
        String sql = """
            select i.id, i.name, i.category, i.price
            from ingredient i
            join dish_ingredient di on di.id_ingredient = i.id
            where di.id_dish = ?
            """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ingredient ing = new Ingredient();
            ing.setId(rs.getInt("id"));
            ing.setName(rs.getString("name"));
            ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
            ing.setPrice(rs.getDouble("price"));
            return ing;
        }, dishId);
    }

    public void detachIngredients(Integer dishId) {
        String sql="DELETE FROM dish_ingredient WHERE id_dish = ?";

        jdbcTemplate.update(sql, dishId);
    }

    public void attachIngredients(Integer dishId, List<Ingredient> ingredients){
        String sql= """
                INSERT INTO dish_ingredient (id_dish, id_ingredient)
                VALUES (?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, ingredients, ingredients.size(), (ps, ingredient) -> {
            ps.setInt(1, dishId);
            ps.setInt(2, ingredient.getId());
        });
    }

    public boolean dishExists(int dishId){
        String sql ="SELECT id from dish WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, dishId);
    return count != null && count > 0;
    }
}
