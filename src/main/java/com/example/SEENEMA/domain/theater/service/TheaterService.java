package com.example.SEENEMA.domain.theater.service;

import com.example.SEENEMA.domain.theater.dto.TheaterDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TheaterService {
    List<TheaterDto.theaterResponse> searchTheater(String theaterName);
    List<TheaterDto.theaterList> getTheater();
    List<TheaterDto.theaterResponse> searchSeatTheater(String theaterName);
    List<TheaterDto.theaterList> getSeatTheater();
}
