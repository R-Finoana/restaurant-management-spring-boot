package spring.restaurantmanagement.entity;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CreateDish {
    private String name;
    private DishTypeEnum dishType;
    private Double sellingPrice;
}
