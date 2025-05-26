package vn.edu.hcmaf.apigamestore.product.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import vn.edu.hcmaf.apigamestore.common.dto.LazyLoadingRequestDto;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    /**
     * This method filters accounts with lazy loading support based on the provided criteria in AccountFilterRequestDto.
     * It uses Criteria API to build dynamic queries and supports pagination.
     *
     * @param request The filter criteria for accounts with pagination details.
     * @return A Page of AccountEntity objects that match the filter criteria.
     */
    @Override
    public Page<AccountEntity> filterAccountsLazyLoading(AccountFilterRequestDto request) {
        int page = request.getPage();
        int size = request.getSize();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Truy vấn chính
        CriteriaQuery<AccountEntity> cq = cb.createQuery(AccountEntity.class);
        Root<AccountEntity> account = cq.from(AccountEntity.class);
        Join<?, ?> game = account.join("game");
        Join<?, ?> category = game.join("category");

        List<Predicate> predicates = buildPredicates(cb, account, game, category, request);
        cq.where(predicates.toArray(new Predicate[0]));

        // Sắp xếp
        if (request.getSortBy() != null) {
            switch (request.getSortBy()) {
                case "price_asc" -> cq.orderBy(cb.asc(account.get("price")));
                case "price_desc" -> cq.orderBy(cb.desc(account.get("price")));
                case "title_asc" -> cq.orderBy(cb.asc(account.get("title")));
                case "title_desc" -> cq.orderBy(cb.desc(account.get("title")));
            }
        }

        // Truy vấn phân trang
        TypedQuery<AccountEntity> query = entityManager.createQuery(cq);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        List<AccountEntity> results = query.getResultList();

        // Truy vấn đếm tổng số bản ghi
        Long total = countTotalRecords(cb, request);

        return new PageImpl<>(results, PageRequest.of(page - 1, size), total);
    }
    /**
     * Builds a list of predicates based on the filter criteria provided in AccountFilterRequestDto.
     *
     * @param cb The CriteriaBuilder instance used to create predicates.
     * @param account The root of the AccountEntity.
     * @param game The join to the GameEntity.
     * @param category The join to the CategoryEntity.
     * @param filter The filter criteria for accounts.
     * @return A list of predicates that can be used in the query.
     */
    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<AccountEntity> account,
                                            Join<?, ?> game, Join<?, ?> category,
                                            AccountFilterRequestDto filter) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            String kw = "%" + filter.getKeyword().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(account.get("title")), kw),
                    cb.like(cb.lower(account.get("info")), kw)
            ));
        }

        if (filter.getGameId() != null && !filter.getGameId().isEmpty()) {
            predicates.add(cb.equal(game.get("id"), Long.valueOf(filter.getGameId())));
        }

        if (filter.getCategoryId() != null && !filter.getCategoryId().isEmpty()) {
            predicates.add(cb.equal(category.get("id"), Long.valueOf(filter.getCategoryId())));
        }

        if (filter.getLowPrice() != null && !filter.getLowPrice().isEmpty()) {
            predicates.add(cb.greaterThanOrEqualTo(account.get("price"), Double.valueOf(filter.getLowPrice())));
        }

        if (filter.getHighPrice() != null && !filter.getHighPrice().isEmpty()) {
            predicates.add(cb.lessThanOrEqualTo(account.get("price"), Double.valueOf(filter.getHighPrice())));
        }

        return predicates;
    }
    /**
     * Counts the total number of records that match the filter criteria.
     *
     * @param cb The CriteriaBuilder instance used to create the count query.
     * @param filter The filter criteria for accounts.
     * @return The total count of records matching the filter criteria.
     */
    private long countTotalRecords(CriteriaBuilder cb, AccountFilterRequestDto filter) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<AccountEntity> countRoot = countQuery.from(AccountEntity.class);
        Join<?, ?> game = countRoot.join("game");
        Join<?, ?> category = game.join("category");

        List<Predicate> predicates = buildPredicates(cb, countRoot, game, category, filter);

        countQuery.select(cb.count(countRoot));
        countQuery.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(countQuery).getSingleResult();
    }


}