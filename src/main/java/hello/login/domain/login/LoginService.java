package hello.login.domain.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     *로그인의 핵심 비즈니스 로직은 회원을 조회한 다음에 파라미터로 넘어온 password와 비교해서 같으면
     * 회원을 반환하고, 만약 password가 다르면 null 을 반환한다.
     * @return null 로그인 실패
     */
     public Member login(String loginId, String password){
//         Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
//
//         Member member = findMemberOptional.get();//.get해서 꺼내온다, 없으면 예외발생
//
//         if(member.getPassword().equals(password)){
//             return member;
//         }else {
//             return null;
//         }

//         Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);
//         return byLoginId.filter(m-> m.getPassword().equals(password)) //optional안에 들어있는 멤버의 비번이 비번과 같은가
//            .orElse(null); //같지 않으면 null반환해

      return memberRepository.findByLoginId(loginId)
                .filter(m-> m.getPassword().equals(password)) //optional안에 들어있는 멤버의 비번이 비번과 같은가 같으면 m 리턴
                .orElse(null); //같지 않으면 null반환해
     }
}
