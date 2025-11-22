package com.raj.hotel.services;

import com.raj.hotel.entities.Hotel;

import java.util.List;

public interface HotelService {

    Hotel createHotel(Hotel hotel);

    List<Hotel> getAllHotels();

    Hotel getHotelById(String hotelId);

    Hotel updateHotel(String hotelId, Hotel hotel);
    void deleteHotel(String hotelId);

}
