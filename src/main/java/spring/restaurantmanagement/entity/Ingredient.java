package spring.restaurantmanagement.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Ingredient {
    private Integer id;
    private String name;
    private CategoryEnum category;
    private Double price;
}
