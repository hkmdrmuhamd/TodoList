package com.example.todolistbackend.API;

import com.example.todolistbackend.DTOs.request.CreateUserRequest;
import com.example.todolistbackend.DTOs.request.LoginRequest;
import com.example.todolistbackend.DTOs.response.AuthResponse;
import com.example.todolistbackend.DTOs.response.RegisterResponse;
import com.example.todolistbackend.bussines.abstractt.IUserService;
import com.example.todolistbackend.Entity.User;
import com.example.todolistbackend.security.JwtTokenProvider;
import com.example.todolistbackend.utilities.ErrorDataResult;
import com.example.todolistbackend.utilities.SuccesDataResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthControllers {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;


    public AuthControllers(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, IUserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());

        try {
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            String jwtToken = jwtTokenProvider.generateJwtToken(auth);
            User user = userService.getByUserName(loginRequest.getUserName()).getData();

            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(jwtToken);
            authResponse.setRole(user.getRole());

            return ResponseEntity.ok(new SuccesDataResult<>("Login Success", authResponse));
        } catch (AuthenticationException e) {
            AuthResponse authResponse = new AuthResponse();
            ErrorDataResult<AuthResponse> errorDataResult = new ErrorDataResult<>("User name or password is wrong", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDataResult);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest registerRequest) {
        try {
            RegisterResponse registerResponse = new RegisterResponse();

            if (userService.getByUserName(registerRequest.getUserName()).getData() != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDataResult<>("User already exists", null));
            }

            User user = new User();
            user.setUserName(registerRequest.getUserName());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setRole("ROLE_CLIENT");
            userService.createOneUser(user);

            registerResponse.setUserId(user.getId());
            registerResponse.setUserName(user.getUserName());
            registerResponse.setRole(user.getRole());

            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccesDataResult<>("Registration Success", registerResponse));
        } catch (Exception e) {
            RegisterResponse errorResponse = new RegisterResponse();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDataResult<>("",errorResponse));
        }
    }


    @GetMapping("/getUser/{userName}")
    public User getUser(@PathVariable String userName){
        return userService.getByUserName(userName).getData();
    }

    @GetMapping("/current-user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestHeader("Authorization") String token) {
        String cleanToken = token.replace("Bearer ", "");
        Map<String, Object> response = new HashMap<>();

        Integer userId = jwtTokenProvider.getUserIdFromJwt(cleanToken);
        String username = jwtTokenProvider.getUsernameFromJwt(cleanToken);

        response.put("name", username);
        response.put("id", userId);

        return ResponseEntity.ok(response);
    }

}