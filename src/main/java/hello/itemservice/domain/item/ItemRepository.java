package hello.itemservice.domain.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static hello.itemservice.domain.jdbc.DBConnectionUtil.getConnection;


@Slf4j
@Repository
public class ItemRepository {


    //멀티스레드 환경에서는 HaspMap x,ConcurrentHashMap o
    private static long sequence = 0L;

    public Item save(Item item) throws SQLException {
        String sql = "insert into item(id,ItemName,price,quantity) values(?,?,?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(sequence++));
            pstmt.setString(2,item.getItemName());
            pstmt.setInt(3,item.getPrice());
            pstmt.setInt(4,item.getQuantity());
            pstmt.executeUpdate();
            return item;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Item findById(Long id) throws SQLException {

        String sql = "select * from item where id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Math.toIntExact(id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Item item = new Item();
                item.setId(Long.valueOf(rs.getString("id")));
                item.setItemName(rs.getString("ItemName"));
                item.setPrice(Integer.valueOf(rs.getString("price")));
                item.setQuantity(Integer.valueOf(rs.getString("quantity")));
                return item;
            } else {
                throw new NoSuchElementException("해당하는 아이디의 아이템을 찾을 수가 없습니다="+id);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public List<Item> findAll() throws SQLException {
        String sql = "select * from item";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            List<Item> list = new ArrayList<>();
            while (rs.next()) {
                Item item = new Item();
                item.setId(Long.valueOf(rs.getString("id")));
                item.setItemName(rs.getString("ItemName"));
                item.setPrice(Integer.valueOf(rs.getString("price")));
                item.setQuantity(Integer.valueOf(rs.getString("quantity")));
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    public void update(Long id, Item updateParam) throws SQLException {
        String sql = "update item set ItemName=? price=? quantity=? where id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,updateParam.getItemName());
            pstmt.setInt(2,updateParam.getPrice());
            pstmt.setInt(3,updateParam.getQuantity());
            pstmt.setInt(4, Math.toIntExact(id));
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }
}
