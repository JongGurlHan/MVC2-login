package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
* 세션관리*/
@Component
public class SessionManager {

    //상수로 만든다
    public static final String SESSION_COOKIE_NAME = "mySessionId";

    //동시성 이슈가 발생할 수 있으니까
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    //세션생성

    /**
     * 세션 생성
     * sessionId 생성 (임의의 추정 불가능한 랜덤 값)
     * 세션 저장소에 sessionId와 보관할 값 저장
     * sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response){

        //sessionId 생생하고 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        //쿠키생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);//쿠키에 키값 "mySessionId", 벨류 sessionId(랜던생성값)넣는다
        response.addCookie(mySessionCookie);
    }
    /**
     * 세션조회
     */
//    public Object getSession(HttpServletRequest request){
//        Cookie[] cookies = request.getCookies();//쿠키가 배열로 반환이된다.
//        if(cookies == null){ // 쿠키가 하나도 없다면?
//            return null;
//        }
//        for (Cookie cookie : cookies) {
//            if(cookie.getName().equals(SESSION_COOKIE_NAME)){ //쿠키이름이 SESSION_COOKIE_NAME랑 같으면
//                return sessionStore.get(cookie.getValue()); //sessionStore에서 value찾는다
//            }
//        }
//        return null;
//    }

    public Object getSession(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie == null){
            return null;
        }
        return sessionStore.get(sessionCookie.getValue()); //member객체가 반환
    }

    /**
     * 세션만료
     */
    public void expire(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    //쿠키찾는 로직
    public Cookie findCookie(HttpServletRequest request, String cookieName){
        if(request.getCookies()== null){ // 쿠키가 하나도 없다면?
            return null;
        }
        //스트림은 자바8부터 추가된 컬렉션의 저장 요소를 하나씩 참조해서 람다식으로 처리할 수 있도록 해주는 반복자입니다.
        // Iterator와 비슷한 역할을 하지만 람다식으로 요소 처리 코드를 제공하여 코드가 좀 더 간결하게 할 수 있다는 점과 내부 반복자를 사용하므로 병렬처리가 쉽다는 점에서 차이점이 있습니다.
        return Arrays.stream(request.getCookies()) //쿠키가 있다면 배열을 stream으로 바꿔준다.
                .filter(cookie -> cookie.getName().equals(cookieName)) //cookieName(SESSION_COOKIE_NAME)이 있으면
                .findAny() //cookie 반환
                .orElse(null);
    }


}
