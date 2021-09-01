package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    // init(): 필터 초기화 메서드, 서블릿 컨테이너가 생성될 때 호출
    // doFilter(): 고객의 요청이 올 때 마다 해당 메서드가 호출된다. 필터의 로직을 구현하면 된다.
    // destroy(): 필터 종료 메서드, 서블릿 컨테이너가 종료될 때 호출된다.
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        //ServletRequest는 HttpServletRequest의 부모 인터페이스인데, 기능이 별로 없어서 다운캐스팅해준다
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        StringBuffer requestURI = httpRequest.getRequestURL();

        String uuid = UUID.randomUUID().toString(); //Http 요청을 구분하기 위해 임의의 uuid만든다.

        try{
            log.info("REQUEST [{}][[{}]", uuid, requestURI);
            chain.doFilter(request, response);//다음필터가 있으면 호출, 없으면 서블릿 호출, 이 로직이 없으면 다음 단계 진행이 안된다.
        }catch (Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");

    }
}
