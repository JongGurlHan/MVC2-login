package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); //static사용
    private static long sequence = 0L;

    //회원 저장
    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    //회원 조회
    public Member findById(Long id){
        return store.get(id);
    }

    //못찾을 수 있으니까
    //Optional: Optional이라는 통 안에 회원객체가 있을수도, 없을수도 있다.
    //값을 null로 반환해야하는 상황에서는 null을 직접 반환하는 대신에 값이 없으면
    //Optional.empty();로 찾을 수 있도록 설정
    public Optional<Member> findByLoginId(String loginId){
        List<Member>  all = findAll();
        for (Member m : all) {
               if(m.getLoginId().equals(loginId)){
                   return Optional.of(m);
               }
        }
        return Optional.empty();
    }

    //자바8 람다버전
//    return findAll().stream() //list를 stream으로 바꾼다
//        .filter(m -> m.getLoginId().equals(loginId)) //filter: 괄호안을 만족해야 다음으로 넘어간다. /루프안에서 m.getLoginId().equals(loginId) 일때만 넘어간다.
//        .findFirst(); //먼저 나오는애를 받아서 반환한다.

    //전체 회원 조회
    public List<Member>findAll(){
        return new ArrayList<>(store.values());
    }

    //테스트 하고 초기화위해서
    public void clearStore(){
       store.clear();
    }



}
