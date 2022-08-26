package hello.itemservice.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.NoSuchElementException;

import static hello.itemservice.domain.jdbc.DBConnectionUtil.getConnection;

@Slf4j
@Repository
public class MemberRepository {

    private static long sequence = 0L;

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(id,loginId,password,username) values(?,?,?,?)";
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(sequence++));
            pstmt.setString(2, member.getLoginId());
            pstmt.setString(3, member.getPassword());
            pstmt.setString(4, member.getUsername());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Member findByLoginId(String loginId) throws SQLException {
        String sql = "select * from member where loginId=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1,loginId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setId(Long.valueOf(rs.getString("id")));
                member.setLoginId(rs.getString("LoginId"));
                member.setPassword(rs.getString("password"));
                member.setUsername(rs.getString("username"));
                return member;
            } else {
                throw new NoSuchElementException("member not found loginId="+loginId);
            }
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
