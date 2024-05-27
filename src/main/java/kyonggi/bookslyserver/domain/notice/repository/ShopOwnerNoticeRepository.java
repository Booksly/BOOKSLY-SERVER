package kyonggi.bookslyserver.domain.notice.repository;

import kyonggi.bookslyserver.domain.notice.entity.ShopOwnerNotice;
import kyonggi.bookslyserver.domain.notice.repository.CustomRepository.ShopOwnerNoticeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopOwnerNoticeRepository extends JpaRepository<ShopOwnerNotice,Long>, ShopOwnerNoticeRepositoryCustom {
}
