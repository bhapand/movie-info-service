package io.kairos.movieinfoservice.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.kairos.movieinfoservice.models.Movie;
import io.kairos.movieinfoservice.models.external.MovieData;
import io.kairos.movieinfoservice.services.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.view.RedirectView;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    @Value("${movie-api.url}")
    public String movieApiUrl;

    @Value("${movie-api.token}")
    public String apiToken;

    @Autowired
    WebClient webClient;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Mono<Movie> getMovieInfo(long movieId) {
        return populateMovie().filter(movie -> movie.getMovieId() == movieId).next().switchIfEmpty(Mono.just(new Movie(movieId, "Titanic", "2000", "Desc")));
    }


    @Override
    public Flux<Movie> getMovieInfoList() {
        return populateMovie();
    }

    private Flux<Movie> populateMovie() {
        AtomicLong idValue = new AtomicLong(1);
        return doMovieApiCall().map(movieData -> new Movie(idValue.getAndIncrement(), movieData.getName(), movieData.getReleaseDate(), movieData.getDescription()));
    }

    public Flux<MovieData> doMovieApiCall() {
        return webClient.get()
                .uri(URI.create(movieApiUrl + "/titles"))
                .header("Authorization", "Bearer " + apiToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .flatMapMany(apiCallToMovieDataMapper());
    }

    private Function<String, Publisher<? extends MovieData>> apiCallToMovieDataMapper() {
        return response -> {
            try {
                JsonNode data = objectMapper.readTree(response).get("pagination").get("data");
                return Flux.fromIterable(objectMapper.readerForListOf(MovieData.class).readValue(data));
            } catch (IOException e) {
                log.error("Error while parsing movie data response", e);
                return Flux.empty();
            }
        };
    }
}
