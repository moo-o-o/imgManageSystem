package com.huqz.config;

import com.alibaba.fastjson.JSON;
import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.model.User;
import com.huqz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/register");
        web.ignoring().antMatchers("/send_reg_mail");
        web.ignoring().antMatchers("/reset");
        web.ignoring().antMatchers("/send_forget_mail");
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/imgs/view/**");
        web.ignoring().antMatchers("/imgs/thumb/**");
        web.ignoring().antMatchers("/imgs/avatar/**");
        web.ignoring().antMatchers("/share/query/**");
        web.ignoring().antMatchers("/fapi/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").access("hasAnyRole('ADMIN', 'USER')")
                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(((request, response, authException) -> {
                    Result r = ResultGenerator.fail(ResultCode.UNAUTHORIZED, "?????????");
                    response.setContentType("application/json;charset=utf-8");
                    String s = JSON.toJSONString(r);
                    response.getWriter().write(s);
                }))
                .and()
                .formLogin().loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(((request, response, authentication) -> {
                    User principal = (User) authentication.getPrincipal();
                    System.out.println(principal);
                    response.setContentType("application/json;charset=utf-8");
                    Result ok = ResultGenerator.ok(principal);
                    response.getWriter().write(JSON.toJSONString(ok));
                }))
                .failureHandler(((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    String msg;
                    if (exception instanceof LockedException) {
                        msg = "?????????????????????????????????";
                    }else if (exception instanceof BadCredentialsException) {
                        msg = "??????????????????????????????????????????";
                    }else if (exception instanceof DisabledException) {
                        msg = "?????????????????????????????????";
                    }else if (exception instanceof AccountExpiredException){
                        msg = "?????????????????????????????????";
                    }else if (exception instanceof CredentialsExpiredException) {
                        msg = "?????????????????????????????????";
                    }else {
                        msg = "???????????????";
                    }
                    Result fail = ResultGenerator.fail(ResultCode.PASSWORD_ERROR, msg);
                    response.getWriter().write(JSON.toJSONString(fail));
                }))
                .permitAll()
                .and()
                .rememberMe()
                .rememberMeParameter("keepLogged")
                .tokenValiditySeconds(7 * 24 * 60 * 60)
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userService)
                .and()
                .logout().logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .addLogoutHandler(((request, response, authentication) -> {

                }))
                .logoutSuccessHandler(((request, response, authentication) -> {
                    Result ok = ResultGenerator.ok();
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(JSON.toJSONString(ok));
                }))
                .and()
                .exceptionHandling()
                .accessDeniedHandler(((request, response, accessDeniedException) -> {
                    Result result = ResultGenerator.fail(ResultCode.FORBIDDEN, "????????????");
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(JSON.toJSONString(result));
                }))
                .and()
                .csrf().disable();
    }

    @Bean
    PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

}
