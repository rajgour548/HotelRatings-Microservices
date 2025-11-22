package com.raj.user.service.external.services;

import com.raj.user.service.entities.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "HOTELSERVICE")
public interface HotelService {
@GetMapping("/hotels/{hotelId}")
Hotel getHotel(@PathVariable("hotelId") String hotelId, @RequestHeader("Authorization") String token);
}
