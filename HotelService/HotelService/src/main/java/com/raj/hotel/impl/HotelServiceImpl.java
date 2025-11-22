package com.raj.hotel.impl;

import com.raj.hotel.entities.Hotel;
import com.raj.hotel.exceptions.ResourceNotFoundException;
import com.raj.hotel.repositories.HotelRepository;
import com.raj.hotel.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public Hotel createHotel(Hotel hotel) {
        String randomHotelId = UUID.randomUUID().toString();
        hotel.setHotelId(randomHotelId);
        return hotelRepository.save(hotel);
    }

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel getHotelById(String hotelId) {
        return hotelRepository.findById(hotelId).orElseThrow(()->new ResourceNotFoundException("Hotel with given id is not found !! "+hotelId));
    }

    @Override
    public Hotel updateHotel(String hotelId, Hotel hotel) {
        Hotel existingHotel = getHotelById(hotelId);
        existingHotel.setHotelName(hotel.getHotelName());
        existingHotel.setHotelAbout(hotel.getHotelAbout());
        existingHotel.setHotelLocation(hotel.getHotelLocation());
        // Add other fields as needed
        return hotelRepository.save(existingHotel);
    }

    @Override
    public void deleteHotel(String hotelId) {
        Hotel existingHotel = getHotelById(hotelId);
        hotelRepository.delete(existingHotel);
    }


}
