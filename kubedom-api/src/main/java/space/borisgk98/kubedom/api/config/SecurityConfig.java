package space.borisgk98.kubedom.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import space.borisgk98.kubedom.api.cosnt.AppConst;
import space.borisgk98.kubedom.api.cosnt.AppUrls;
import space.borisgk98.kubedom.api.security.JwtConfigurer;
import space.borisgk98.kubedom.api.security.JwtTokenProvider;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AppConst.SERVER_PREFIX + AppUrls.LOGIN).permitAll()
                .antMatchers(AppConst.SERVER_PREFIX + AppUrls.REGISTER).permitAll()
                .antMatchers(AppConst.SERVER_PREFIX + AppConst.WS + AppUrls.PROVIDER).permitAll()
                .antMatchers(AppConst.SERVER_PREFIX + AppConst.WS + AppUrls.CUSTOMER).permitAll()
                .antMatchers(AppConst.SERVER_PREFIX + "/test").permitAll()
                .anyRequest().authenticated().and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}
