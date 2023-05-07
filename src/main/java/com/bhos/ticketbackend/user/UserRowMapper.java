package com.bhos.ticketbackend.user;

import com.bhos.ticketbackend.dto.EmployeeDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new EmployeeDTO().builder()
                .emp_id(resultSet.getInt("id"))
                .firstname(resultSet.getString("firstname"))
                .lastname(resultSet.getString("lastname"))
                .email(resultSet.getString("email")).build();
    }
}
