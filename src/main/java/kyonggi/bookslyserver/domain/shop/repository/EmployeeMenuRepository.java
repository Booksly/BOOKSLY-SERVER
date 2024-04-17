package kyonggi.bookslyserver.domain.shop.repository;

import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeMenuRepository extends JpaRepository<EmployeeMenu,Long> {
}
