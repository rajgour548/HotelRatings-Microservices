package com.raj.rating.services;

import com.raj.rating.entities.Rating;

import java.util.List;

public interface RatingService {
    //create ratings
    Rating createRatings(Rating rating);

    //get all ratings
    List<Rating> getAllRatings();

    //get ratings by userId
    List<Rating> getRatingByUserId(String userId);

    //get ratings by hotelId
    List<Rating> getRatingByHotelId(String hotelId);


}
