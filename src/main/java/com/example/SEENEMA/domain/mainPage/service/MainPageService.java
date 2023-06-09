package com.example.SEENEMA.domain.mainPage.service;

import com.example.SEENEMA.domain.mainPage.dto.MainPageDto;
import com.example.SEENEMA.domain.mainPage.dto.PlayDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MainPageService {
    List<PlayDto.musicalList> getMusicals();
    List<PlayDto.concertList> getConcerts();

}
