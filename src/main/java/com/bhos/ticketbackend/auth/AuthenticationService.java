package com.bhos.ticketbackend.auth;


import com.bhos.ticketbackend.config.JwtService;
import com.bhos.ticketbackend.dto.ResponseDTO;
import com.bhos.ticketbackend.token.Token;
import com.bhos.ticketbackend.token.TokenRepository;
import com.bhos.ticketbackend.token.TokenType;
import com.bhos.ticketbackend.entity.Employee;
import com.bhos.ticketbackend.dto.EmployeeDTO;
import com.bhos.ticketbackend.user.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.NonUniqueResultException;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EmployeeRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public ResponseDTO register(RegisterRequest request) {
        var user = Employee.builder()
                .first_name(request.getFirstname())
                .last_name(request.getLastname())
                .username(request.getUsername())
                .status(request.getStatus())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

            var savedUser = repository.save(user);
            //logger.info("token geldimi: {}", user);
            var jwtToken = jwtService.generateToken(user);

            saveUserToken(savedUser, jwtToken);


        return ResponseDTO.of(jwtToken, "Operation success");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        //get employee id by email
        int emp_id = repository.getEmpIdByEmail(request.getEmail());

        var jwtToken = jwtService.generateToken((UserDetails) user);
        EmployeeDTO employeeDTO = new EmployeeDTO().builder()

                .firstname(user.getFirst_name())
                .lastname(user.getLast_name())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
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
}