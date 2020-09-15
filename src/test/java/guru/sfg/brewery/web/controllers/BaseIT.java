package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Created by jt on 6/13/20.
 */
public abstract class BaseIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    BeerRepository beerRepository;

    protected MockMvc mockMvc;
    protected Beer beerToTest;

    @BeforeEach
    public void setup() {
        Random random = new Random();

        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();

        beerToTest = beerRepository.saveAndFlush(Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(random.nextInt(99999999)))
                    .build());
    }

    public static Stream<Arguments> getStreamAllUsers() {
        return Stream.of(Arguments.of("spring" , "guru"),
                Arguments.of("scott", "tiger"),
                Arguments.of("user", "password"));
    }

    public static Stream<Arguments> getStreamAdminCustomer() {
        return Stream.of(Arguments.of("spring" , "guru"),
                Arguments.of("scott", "tiger"));
    }

    public static Stream<Arguments> getStreamAdmin() {
        return Stream.of(Arguments.of("spring" , "guru"));
    }

    public static Stream<Arguments> getStreamNotAdmin() {
        return Stream.of(Arguments.of("scott", "tiger"),
                Arguments.of("user", "password"));
    }
}
