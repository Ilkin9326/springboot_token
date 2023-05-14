package com.bhos.ticketbackend.dao;

import com.bhos.ticketbackend.entity.Employee;
import com.bhos.ticketbackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findFirstByTitle(String title);
    List<Role> findAllByTitleIn(List<String> names);

    @Query(value = "SELECT r.role_id, r.title FROM roles as r ORDER BY role_id ASC", nativeQuery = true)
    List<Role> findAllByOrderByRoleIdAsc();


    @Query(value = "delete from roles r where r.role_id=:roleId", nativeQuery = true)
    void deleteRoleByRoleId(Integer roleId);

    @Query(value = "select r.* from roles r where r.role_id=:roleId", nativeQuery = true)
    Optional<Role> findRoleByRoleId(Integer roleId);
}
