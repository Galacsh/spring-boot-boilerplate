package io.galacsh.jpa_h2_querydsl.util;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Repository
public abstract class QuerydslSupport {

    private final Class<?> entityClass;
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;
    private Querydsl querydsl;

    public QuerydslSupport(Class<?> entityClass) {
        Assert.notNull(entityClass, "Entity class must not be null");
        this.entityClass = entityClass;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null");

        this.entityManager = entityManager;

        this.queryFactory = new JPAQueryFactory(entityManager);

        JpaEntityInformation<?, ?> entityInformation = JpaEntityInformationSupport.getEntityInformation(entityClass, entityManager);
        EntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<?> path = resolver.createPath(entityInformation.getJavaType());
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null");
        Assert.notNull(queryFactory, "JPAQueryFactory must not be null");
        Assert.notNull(querydsl, "Querydsl must not be null");
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

    protected Querydsl getQuerydsl() {
        return querydsl;
    }

    protected <T> JPAQuery<T> select(Expression<T> expression) {
        return queryFactory.select(expression);
    }

    protected JPAQuery<Tuple> select(Expression<?>... expressions) {
        return queryFactory.select(expressions);
    }

    protected <T> JPAQuery<T> selectFrom(EntityPath<T> entityPath) {
        return queryFactory.selectFrom(entityPath);
    }

    protected <T> Page<T> applyPagination(Pageable pageable, Supplier<NumberExpression<Long>> countExpression, Function<JPAQueryFactory, JPAQuery<T>> contentQuery) {
        JPAQuery<T> jpaContentQuery = contentQuery.apply(queryFactory);
        List<T> content = querydsl.applyPagination(pageable, jpaContentQuery).fetch();

        JPAQuery<Long> countQuery = contentQuery.apply(queryFactory).select(countExpression.get());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
