package com.example.SEENEMA.mainPage.service;

import com.example.SEENEMA.mainPage.dto.MainPageDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MainPageService {
   MainPageDto.responseDTO readRanking();
}
