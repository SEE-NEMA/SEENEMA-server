package com.example.SEENEMA.theater.service;

import com.example.SEENEMA.theater.dto.TheaterDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TheaterService {
    List<TheaterDto.theaterResponse> searchTheater(String theaterName);
    List<TheaterDto.theaterList> getTheater();
}
