package spring.restaurantmanagement.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.restaurantmanagement.entity.*;

import javax.sql.DataSource;
import java.sql.*;
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

    Dish findDishById(Integer id) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    """
                            select dish.id as dish_id, dish.name as dish_name, dish_type, dish.selling_price as selling_price
                            from dish
                            where dish.id = ?;
                            """);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt("dish_id"));
                dish.setName(resultSet.getString("dish_name"));
                dish.setDishType(DishTypeEnum.valueOf(resultSet.getString("dish_type")));
                dish.setSellingPrice(resultSet.getObject("selling_price") == null
                        ? null : resultSet.getDouble("selling_price"));
                return dish;
            }
            throw new RuntimeException("Dish not found " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Dish> saveDishes(List<CreateDish> toSave) {
        String insertSql = """
            INSERT INTO dish (name, dish_type, selling_price)
            VALUES (?, ?::dish_type, ?)
            RETURNING id
            """;
        List<Integer> ids = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                for (CreateDish d : toSave) {
                    ps.setString(1, d.getName());
                    ps.setString(2, d.getDishType().name());
                    if (d.getSellingPrice() != null) {
                        ps.setDouble(3, d.getSellingPrice());
                    } else {
                        ps.setNull(3, Types.DOUBLE);
                    }
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            ids.add(rs.getInt(1));
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ids.stream()
                .map(this::findDishById)
                .toList();
    }

    public boolean dishNameExists(String name) {
        String sql = "SELECT id FROM dish WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
