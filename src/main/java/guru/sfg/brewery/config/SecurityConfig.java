package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlParamAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlParamAuthFilter restUrlParamAuthFilter(AuthenticationManager authenticationManager) {
        RestUrlParamAuthFilter filter = new RestUrlParamAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class).csrf().disable();

        http.addFilterBefore(restUrlParamAuthFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class).csrf().disable();

        ((HttpSecurity)
                ((HttpSecurity)
                        ((AuthorizedUrl)
                                ((HttpSecurity)
                                        http.authorizeRequests(auth -> {
                                            auth
                                                    .antMatchers("/h2-console/**")
                                                        .permitAll() //do not use in production!
                                                    .antMatchers("/", "/webjars/**", "/login", "/resources/**")
                                                        .permitAll()
                                                    .antMatchers(HttpMethod.GET, "/api/v1/beer/**")
                                                        .hasAnyRole("ADMIN", "CUSTOMER", "USER")
//                                                    .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**")
//                                                        .hasRole("ADMIN")
                                                    .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}")
                                                        .hasAnyRole("ADMIN", "CUSTOMER", "USER")
                                                    .mvcMatchers("/brewery/breweries")
                                                        .hasAnyRole("ADMIN", "CUSTOMER")
                                                    .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries")
                                                        .hasAnyRole("ADMIN", "CUSTOMER")
                                                    .mvcMatchers("/beers/find", "/beers/{beerId}")
                                                        .hasAnyRole("ADMIN", "CUSTOMER", "USER");                                        })
                                )
                                .authorizeRequests().anyRequest()
                        )
                        .authenticated().and()
                )
                .formLogin().and()
        )
        .httpBasic();

        // h2 console config
        http.headers().frameOptions().sameOrigin();
    }

//    @Bean
//    @Override
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


    @Bean
    PasswordEncoder passwordEncoder() {
//  1      return NoOpPasswordEncoder.getInstance();
//  2      return new LdapShaPasswordEncoder();
//  3      return new StandardPasswordEncoder(); // <- SHA-256
//  4      return new BCryptPasswordEncoder();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


//    @Override
    protected void configureInMemory(AuthenticationManagerBuilder auth) throws Exception {
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
