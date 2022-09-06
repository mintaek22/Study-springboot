package hello.repository;

import hello.domain.item.Item;
import hello.domain.item.ItemSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;


@Slf4j
@Transactional
public class ItemRepositoryJDBC implements ItemRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ItemRepositoryJDBC(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public Item save(Item item) {

        String sql = "insert into item(ItemName,price,quantity) " +
                "values(:itemName,:price,:quantity)";

        //객체 자동 mapping으로 넣어줌
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql,param,keyHolder);

        Number key = keyHolder.getKey();
        item.setId(key.longValue());
        return item;
    }

    @Override
    public Item findById(Long id){

        String sql = "select * from item where id=:id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id",id);
        return jdbcTemplate.queryForObject(sql, param, itemRowMapper());
    }

    @Override
    public List<Item> findAll(ItemSearchDto itemSearchDto){
        String sql = "select * from item";

        return jdbcTemplate.query(sql,itemRowMapper());
    }

    @Override
    public void update(Long id, Item updateParam){
        String sql = "update item set " +
                "ItemName =:itemName,price=:price,quantity=:quantity " +
                "where id=:id";

        SqlParameterSource param = new MapSqlParameterSource()
        .addValue("itemName",updateParam.getItemName())
        .addValue("price",updateParam.getPrice())
        .addValue("quantity",updateParam.getQuantity())
        .addValue("id",id);

        jdbcTemplate.update(sql,param);
    }

    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class);
    }
}
