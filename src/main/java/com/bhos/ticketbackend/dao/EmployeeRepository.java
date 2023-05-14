package com.bhos.ticketbackend.dao;

import com.bhos.ticketbackend.dto.EmployeeDTO;
import com.bhos.ticketbackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findByEmail(String email);

    @Query(value = "select e.emp_id, e.first_name, e.last_name, e.username, e.email, null as password, string_agg(r.title, ', ') as role, e.status from employee as e\n" +
            "left join emp_roles as er on er.emp_id=e.emp_id\n" +
            "left join roles as r on r.role_id=er.role_id where e.emp_id = ?1 and e.is_active=1 group by e.emp_id", nativeQuery = true)
    Optional<Employee> findByEmpId(Integer emp_id);

    @Query(value = "select e.emp_id, e.first_name, e.last_name, e.username, e.email, null as password, string_agg(r.title, ', ') as role, e.status from employee as e\n" +
            "left join emp_roles as er on er.emp_id=e.emp_id\n" +
            "left join roles as r on r.role_id=er.role_id where e.is_active=1 group by e.emp_id order by e.emp_id", nativeQuery = true)
    List<Employee> getAllUser();

    @Query(value = "select e.emp_id from employee e where e.email=?1", nativeQuery = true)
    Integer getEmpIdByEmail(String email);
}
