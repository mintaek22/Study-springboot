package hello;

import hello.repository.*;
import hello.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.servlet.Filter;

@Configuration
public class WebConfig {

    private final EntityManager em;

    public WebConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public FilterRegistrationBean logCheckFilter(){

        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());

        //필터 적용 순서
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public ItemRepository itemRepository() {
        return new itemRepositoryJPA(em);
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepositoryJPA(em);
    }
}
