package judyshop.shoppingmall.service;

import judyshop.shoppingmall.domain.Member;
import judyshop.shoppingmall.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional // 반복 가능한 테스트 지원(각각의 테스트를 실행할 때마다 트랜잭션을 시작하고 테스트가 끝나면 트랜잭션을 강제로 롤백 / 테스트에서만!)
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
//    @Rollback(false)  // 롤백 안하고 커밋해서 값 잘 들어갔는지 확인하기
    public void 회원가입() throws Exception {

        // given
        Member member = new Member();
        member.setName("Tei");

        // when
        Long savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId)); // 같은 영속성 컨텍스트 안에서는 아이디 값이 같으면 같은 엔티티로 식별함
    }

    @Test(expected = IllegalStateException.class)
    public void 중복회원예외() throws Exception {

        // given
        Member member1 = new Member();
        member1.setName("Tei");

        Member member2 = new Member();
        member2.setName("Tei");

        // when
        memberService.join(member1);
        memberService.join(member2); // 예외가 발생해야 함!!!

        // then
        fail("예외가 발생해야 함");

    }
}