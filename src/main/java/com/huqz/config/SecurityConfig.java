package com.huqz.config;

import com.alibaba.fastjson.JSON;
import com.huqz.core.Result;
import com.huqz.core.ResultCode;
import com.huqz.core.ResultGenerator;
import com.huqz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

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
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/imgs/view/**");
        web.ignoring().antMatchers("/share/query/**");
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
                    Result r = ResultGenerator.fail(ResultCode.UNAUTHORIZED, "未登录");
                    response.setContentType("application/json;charset=utf-8");
                    String s = JSON.toJSONString(r);
                    response.getWriter().write(s);
                }))
                .and()
                .formLogin().loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(((request, response, authentication) -> {
                    Object principal = authentication.getPrincipal();
                    response.setContentType("application/json;charset=utf-8");
                    Result ok = ResultGenerator.ok(principal);
                    response.getWriter().write(JSON.toJSONString(ok));
                }))
                .failureHandler(((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    String msg;
                    if (exception instanceof LockedException) {
                        msg = "账户被锁定，登陆失败！";
                    }else if (exception instanceof BadCredentialsException) {
                        msg = "账户名或密码错误，登陆失败！";
                    }else if (exception instanceof DisabledException) {
                        msg = "账户被禁用，登陆失败！";
                    }else if (exception instanceof AccountExpiredException){
                        msg = "账户已过期，登陆失败！";
                    }else if (exception instanceof CredentialsExpiredException) {
                        msg = "密码已过期，登陆失败！";
                    }else {
                        msg = "登陆失败！";
                    }
                    Result fail = ResultGenerator.fail(ResultCode.UNAUTHORIZED, msg);
                    response.getWriter().write(JSON.toJSONString(fail));
                }))
                .permitAll()
                .and()
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenValiditySeconds(3600)
                .tokenRepository(persistentTokenRepository())
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
                    Result result = ResultGenerator.fail(ResultCode.FORBIDDEN, "权限不足");
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
