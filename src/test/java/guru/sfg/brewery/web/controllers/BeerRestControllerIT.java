package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+beerToTest.getId())
                .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerWrongRole() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+beerToTest.getId())
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+beerToTest.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/")
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"+beerToTest.getId())
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUPC() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/"+beerToTest.getUpc())
                .with(httpBasic("user", "password")))
                .andExpect(status().isOk());
    }


    @Test
    void deleteBeerUrlParam() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+beerToTest.getId())
                .param("Api-Key", "spring")
                .param("Api-Secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerUrlParamWrongCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+beerToTest.getId())
                .param("Api-Key", "spring")
                .param("Api-Secret", "guru1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+beerToTest.getId())
                .header("Api-Key", "spring")
                .header("Api-Secret", "guru"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBeerWrongCredentials() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/"+beerToTest.getId())
                .header("Api-Key", "spring")
                .header("Api-Secret", "guru1"))
                .andExpect(status().isUnauthorized());
    }

}
