package com.bhos.ticketbackend.serviceImpl;

import com.bhos.ticketbackend.dao.RoleRepository;
import com.bhos.ticketbackend.dto.AuthenticationRequest;
import com.bhos.ticketbackend.auth.AuthenticationResponse;
import com.bhos.ticketbackend.auth.RegisterRequest;
import com.bhos.ticketbackend.config.JwtService;
import com.bhos.ticketbackend.dto.EmployeeDTO;
import com.bhos.ticketbackend.dto.ResponseDTO;
import com.bhos.ticketbackend.dto.RoleRequestDto;
import com.bhos.ticketbackend.entity.Employee;
import com.bhos.ticketbackend.entity.Role;
import com.bhos.ticketbackend.exception.CustomMessageException;
import com.bhos.ticketbackend.exception.UserEmailUniqueException;
import com.bhos.ticketbackend.service.IEmployeeService;
import com.bhos.ticketbackend.token.Token;
import com.bhos.ticketbackend.dao.TokenRepository;
import com.bhos.ticketbackend.token.TokenType;
import com.bhos.ticketbackend.dao.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Override
    public ResponseDTO signUp(RegisterRequest request) {
        this.userRequestValidation(request);
        List<Role> reqRoles = roleRepository.findAllByTitleIn(request.getRoles());
        var user = Employee.builder()
                .first_name(request.getFirstname())
                .last_name(request.getLastname())
                .username(request.getUsername())
                .status(request.getStatus())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(reqRoles)
                .build();

        //check whether the user exists in the database by email
        Integer emp_id = repository.getEmpIdByEmail(request.getEmail());
        String mesg = "(email)=(" + request.getEmail() + ") already exists.";

        String jwtToken = "";

        if (emp_id != null) {
            throw new UserEmailUniqueException(mesg);
        } else {

            var savedUser = repository.save(user);
            //logger.info("token geldimi: {}", user);
            jwtToken = jwtService.generateToken(user);

            saveUserToken(savedUser, jwtToken);
        }


        return ResponseDTO.of(jwtToken, "Operation success");
    }

    @Override
    public AuthenticationResponse signIn(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        //get employee id by email
        int emp_id = repository.getEmpIdByEmail(request.getEmail());

        var jwtToken = jwtService.generateToken((UserDetails) user);
        EmployeeDTO employeeDTO = new EmployeeDTO().builder()

                .firstname(user.getFirst_name())
                .lastname(user.getLast_name())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();

        revokeAllUserTokens(emp_id);

        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .user(employeeDTO)
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(Employee employee, String jwtToken) {
        var token = Token.builder()
                .user(employee)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Integer emp_id) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(emp_id);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void userRequestValidation(RegisterRequest userRequest) {

        // TODO: 3/4/23 password must be not null or blank
        if(ObjectUtils.isEmpty(userRequest.getPassword())) {
            throw new CustomMessageException("Password can't be blank or null",
                    String.valueOf(HttpStatus.BAD_REQUEST));
        }

        // TODO: 3/4/23 check role valid request
        List<String> roles = roleRepository.findAll().stream().map(Role::getTitle).toList();
        for (var role : userRequest.getRoles()) {
            if (!roles.contains(role)) {
                throw new CustomMessageException("Role is invalid request.",
                        String.valueOf(HttpStatus.BAD_REQUEST));
            }

        }

    }
}