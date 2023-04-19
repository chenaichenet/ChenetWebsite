package pers.website.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Spring Security配置
 *
 * @author ChenetChen
 * @since 2023/3/7 15:35
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig{
    
    @Bean
    public WebSecurityCustomizer customizer() {
        return web -> web.ignoring().anyRequest();
    }
    // todo-chen 配置文件还有问题，springSecurity6.0更新了许多东西
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(author->{
            author
                    .requestMatchers("/","/index").permitAll()
                    .requestMatchers("/resources/**","/static/**").permitAll()
                    .requestMatchers("/user", "/user/**").permitAll();
                }).build();
    }
}
