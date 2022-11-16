package lk.directpay.task.services;



import lk.directpay.task.configurations.PasswordConfiguration;
import lk.directpay.task.filters.AppValidationFilter;
import lk.directpay.task.filters.JWTRequestFilter;
import lk.directpay.task.filters.RequestDataLogFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    JWTRequestFilter jwtRequestFilter;

    @Autowired
    AppValidationFilter appValidationFilter;

    @Autowired
    RequestDataLogFilter requestDataLogFilter;

    @Autowired
    AuthUserDetailsService userDetailsService;

    private final PasswordConfiguration passwordConfiguration;

    public SecurityConfiguration(PasswordConfiguration passwordConfiguration) {
        this.passwordConfiguration = passwordConfiguration;
    }

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordConfiguration.getEncoder());
    }

    //    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
            //    .antMatchers("/api/**").hasAuthority("mobile_api_access")
//                .antMatchers("/dashboard/**").hasAuthority("merchant_api_access")
//                .authenticated()
                .anyRequest()
                .permitAll()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
                // http.addFilterBefore(appValidationFilter,JWTRequestFilter.class);
                // http.addFilterAfter(requestDataLogFilter,AppValidationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
