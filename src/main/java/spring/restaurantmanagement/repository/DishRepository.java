package spring.restaurantmanagement.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.restaurantmanagement.entity.CategoryEnum;
import spring.restaurantmanagement.entity.Dish;
import spring.restaurantmanagement.entity.Ingredient;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class DishRepository {
    DataSource dataSource;

    public List<Dish> findDish(){
        String dishSql = "SELECT id, name, selling_price FROM dish";
        List<Dish> dishes = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(dishSql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Dish d = new Dish();
                d.setId(rs.getInt("id"));
                d.setName(rs.getString("name"));
                d.setSellingPrice(rs.getDouble("selling_price"));
                dishes.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Dish dish : dishes) {
            dish.setIngredientList(findIngredientsByDishId(dish.getId()));
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
        List<Ingredient> ingredients = new ArrayList<>();

        try(Connection conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){

            ps.setInt(1, dishId);

            try (ResultSet rs = ps.executeQuery();){
                while (rs.next()) {
                    Ingredient ing = new Ingredient();
                    ing.setId(rs.getInt("id"));
                    ing.setName(rs.getString("name"));
                    ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                    ing.setPrice(rs.getDouble("price"));

                    ingredients.add(ing);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }

    public void detachIngredients(Integer dishId) {
        String sql="DELETE FROM dish_ingredient WHERE id_dish = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dishId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void attachIngredients(Integer dishId, List<Ingredient> ingredients){
        String sql= """
                INSERT INTO dish_ingredient (id_dish, id_ingredient)
                VALUES (?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Ingredient ing : ingredients) {
                ps.setInt(1, dishId);
                ps.setInt(2, ing.getId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean dishExists(int dishId){
        String sql ="SELECT id from dish WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dishId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
