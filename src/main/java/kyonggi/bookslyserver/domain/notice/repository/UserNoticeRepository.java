package kyonggi.bookslyserver.domain.notice.repository;

import kyonggi.bookslyserver.domain.notice.entity.UserNotice;
import kyonggi.bookslyserver.domain.notice.repository.CustomRepository.UserNoticeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoticeRepository extends JpaRepository<UserNotice,Long>, UserNoticeRepositoryCustom {
}