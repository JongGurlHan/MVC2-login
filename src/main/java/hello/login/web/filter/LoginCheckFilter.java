package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    //로그인 상관없이 들어갈수 있는 경로 설정
    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};

    //인터페이스의 default는 구현안해도 된다. 즉, init, destroy는 구현 안해도 된다.

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작{}", requestURI);

            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행{},", requestURI);
                HttpSession session = httpRequest.getSession(false); //세션이 있는지 확인
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null){ //세션이 null이거나(로그인x), 어트리뷰트가 null이면

                    log.info("미인증 사용자 요청{}", requestURI); //로그인 안함
                    //로그인으로 redirect
                    //로그인 페이지로 보내고, 로그인했으면 다시 이 페이지(requestURI)로 이동
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }
            chain.doFilter(request, response); //화이트 리스트면 위의 조건절을 안타고 바로 doFilter로 넘어간다
        }catch (Exception e){
            throw e; //예외 로깅 가능, 톰켓까지 예외를 보내줘야함
        }finally {
            log.info("인증체크 필터 종로{}", requestURI);

        }
        }
    /**
     * 화이트 리스트의 경우 인증 체크x
     */
        private boolean isLoginCheckPath(String requestURI) {
            return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
        }
        // whitelist와 requestURI가 매칭되는가 확인
        // whitelist에 안든건 false가 된다.

}
