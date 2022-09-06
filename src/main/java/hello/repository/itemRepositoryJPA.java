package hello.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.domain.item.Item;
import hello.domain.item.ItemSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static hello.domain.item.QItem.item;

@Slf4j
@Transactional
public class itemRepositoryJPA implements ItemRepository{

    private final EntityManager em;
    private final JPAQueryFactory query;


    public itemRepositoryJPA(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    @Override
    public Item save(Item item){
        em.persist(item);
        return item;
    }

    @Override
    public void update(Long id, Item updateParam) {
        Item findItem = em.find(Item.class,id);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Item findById(Long id){
        return em.find(Item.class,id);
    }

    @Override
    public List<Item> findAll(ItemSearchDto itemSearchDto){

        String itemName = itemSearchDto.getItemName();
        Integer maxPrice = itemSearchDto.getPrice();

        return query.select(item)
                .from(item)
                .where(likeItemName(itemName),MaxPrice(maxPrice))
                .fetch();
    }

    private BooleanExpression likeItemName(String itemName){
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }
        return null;
    }

    private BooleanExpression MaxPrice(Integer price){
        if(price != null){
            return item.price.loe(price);
        }
        return null;
    }
}
