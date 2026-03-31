package spring.restaurantmanagement.entity;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Dish {
    private int id;
    private String name;
    private Double sellingPrice;
    private List<Ingredient> ingredientList;
    private DishTypeEnum dishType;
}
