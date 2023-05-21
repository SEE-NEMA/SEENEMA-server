package com.example.SEENEMA.domain.theater.service;

import com.example.SEENEMA.domain.theater.repository.TheaterRepository;
import com.example.SEENEMA.domain.theater.domain.Theater;
import com.example.SEENEMA.domain.theater.dto.TheaterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Override
    public List<TheaterDto.theaterList> getTheater() {
        List<Theater> theaters = theaterRepository.findAll();
        List<TheaterDto.theaterList> theaterLists = new ArrayList<>();
        for (Theater theater : theaters) {
            TheaterDto.theaterList theaterList = new TheaterDto.theaterList(theater);
            theaterLists.add(theaterList);
        }
        return theaterLists;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TheaterDto.theaterResponse> searchSeatTheater(String theaterName){
        return theaterRepository.findByTheaterNameContaining(theaterName).stream()
                .map(TheaterDto.theaterResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TheaterDto.theaterList> getSeatTheater() {
        List<Theater> theaters = theaterRepository.findAll();
        List<TheaterDto.theaterList> theaterLists = new ArrayList<>();
        for (Theater theater : theaters) {
            TheaterDto.theaterList theaterList = new TheaterDto.theaterList(theater);
            theaterLists.add(theaterList);
        }
        return theaterLists;
    }
}
