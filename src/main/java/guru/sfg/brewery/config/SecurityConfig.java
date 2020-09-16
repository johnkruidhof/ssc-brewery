package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import guru.sfg.brewery.security.google.Google2faFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by jt on 6/13/20.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PersistentTokenRepository persistentTokenRepository;
    private final Google2faFilter google2faFilter;

    //
    // needed for use with Spring Data JPA SPeL
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

//    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
//        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }
//
//    public RestUrlParamAuthFilter restUrlParamAuthFilter(AuthenticationManager authenticationManager) {
//        RestUrlParamAuthFilter filter = new RestUrlParamAuthFilter(new AntPathRequestMatcher("/api/**"));
//        filter.setAuthenticationManager(authenticationManager);
//        return filter;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.addFilterBefore(google2faFilter, SessionManagementFilter.class);

//        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
//                UsernamePasswordAuthenticationFilter.class).csrf().disable();
//
//        http.addFilterBefore(restUrlParamAuthFilter(authenticationManager()),
//                UsernamePasswordAuthenticationFilter.class).csrf().disable();

        http.cors()
            .and()
            .authorizeRequests(authorize -> {
                authorize
                        .antMatchers("/h2-console/**").permitAll() //do not use in production!
                        .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
            } )
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin(loginConfigurer -> {
                loginConfigurer
                        .loginProcessingUrl("/login")
                        .loginPage("/").permitAll()
                        .successForwardUrl("/")
                        .defaultSuccessUrl("/")
                        .failureUrl("/?error");
            })
             .logout(logoutConfigurer -> {
                 logoutConfigurer
                         .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                         .logoutSuccessUrl("/?logout")
                         .permitAll();
             })
            .httpBasic()
            .and().csrf().ignoringAntMatchers("/h2-console/**", "/api/**")
            .and().rememberMe()
//                .tokenRepository(persistentTokenRepository)
                .key("sfg-key")
                .userDetailsService(userDetailsService);

            //h2 console config
            http.headers().frameOptions().sameOrigin();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Hieronder oude niet gebruikte methodes voor referentie
     */

//    @Override
//      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       // auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());

//        auth.inMemoryAuthentication()
//                .withUser("spring")
//                .password("{bcrypt}$2a$10$7tYAvVL2/KwcQTcQywHIleKueg4ZK7y7d44hKyngjTwHCDlesxdla")
//                .roles("ADMIN")
//                .and()
//                .withUser("user")
//                .password("{sha256}1296cefceb47413d3fb91ac7586a4625c33937b4d3109f5a4dd96c79c46193a029db713b96006ded")
//                .roles("USER");
//
//        auth.inMemoryAuthentication().withUser("scott").password("{bcrypt15}$2a$15$baOmQtw8UqWZRDQhMFPFj.xhkkWveCTQHe4OBdr8yw8QshejiSbI6").roles("CUSTOMER");
  //  }

    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }


//    @Bean
    PasswordEncoder passwordEncoderWhileInMemory() {
//  1      return NoOpPasswordEncoder.getInstance();
//  2      return new LdapShaPasswordEncoder();
//  3      return new StandardPasswordEncoder(); // <- SHA-256
//  4      return new BCryptPasswordEncoder();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    //    @Override
    protected void configureWhileInMemory(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
//  0              .password("{noop}guru")
//  1              .password("guru")
//  2              .password("{SSHA}NibT99gVRw59S749DDkk6viSUcEDCxIennLCPA==")
//  3              .password("c8d470558ff8a6d9aaecf4f858da05c4b6c10131d62fbb04dbff6c7d3ab6e533314cd8d6d9e8e90e")
//  4              .password("$2a$10$pwpTuOy2xCaD9e0cHm0lkuJPjqQUQGeSSIZ8GEOMutWDzwnfQaQuO")
                .password("{bcrypt}$2a$10$5h96ZCRkXNl3R/kvtZu1tetgVhUpS4Ndnwt3IiVoQxfUkPOcYs1au")
                .roles("ADMIN")
                .and()
                .withUser("user")
//  0              .password("{noop}password")
//  1              .password("password")
//  2              .password("{SSHA}6/FL26HDKijB4ZfrFsFraXVCnjGSpi3N52q0zQ==")
//  3              .password("247bc4c0ffa726e1b5e16a173c95617941e1d34e4acfd1ad1d3fbdff9a6b7144bf59af92e9c89a59")
//  4              .password("$2a$10$pwpTuOy2xCaD9e0cHm0lkuJPjqQUQGeSSIZ8GEOMutWDzwnfQaQuO")
                .password("{sha256}247bc4c0ffa726e1b5e16a173c95617941e1d34e4acfd1ad1d3fbdff9a6b7144bf59af92e9c89a59")
                .roles("USER")
                .and()
                .withUser("scott")
//  0              .password("{noop}tiger")
//  3              .password("5e968aeb6288dc99efb3b4deead212c460c4c8e3c1aa9bd6a7e5c8ffef65e6ffd7c1a9daf5d444d8")
//                .password("{bcrypt15}$2a$15$y0Mb5GIx099Ji/HwrqHEzu8KZxEgmv.jsZpDC1kxxB4qjJv5MghRG")
                .password("{bcrypt10}$2a$10$3SukDpbaTXwJ8ujV1yC3bO3dG4RgwHT0CtuycROiubgiXAWlugsFS")
                .roles("CUSTOMER");
    }
}
