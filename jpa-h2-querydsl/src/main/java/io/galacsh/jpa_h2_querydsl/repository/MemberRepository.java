package io.galacsh.jpa_h2_querydsl.repository;

import io.galacsh.jpa_h2_querydsl.domain.Member;
import io.galacsh.jpa_h2_querydsl.repository.query.MemberQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
}
