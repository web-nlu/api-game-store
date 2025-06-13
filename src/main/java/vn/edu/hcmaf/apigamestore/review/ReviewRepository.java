package vn.edu.hcmaf.apigamestore.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface  ReviewRepository extends JpaRepository<ReviewEntity, Long> {

  List<ReviewEntity> findByAccountId(Long accountId);

  @NativeQuery("""
          SELECT * FROM reviews r
          WHERE (:rating IS NULL OR r.rating = :rating)
            AND (:accountId IS NULL OR r.account_id = :accountId)
            AND r.is_hide = :isHide
          ORDER BY r.created_at DESC, r.rating DESC
          LIMIT :limit OFFSET :offset
          """)
  List<ReviewEntity> filter(
          @Param("rating") Integer rating,
          @Param("accountId") Long accountId,
          @Param("isHide") boolean isHide,
          @Param("offset") int offset,
          @Param("limit") int limit
  );

  @NativeQuery("""
          SELECT COUNT(r.id) FROM reviews r
          WHERE (:rating IS NULL OR r.rating = :rating)
            AND (:accountId IS NULL OR r.account_id = :accountId)
            AND r.is_hide = :isHide
          """)
  int count(@Param("rating") Integer rating,
            @Param("accountId") Long accountId,
            @Param("isHide") boolean isHide);
}
