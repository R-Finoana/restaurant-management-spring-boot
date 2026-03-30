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
        String sql = """
                select d.id AS dish_id,
                       d.name AS dish_name,
                       d.selling_price AS dish_price,
                       i.id AS ingredient_id,
                       i.name AS ingredient_name,
                       i.category AS ingredient_category,
                       i.price AS ingredient_price
                from dish d
                         join dish_ingredient di
                              on di.id_dish=d.id
                         join ingredient i
                              on di.id_ingredient=i.id;
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            List<Ingredient> ingredientList = new ArrayList<>();
            Ingredient ing = new Ingredient();
            ing.setId(rs.getInt("ingredient_id"));
            ing.setName(rs.getString("ingredient_name"));
            ing.setCategory(CategoryEnum.valueOf(rs.getString("ingredient_category")));
            ing.setPrice(rs.getDouble("ingredient_price"));

            ingredientList.add(ing);

            Dish dish = new Dish();
            dish.setId(rs.getInt("dish_id"));
            dish.setName(rs.getString("dish_name"));
            dish.setSellingPrice(rs.getDouble("dish_price"));
            dish.setIngredientList(ingredientList);

            return dish;
        });
    }

    public void detachIngredients(Integer dishId) {
        String sql="DELETE FROM dish_ingredient WHERE id_dish = ?";

        jdbcTemplate.update(sql, dishId);
    }

    public void attachIngredients(Integer dishId, List<Ingredient> ingredients){
        String sql= """
                INSERT INTO dish_ingredient (id_dish, id_ingredient, quantity_required, unit)
                SELECT ?, id, quantity_required, unit
                FROM ingredient
                WHERE id = ?
                """;

        jdbcTemplate.batchUpdate(sql, ingredients, ingredients.size(), (ps, ingredient) -> {
            ps.setInt(1, dishId);
            ps.setInt(2, ingredient.getId());
        });
    }

    public boolean dishExists(int dishId){
        String sql ="SELECT id, name, selling_price from dish WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, dishId);
    return count != null && count > 0;
    }
}
