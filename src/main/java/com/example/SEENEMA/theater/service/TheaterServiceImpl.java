package com.example.SEENEMA.theater.service;

import com.example.SEENEMA.theater.dto.TheaterDto;
import com.example.SEENEMA.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TheaterServiceImpl implements TheaterService{

    private final TheaterRepository theaterRepository;
    @Override
    @Transactional(readOnly = true)
    public List<TheaterDto.theaterResponse> searchTheater(String theaterName){
        return theaterRepository.findByTheaterNameContaining(theaterName).stream()
                .map(TheaterDto.theaterResponse::new)
                .collect(Collectors.toList());
    }
}
