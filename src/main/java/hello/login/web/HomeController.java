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
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

//    @GetMapping("/") //로그인안한 사람도 들어가야하니까 required false
//    public String homeLoginV2(HttpServletRequest request, Model model ){
//
//        //세션 관리자에 저장된 회원 정보 조회(Member)로 캐스팅
//        Member member = (Member)sessionManager.getSession(request);
//
//        if(member == null ){
//            return "home";
//        }
//
//        model.addAttribute("member", member);
//        return "loginHome";
//    }

   // @GetMapping("/") //로그인안한 사람도 들어가야하니까 required false
    public String homeLoginV3(HttpServletRequest request, Model model ){

        HttpSession session = request.getSession(false); //처음 진입한 사람은 세션을 생성할 의도가 없기때문에 false
        if(session == null){
            return "home";
        }

        //세션 관리자에 저장된 회원 정보 조회(Member)로 캐스팅
        Member loginMember = (Member)sessionManager.getSession(request);

        //세션에 회원데이터가 없으면 home
        if(loginMember == null ){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/") //로그인안한 사람도 들어가야하니까 required false
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false)Member loginMember , Model model ){
            //세션을 찾고, 세션에 들어있는 데이터를 찾는 번거로운 과정을 스프링이 한번에 편리하게 처리해주는 것을 확인할 수 있다.

        //세션에 회원데이터가 없으면 home
        if(loginMember == null ){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}