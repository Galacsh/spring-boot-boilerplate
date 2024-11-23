package io.galacsh.jpa_h2_querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.galacsh.jpa_h2_querydsl.domain.Member;
import io.galacsh.jpa_h2_querydsl.domain.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;
import static io.galacsh.jpa_h2_querydsl.domain.QMember.member;
import static io.galacsh.jpa_h2_querydsl.domain.QTeam.team;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SimpleTest {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    EntityManager em;

    JPAQueryFactory query;

    @BeforeEach
    void setup() {
        query = new JPAQueryFactory(em);

        Team teamA = new Team("A");
        Team teamB = new Team("B");
        Team teamC = new Team("C");
        em.persist(teamA);
        em.persist(teamB);
        em.persist(teamC);

        Member member1 = Member.builder().username("John").age(20).build();
        Member member2 = Member.builder().username("Jane").age(21).build();
        Member member3 = Member.builder().username("Alex").age(22).build();
        Member member4 = Member.builder().username("Alice").age(23).build();
        Member member5 = Member.builder().username("C").age(24).build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.persist(member5);

        member1.changeTeam(teamA);
        member2.changeTeam(teamA);
        member3.changeTeam(teamB);
        member4.changeTeam(teamB);
        member5.changeTeam(teamC);

        em.flush();
        em.clear();
    }

    @Test
    void JPQL_테스트() {
        String query = "select m from Member m where m.username = :username";

        Member found = em.createQuery(query, Member.class)
                .setParameter("username", "John")
                .getSingleResult();

        assertThat(found.getUsername()).isEqualTo("John");
    }

    @Test
    void Querydsl_사용해보기() {
        Member found = query
                .selectFrom(member)
                .where(
                        member.username.eq("John"),
                        member.age.goe(20)
                ).fetchOne();
        assert found != null;

        assertThat(found.getUsername()).isEqualTo("John");
    }

    @Test
    void 정렬() {
        List<Member> sorted = query.selectFrom(member)
                .where(member.age.goe(20))
                .orderBy(member.age.desc().nullsLast())
                .fetch();

        Member previous = sorted.getFirst();
        for (int i = 1; i < sorted.size(); i++) {
            Member current = sorted.get(i);
            assertThat(previous.getAge()).isGreaterThanOrEqualTo(current.getAge());
            previous = current;
        }
    }

    @Test
    void GroupBy() {
        // when
        List<Tuple> result = query.select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .having(team.name.eq("A"))
                .fetch();

        // then
        assertThat(result.size()).isEqualTo(1);

        Tuple teamA = result.getFirst();
        assertThat(teamA.get(team.name)).isEqualTo("A");
        assertThat(teamA.get(member.age.avg())).isEqualTo(20.5);
    }

    @Test
    void LeftJoin() {
        // when
        List<Tuple> result = query.select(member, team)
                .from(member)
                // 연관관계 컬럼으로 join 수행
                // Left 조인이므로 모든 멤버를 일단 가져옴
                .leftJoin(member.team, team)
                // member age가 22인 행들만 Join 됨
                // left 조인이므로 행 수에는 변화 없음
                .on(member.age.goe(22))
                .fetch();

        // then
        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(0).get(team)).isNull();
        assertThat(result.get(1).get(team)).isNull();
        assertThat(result.get(2).get(team)).isNotNull();
        assertThat(result.get(3).get(team)).isNotNull();
        assertThat(result.get(4).get(team)).isNotNull();
    }

    @Test
    void LeftJoin_연관관계_없을_때() throws Exception {
        // when
        List<Tuple> result = query.select(member, team)
                .from(member)
                // 연관관계 컬럼으로 join 수행
                // Left 조인이므로 모든 멤버를 일단 가져옴
                .leftJoin(team)
                // member age가 22인 행들만 Join 됨
                // left 조인이므로 행 수에는 변화 없음
                .on(member.username.eq(team.name))
                .fetch();

        // then
        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(0).get(team)).isNull();
        assertThat(result.get(1).get(team)).isNull();
        assertThat(result.get(2).get(team)).isNull();
        assertThat(result.get(3).get(team)).isNull();
        assertThat(result.get(4).get(team)).isNotNull();
    }

    @Test
    void No_FetchJoin() {
        // when
        Member found = query.selectFrom(member)
                .where(member.username.eq("John"))
                .fetchOne();
        assert found != null;

        // then
        PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
        boolean loaded = util.isLoaded(found.getTeam());
        assertThat(loaded).isFalse();
    }

    @Test
    void FetchJoin() {
        // when
        Member found = query.selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("John"))
                .fetchOne();
        assert found != null;

        // then
        PersistenceUnitUtil util = emf.getPersistenceUnitUtil();
        boolean loaded = util.isLoaded(found.getTeam());
        assertThat(loaded).isTrue();
    }

    @Test
    void Subquery() throws Exception {
        // when
        Tuple tuple = query
                .select(member, select(member.age.avg()).from(member))
                .from(member)
                .where(member.age.eq(
                        select(member.age.max()).from(member)
                )).fetchOne();
        assert tuple != null;

        // then
        Member found = Optional.ofNullable(tuple.get(member)).orElseThrow();
        assertThat(found.getUsername()).isEqualTo("C");
        assertThat(tuple.get(1, Double.class)).isEqualTo(22);
    }
}