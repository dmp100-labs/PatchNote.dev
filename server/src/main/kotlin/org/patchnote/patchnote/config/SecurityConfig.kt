package org.patchnote.patchnote.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }  // 개발 중에는 CSRF 비활성화
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/**").permitAll()  // 모든 API 허용
                    .requestMatchers("/actuator/**").permitAll()  // Actuator 허용
                    .requestMatchers("/h2-console/**").permitAll()  // H2 콘솔 허용
                    .anyRequest().permitAll()  // 나머지도 모두 허용
            }
            .headers { headers ->
                headers.frameOptions { it.sameOrigin() }  // H2 콘솔용
            }

        return http.build()
    }
}