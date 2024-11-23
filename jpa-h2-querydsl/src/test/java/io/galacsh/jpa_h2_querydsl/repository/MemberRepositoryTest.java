package io.galacsh.jpa_h2_querydsl.repository;

import io.galacsh.jpa_h2_querydsl.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 성인_멤버() {
        // given
        Member member1 = Member.builder().username("John").age(17).build();
        Member member2 = Member.builder().username("Jane").age(18).build();
        Member member3 = Member.builder().username("Alex").age(19).build();
        Member member4 = Member.builder().username("Alice").age(20).build();
        Member member5 = Member.builder().username("C").age(21).build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);

        em.flush();
        em.clear();

        // when
        Page<Member> adultMembers = memberRepository.findAdultMembers(
                PageRequest.of(0, 2, Sort.by(Sort.Order.asc("age")))
        );

        // then
        assertThat(adultMembers.getTotalElements()).isEqualTo(3);
        assertThat(adultMembers.getTotalPages()).isEqualTo(2);

        assertThat(adultMembers.getContent())
                .extracting("username")
                .containsExactly("Alex", "Alice");

        assertThat(adultMembers.getContent())
                .extracting("age")
                .allMatch(age -> (int) age >= 19);
    }
}