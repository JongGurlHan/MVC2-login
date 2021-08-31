package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;


class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest(){
        //세션생성(서버에서 세션을 만들고 쿠키만들어서 response에 담아놓음, 응답보냄) 서버->클라이언트
        MockHttpServletResponse response = new MockHttpServletResponse();//test 진행위해 스프링에서 제공
        Member member = new Member();
        sessionManager.createSession(member, response);

        //요청에 응답 쿠키 저장 (웹브라우저가 쿠키 만들어 서버에 전달) 클라이언트->서버
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); //mySessionId=1231-13213-123-1

        //세션조회 -서버에서
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        //세션만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNotNull();
    }

}