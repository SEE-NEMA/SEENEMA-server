package com.example.SEENEMA.mainPage.service;

import com.example.SEENEMA.mainPage.dto.MainPageDto;
import org.springframework.stereotype.Service;

@Service
public interface MainPageService {
    MainPageDto.readRanking readRanking();
}
