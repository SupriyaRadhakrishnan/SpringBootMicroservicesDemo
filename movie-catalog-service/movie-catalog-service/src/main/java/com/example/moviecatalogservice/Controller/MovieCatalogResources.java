package com.example.moviecatalogservice.Controller;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.moviecatalogservice.model.CatalogItem;
import com.example.moviecatalogservice.model.Movie;
import com.example.moviecatalogservice.model.Rating;
import com.example.moviecatalogservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResources {
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	 @RequestMapping("/{userId}")
	    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

	        UserRating userRating = restTemplate.getForObject("http://rating-data-service/ratingsdata/user/" + userId, UserRating.class);

	        return userRating.getRatings().stream()
	                .map(rating -> {
	                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
	                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
	                })
	                .collect(Collectors.toList());

	    }
	}

	/*
	Alternative WebClient way
	Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
	.retrieve().bodyToMono(Movie.class).block();
	*/