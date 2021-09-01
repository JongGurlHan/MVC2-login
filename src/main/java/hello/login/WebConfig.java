package hello.login;

import hello.login.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    //스프링부트를 사용할때 필터 등록할때 Bean등록해준다.
    //스프링부트가 WAS를 띄울때 필터를 넣어준다.
    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter()); //만들어준 필터를 넣어준다.
        filterFilterRegistrationBean.setOrder(1); //필터 체인 순서를 정해준다.
        filterFilterRegistrationBean.addUrlPatterns("/*"); //모든 url에 적용

        return  filterFilterRegistrationBean;
    }
}
