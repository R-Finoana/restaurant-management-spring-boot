package spring.restaurantmanagement.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class StockValue {
    private Double quantity;
    private UnitTypeEnum unit;
}
