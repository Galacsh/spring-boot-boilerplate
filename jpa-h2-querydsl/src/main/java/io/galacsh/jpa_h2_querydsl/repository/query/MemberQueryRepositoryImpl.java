package io.galacsh.jpa_h2_querydsl.repository.query;

import io.galacsh.jpa_h2_querydsl.domain.Member;
import io.galacsh.jpa_h2_querydsl.util.QuerydslSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static io.galacsh.jpa_h2_querydsl.domain.QMember.member;

public class MemberQueryRepositoryImpl extends QuerydslSupport implements MemberQueryRepository {
    public MemberQueryRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public Page<Member> findAdultMembers(Pageable pageable) {
        return applyPagination(
                pageable,
                member::count,
                q -> q.selectFrom(member)
                        .where(member.age.goe(19))
        );
    }
}
