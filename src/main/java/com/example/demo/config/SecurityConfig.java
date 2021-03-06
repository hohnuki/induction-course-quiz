package com.example.demo.config;


import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //for h2 console 本番では削除
        http.authorizeRequests().antMatchers("/fonts/**","/style/**").permitAll();
//        http.authorizeRequests().antMatchers("/h2-console/**","/fonts/**", "/style/**").permitAll()
//                        .and()
//                                .csrf().ignoringAntMatchers("/h2-console/**")
//                        .and()
//                                .headers().frameOptions().disable();
//        super.configure(http);

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //
                .logoutUrl("/logout") //ログアウトのURL
                .logoutSuccessUrl("/login")
                .and()
                .csrf()
                .disable();


        http.authorizeRequests()
                .mvcMatchers("/login/**").permitAll()
                .mvcMatchers("/users/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
