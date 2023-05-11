package com.bhos.ticketbackend.serviceImpl;

import com.bhos.ticketbackend.dto.AuthenticationRequest;
import com.bhos.ticketbackend.auth.AuthenticationResponse;
import com.bhos.ticketbackend.auth.RegisterRequest;
import com.bhos.ticketbackend.config.JwtService;
import com.bhos.ticketbackend.dto.EmployeeDTO;
import com.bhos.ticketbackend.dto.ResponseDTO;
import com.bhos.ticketbackend.dto.RoleRequestDto;
import com.bhos.ticketbackend.entity.Employee;
import com.bhos.ticketbackend.exception.UserEmailUniqueException;
import com.bhos.ticketbackend.service.IEmployeeService;
import com.bhos.ticketbackend.token.Token;
import com.bhos.ticketbackend.dao.TokenRepository;
import com.bhos.ticketbackend.token.TokenType;
import com.bhos.ticketbackend.dao.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements IEmployeeService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseDTO signUp(RegisterRequest request) {
        var user = Employee.builder()
                .first_name(request.getFirstname())
                .last_name(request.getLastname())
                .username(request.getUsername())
                .status(request.getStatus())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        //Check user exist in databvase by given email
        Integer emp_id = repository.getEmpIdByEmail(request.getEmail());
        String mesg = "(email)=("+request.getEmail()+") already exists.";

        String jwtToken = "";
        logger.info("gelen emp_id: {}", emp_id);

        if(emp_id != null){
            throw  new UserEmailUniqueException(mesg);
        }else {

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
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

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
}
