package io.kairos.movieinfoservice.client;

import io.kairos.movieinfoservice.models.external.MovieData;
import io.kairos.movieinfoservice.services.impl.MovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MovieServiceTest {

    public static final String TITLES_URL = "http://localhost:9002/movie-info-service/api/info/1";

    MovieServiceImpl movieService = new MovieServiceImpl();

    private WebClient webClient = WebClient.builder().build();

    @Test
    public void testInfoCall() {
        /*Flux<MovieData> movieDataFlux = movieService.doMovieApiCall();

        StepVerifier.create(movieDataFlux)
                .expectNextCount(15);*/


    }
}
