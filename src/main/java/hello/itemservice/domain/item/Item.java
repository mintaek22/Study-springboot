package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;


/**
 * @Notnull은 Null값만 허용되지 않는다 "" , " "는 가능하다
 * @NotBlank는 null," ","" 다 허용하지 않는다
 */
@Getter @Setter
public class Item {

    private Long id;

    @NotBlank
    private String itemName;

    @NotBlank
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotBlank
    @Range(min = 1, max = 100)
    private Integer quantity;
    public Item() {}

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
