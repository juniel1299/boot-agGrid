package com.aggrid.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    //암호 인코더
    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()); //모든 요청에 대한 접근 허용(개발 중)

//        //URI 허가
//        http.authorizeHttpRequests( auth -> auth
//                .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/plugins/**").permitAll()
//                .requestMatchers("/blog", "/login").permitAll()
//                .requestMatchers("/signup", "/signupok").permitAll()
//                .requestMatchers("/blog/board/**").hasRole("MEMBER") //"ROLE_" 알아서 추가
//                .anyRequest().authenticated() //나머지 경로 > 인증 사용자에게만 허가
//        );

        //CSRF 공격을 막기 위해 토큰 등록
        //CSRF 비활성화
        http.csrf(auth -> auth.disable());

        //커스텀 로그인 페이지
        http.formLogin(auth -> auth
                .loginPage("/login")
                .loginProcessingUrl("/loginok")
                .defaultSuccessUrl("/", true)
                .permitAll()
        );

        //로그아웃
//        http.logout(auth -> auth.logoutUrl("/logout")
//                .logoutSuccessUrl("/blog"));

        return http.build();
    }

}

