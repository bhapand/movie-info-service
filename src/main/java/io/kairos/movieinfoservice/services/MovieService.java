package io.kairos.movieinfoservice.services;

import io.kairos.movieinfoservice.models.Movie;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MovieService {

    public Mono<Movie> getMovieInfo(long movieId);
    public Flux<Movie> getMovieInfoList();
}
