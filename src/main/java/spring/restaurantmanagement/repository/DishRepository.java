package spring.restaurantmanagement.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import spring.restaurantmanagement.entity.CategoryEnum;
import spring.restaurantmanagement.entity.Dish;
import spring.restaurantmanagement.entity.Ingredient;

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
}
