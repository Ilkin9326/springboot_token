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

    @Query(value = "select * from User u", nativeQuery = true)
    List<EmployeeDTO> getAllUser();

    @Query(value = "select e.emp_id from employee e where e.email=?1", nativeQuery = true)
    Integer getEmpIdByEmail(String email);
}
