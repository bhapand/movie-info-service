package io.kairos.movieinfoservice.resouces;

import io.kairos.movieinfoservice.models.Movie;
import io.kairos.movieinfoservice.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
@RequestMapping(value = "info")
public class MovieResource {

    @Autowired
    MovieService movieService;

    @GetMapping(value = "{movieId}")
    public Mono<Movie> getMovieInfo(@PathVariable(value = "movieId") long movieId) {
        return movieService.getMovieInfo(movieId).log();
    }

    @GetMapping(value = "onebot")
    public Mono<Void> oneBot(ServerWebExchange serverWebExchange) {
        serverWebExchange.getResponse().getHeaders().setLocation(URI.create("https://www.google.com"));
        serverWebExchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
        return serverWebExchange.getResponse().setComplete();
    }

    @GetMapping
    public Flux<Movie> getMovieInfoList() {
        return movieService.getMovieInfoList();
    }

    @Bean
    public RouterFunction<ServerResponse> htmlRouter(
            @Value("classpath:/public/index.html") Resource html) {
        return route(GET("/"), request
                -> ok().contentType(MediaType.TEXT_HTML).syncBody(html)
        );
    }
}


