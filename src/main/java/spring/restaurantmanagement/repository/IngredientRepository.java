package spring.restaurantmanagement.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import spring.restaurantmanagement.entity.CategoryEnum;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.entity.StockValue;
import spring.restaurantmanagement.entity.UnitTypeEnum;
import spring.restaurantmanagement.exception.IngredientNotFoundException;

import java.time.Instant;
import java.util.List;

@Repository
@AllArgsConstructor
public class IngredientRepository {
    JdbcTemplate jdbcTemplate;

    public List<Ingredient> findIngredient() {
        String sql= """
                select id,
                       name,
                       category,
                       price
                from ingredient;
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ingredient ing = new Ingredient();
            ing.setId(rs.getInt("id"));
            ing.setName(rs.getString("name"));
            ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
            ing.setPrice(rs.getDouble("price"));

            return ing;
        });
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

        try{
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Ingredient ing = new Ingredient();
                ing.setId(rs.getInt("id"));
                ing.setName(rs.getString("name"));
                ing.setCategory(CategoryEnum.valueOf(rs.getString("category")));
                ing.setPrice(rs.getDouble("price"));

                return ing;
            }, id);
        } catch (EmptyResultDataAccessException e){
            throw new IngredientNotFoundException(id);
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

        try{
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            StockValue stock = new StockValue();
            stock.setQuantity(rs.getDouble("actual_quantity"));
            stock.setUnit(UnitTypeEnum.valueOf(rs.getString("unit")));

            return stock;
            }, t, id);
        } catch (EmptyResultDataAccessException e){
            throw new IngredientNotFoundException(id);
        }
    }
}
