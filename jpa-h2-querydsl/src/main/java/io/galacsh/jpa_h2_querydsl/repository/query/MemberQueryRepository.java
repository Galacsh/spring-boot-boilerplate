package io.galacsh.jpa_h2_querydsl.repository.query;

import io.galacsh.jpa_h2_querydsl.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberQueryRepository {
    Page<Member> findAdultMembers(Pageable pageable);
}
