package spring.restaurantmanagement.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import spring.restaurantmanagement.entity.CategoryEnum;
import spring.restaurantmanagement.entity.Ingredient;
import spring.restaurantmanagement.exception.IngredientNotFoundException;

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
}
