package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //@GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/") //로그인안한 사람도 들어가야하니까 required false
    //@CookieValue: 스프링이 제공, 쿠키를 꺼내준다.
    public String homeLogin(@CookieValue(name="memberId", required = false) Long memberId, Model model ){
    //스프링에서 자동으로 String -> Long 으로 타입컨버팅해준다.
        if(memberId == null ){
            return "home";
        }

        //로그인성공(쿠키정보있음)
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null){ //쿠키가 너무 옛날에 만들어졌던가 해서 db에 없을수 있따. 쿠기는 있지만 DB에 없다면
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/") //로그인안한 사람도 들어가야하니까 required false
    public String homeLoginV2(HttpServletRequest request, Model model ){

        //세션 관리자에 저장된 회원 정보 조회(Member)로 캐스팅
        Member member = (Member)sessionManager.getSession(request);

        if(member == null ){
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }
}