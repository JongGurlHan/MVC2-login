package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response){
        //HttpServletResponse response: 쿠키 생성한거를 서버에서 http응답으로 response에 넣어서 보내줘야하기 때문에
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        if(loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            //reject:글로벌오류, DB까지 뒤져야 알 수 있는 오류(필드, 오브젝트오류x)
            return "login/loginForm";
        }

        //로그인 성공처리
        //쿠키에 시간 정보를 주지 않으면 세션쿠키(브라우저 종료시 모두 종료)
        //로그인에 성공하면 쿠키를 행성하고 HttpServletResponse에 담는다. 쿠키 이름은 memberId고, 값은 회원의 id를 담는다.
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        return "redirect:/";
    }
    //쿠키를 없애는 방법은 쿠키의 시간을 없애면 된다.
    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expiredCookie(response, "memberId"); //응답넣고 쿠키명 넣으면 expire 해준다.
        return "redirect:/"; 
    }

    private void expiredCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
