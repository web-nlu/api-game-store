package vn.edu.hcmaf.apigamestore.product.repo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import vn.edu.hcmaf.apigamestore.product.AccountEntity;
import vn.edu.hcmaf.apigamestore.product.dto.AccountFilterRequestDto;

import java.util.ArrayList;
import java.util.List;
@Repository
public class AccountRepositoryImpl implements AccountRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AccountEntity> filterAccounts(AccountFilterRequestDto dto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AccountEntity> cq = cb.createQuery(AccountEntity.class);
        Root<AccountEntity> account = cq.from(AccountEntity.class);
        Join<?, ?> game = account.join("game");
        Join<?, ?> category = game.join("category");

        List<Predicate> predicates = new ArrayList<>();

        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            String kw = "%" + dto.getKeyword().toLowerCase() + "%";
            Predicate titleLike = cb.like(cb.lower(account.get("title")), kw);
            Predicate infoLike = cb.like(cb.lower(account.get("info")), kw);
            predicates.add(cb.or(titleLike, infoLike));
        }

        if (dto.getGameId() != null && !dto.getGameId().isEmpty()) {
            predicates.add(cb.equal(game.get("id"), Long.valueOf(dto.getGameId())));
        }

        if (dto.getCategoryId() != null && !dto.getCategoryId().isEmpty()) {
            predicates.add(cb.equal(category.get("id"), Long.valueOf(dto.getCategoryId())));
        }

        if (dto.getLowPrice() != null && !dto.getLowPrice().isEmpty()) {
            predicates.add(cb.greaterThanOrEqualTo(account.get("price"), Double.valueOf(dto.getLowPrice())));
        }

        if (dto.getHighPrice() != null && !dto.getHighPrice().isEmpty()) {
            predicates.add(cb.lessThanOrEqualTo(account.get("price"), Double.valueOf(dto.getHighPrice())));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        // Sắp xếp nếu có
        if (dto.getSortBy() != null) {
            switch (dto.getSortBy()) {
                case "price_asc" -> cq.orderBy(cb.asc(account.get("price")));
                case "price_desc" -> cq.orderBy(cb.desc(account.get("price")));
                case "title_asc" -> cq.orderBy(cb.asc(account.get("title")));
                case "title_desc" -> cq.orderBy(cb.desc(account.get("title")));
            }
        }

        return entityManager.createQuery(cq).getResultList();
    }
}
