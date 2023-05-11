package com.bhos.ticketbackend.row_mappers;

import com.bhos.ticketbackend.dto.EmployeeDTO;
import com.bhos.ticketbackend.entity.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class EmployeeRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Set<Role> roles = new HashSet<>();
        return new EmployeeDTO().builder()
                .emp_id(resultSet.getInt("emp_id"))
                .firstname(resultSet.getString("first_name"))
                .lastname(resultSet.getString("last_name"))
                .email(resultSet.getString("email"))
                .build();

    }
}
