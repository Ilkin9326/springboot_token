package com.bhos.ticketbackend.controllers;

import com.bhos.ticketbackend.dao.EmployeeRepository;
import com.bhos.ticketbackend.dao.RoleRepository;
import com.bhos.ticketbackend.dto.ResponseDTO;
import com.bhos.ticketbackend.dto.EmployeeDTO;
import com.bhos.ticketbackend.dto.RoleRequestDto;
import com.bhos.ticketbackend.entity.Employee;
import com.bhos.ticketbackend.entity.Role;
import com.bhos.ticketbackend.exception.CustomMessageException;
import com.bhos.ticketbackend.exception.RoleUniqueException;
import com.bhos.ticketbackend.exception.UserEmailUniqueException;
import com.bhos.ticketbackend.row_mappers.EmployeeRowMapper;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
public class EmployeeController {
    private final JdbcTemplate jdbcTemplate;
    private final EmployeeRepository repository;
    private final RoleRepository roleRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseEntity<String> sayHello(Principal principal) {
        return ResponseEntity.ok("Hello from secured endpoint " + principal.getName());
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getUsers() {
        List<EmployeeDTO> listDTo = new ArrayList<>();
        try {
            List<Employee> users = repository.getAllUser();
            if(!users.isEmpty()) {
                for (int i = 0; i < users.size(); i++) {
                    listDTo.add(new EmployeeDTO().builder()
                            .emp_id(users.get(i).getEmp_id())
                            .firstname(users.get(i).getFirst_name())
                            .lastname(users.get(i).getLast_name())
                            .username(users.get(i).getUsername())
                            .email(users.get(i).getEmail())
                            .roles(users.get(i).getRoles())
                            .build()
                    );
                }

            }
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

            var user = repository.findByEmpId(emp_id)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            employeeDTO = new EmployeeDTO().builder()
                    .emp_id(user.getEmp_id())
                    .firstname(user.getFirst_name())
                    .lastname(user.getLast_name())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .roles(user.getRoles())
                    .build();

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

            //Check whether the inserted role exists in DB or not.
            checkRoleUnique(request);

            String sql = "insert into roles(title, description) values(?,?)";
            res = jdbcTemplate.update(sql, new Object[]{request.getTitle(),
                    request.getDescription()});
        }catch (EmptyResultDataAccessException exception){
            logger.info("Sql-den gelen result: {}", exception.getMessage());
            return ResponseEntity.ok(ResponseDTO.of(exception.getMessage(), "Bad Request"));
        }

        return ResponseEntity.ok(ResponseDTO.of(res, "Success response"));
    }

    @GetMapping("/all_roles")
    public ResponseEntity<ResponseDTO> getAllRoles(){
        List<Role> roles = new ArrayList<>();
        roles = roleRepository.findAllByOrderByRoleIdAsc();
        return ResponseEntity.ok(ResponseDTO.of(roles, "Success response"));
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity<ResponseDTO> deleteRoleByRoleId(@PathVariable("id") Integer roleId) {
        var role = roleRepository.findRoleByRoleId(roleId).orElseThrow(()-> new CustomMessageException("Silmek istediyiniz "+roleId+" nomreli role bazada movcud deyil", HttpStatus.BAD_REQUEST.toString()));
        roleRepository.deleteRoleByRoleId(roleId);
        return ResponseEntity.ok(ResponseDTO.of("Success response"));
    }

    private void checkRoleUnique(RoleRequestDto request){
        String sqlUniq = "select count(*) from roles r where r.title = ?";
        int a = jdbcTemplate.queryForObject(sqlUniq, Integer.class,
                request.getTitle());
        String msg = "(title)=("+ request.getTitle() +") already exists.] with root cause";
        if(a>0){
            throw new RoleUniqueException(msg);
        }
    }

}