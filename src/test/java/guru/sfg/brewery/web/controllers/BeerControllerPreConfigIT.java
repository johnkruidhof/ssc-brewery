package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@WebMvcTest
@SpringBootTest
@TestPropertySource(locations="classpath:application.properties")
public class BeerControllerPreConfigIT extends BaseIT {

    @Value("${spring.security.user.name}")
    private String userName;

    @Value("${spring.security.user.password}")
    private String password;

    @WithMockUser("randomtestuser") // <- niet echte auth maar aangeven dat spring er vanuit mag gaan dat een user is ingelogd
    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Disabled("in Config is permitAll aangezet")
    @Test
    void findBeersUnauthorized() throws Exception {
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeersWithHttpBasic() throws Exception {
        mockMvc.perform(get("/beers/find")
                .with(httpBasic(userName, password)))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }

    @Disabled("in Config is permitAll aangezet")
    @Test
    void findBeersWithAnonymous() throws Exception {
        mockMvc.perform(get("/beers/find")
                .with(anonymous()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeersWithHttpBasicWrongCredentials() throws Exception {
        mockMvc.perform(get("/beers/find")
                .with(httpBasic(userName, "password")))
                .andExpect(status().isUnauthorized());
    }
}
