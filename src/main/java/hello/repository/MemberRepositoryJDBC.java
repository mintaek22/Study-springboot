package hello.repository;

import hello.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import javax.transaction.Transactional;

@Slf4j
@Transactional
public class MemberRepositoryJDBC implements MemberRepository{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MemberRepositoryJDBC(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Member save(Member member){

        String sql = "insert into member(loginId,password,username) " +
                "values(:loginId,:password,username)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql,param,keyHolder);

        Number key = keyHolder.getKey();
        member.setId(key.longValue());
        return member;
    }

    public Member findByLoginId(String loginId){

        String sql = "select * from member where loginId=:loginId";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("loginId",loginId);
        return jdbcTemplate.queryForObject(sql, param, memberRowMapper());

    }

    private RowMapper<Member> memberRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }
}
