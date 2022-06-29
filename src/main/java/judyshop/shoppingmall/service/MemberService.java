package judyshop.shoppingmall.service;

import judyshop.shoppingmall.domain.Member;
import judyshop.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)  // @Transactional springframework 로 선택하기
@RequiredArgsConstructor  // final 이 있는 필드에 대해서만 생성자를 만들어 줌.
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    @Transactional // 위에 있는 readOnly 트랜잭션보다 이게 우선권을 갖음. 그래서 쓰기도 가능함.
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검사 -> 문제 없으면 save 하고 id값 반환
        memberRepository.save(member);
        return member.getId();
    }

    // 중복인 경우 Exception 처리
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 단건 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    // 회원 이름 수정 (변경 감지 기능 사용)
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }

}
