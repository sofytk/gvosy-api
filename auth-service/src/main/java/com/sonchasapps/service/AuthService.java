package com.sonchasapps.service;

import com.sonchasapps.dto.AuthResponce;
import com.sonchasapps.dto.LogInRequest;
import com.sonchasapps.dto.MyException;
import com.sonchasapps.dto.RegisterRequest;
import com.sonchasapps.model.UserEntity;
import com.sonchasapps.model.UserEntityResponse;
import com.sonchasapps.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Getter
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthResponce register(RegisterRequest request) {
        long start = System.currentTimeMillis();
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new MyException("Email is already exist");
        }

        UserEntity user = new UserEntity(request.getEmail(), request.getName(), passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        long t1 = System.currentTimeMillis();
        System.out.println("AuthService register: t1 " + (t1 - start));


        String token = jwtService.generateToken(user.getEmail());
        long t2 = System.currentTimeMillis();
        System.out.println("AuthService register: t2 " + (t2 - t1));


        UserEntityResponse userEntityResponse = new UserEntityResponse(user.getName(), token, user.getIsPremium());
        long t3 = System.currentTimeMillis();
        System.out.println("AuthService register: t3 " + (t3 - t2));

        long end = System.currentTimeMillis();
        System.out.println("TIME AuthService: " + (end - start) + " ms");

        return new AuthResponce(token, userEntityResponse);
    }

    public AuthResponce login(LogInRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Неверный пароль");
        }

        String token = jwtService.generateToken(user.getEmail());
        UserEntityResponse userEntityResponse = new UserEntityResponse(user.getName(), token, user.getIsPremium());
        return new AuthResponce(token, userEntityResponse);
    }
}



