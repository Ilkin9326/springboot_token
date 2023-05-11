package com.bhos.ticketbackend.controllers;

import com.bhos.ticketbackend.dto.ResponseDTO;
import com.bhos.ticketbackend.dto.EmployeeDTO;
import com.bhos.ticketbackend.dto.RoleRequestDto;
import com.bhos.ticketbackend.row_mappers.EmployeeRowMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
public class EmployeeController {
    private final JdbcTemplate jdbcTemplate;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseEntity<String> sayHello(Principal principal) {
        return ResponseEntity.ok("Hello from secured endpoint " + principal.getName());
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getUsers() {
        List<EmployeeDTO> listDTo = null;
        try {
            String sql = "select e.emp_id, e.first_name, e.last_name, e.email, r.title_az as role from employee as e\n" +
                    "left join emp_roles as er on er.emp_id=e.emp_id\n" +
                    "left join roles as r on r.role_id=er.role_id where e.is_active=1";
            listDTo = jdbcTemplate.query(sql, new EmployeeRowMapper());
        }catch (EmptyResultDataAccessException exception){
            exception.printStackTrace();
            logger.info(exception.getMessage());
        }

        return ResponseEntity.ok(ResponseDTO.of(listDTo, "Success response"));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ResponseDTO> getUserInfoById(@PathVariable("id") Integer emp_id){
        EmployeeDTO employeeDTO=null;
        try{
            String sql = "select e.emp_id, e.first_name, e.last_name, e.email, string_agg(r.title_az, ', ') as role from employee as e\n" +
                    "left join emp_roles as er on er.emp_id=e.emp_id\n" +
                    "left join roles as r on r.role_id=er.role_id where e.emp_id = ? and e.is_active=1 group by e.emp_id";
//            employeeDTO = (EmployeeDTO) jdbcTemplate.queryForObject(sql, new UserRowMapper(), emp_id);

            employeeDTO = (EmployeeDTO) jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), emp_id);
        }catch (EmptyResultDataAccessException exception){
            logger.info("Sql-den gelen result: {}", exception.getMessage());
            return ResponseEntity.ok(ResponseDTO.of((employeeDTO == null || employeeDTO.equals("")) ? "No data found" : "null", "Bad Request"));
        }

        return ResponseEntity.ok(ResponseDTO.of(employeeDTO, "Success response"));
    }

    @PostMapping("/role")
    public ResponseEntity<ResponseDTO> addNewRole(
            @RequestBody RoleRequestDto request
    ) {
        int res = 0;
        try {
            String sql = "insert into roles(title_az, title_en, title_ru) values(?,?,?)";
            res = jdbcTemplate.update(sql, new Object[]{request.getTitle_az(),
                    request.getTitle_en(), request.getTitle_ru()});
        }catch (EmptyResultDataAccessException exception){
            logger.info("Sql-den gelen result: {}", exception.getMessage());
            return ResponseEntity.ok(ResponseDTO.of(exception.getMessage(), "Bad Request"));
        }
        return ResponseEntity.ok(ResponseDTO.of(res, "Success response"));
    }

}