package com.example.SEENEMA.mainPage.service;

import com.example.SEENEMA.mainPage.dto.MainPageDto;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import org.jsoup.nodes.Document;
import javax.transaction.Transactional;
import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MainPageServiceImpl implements MainPageService{
    private final String playDBRanking = "http://www.playdb.co.kr/ranking/Ticket/TPBoxOffice.asp?KindOfGoods=01011&Flag=D";
    @Override
    public MainPageDto.readRanking readRanking() {
        // 타겟 사이트에 연결 후 html 파일 읽어오기
        Connection connection = Jsoup.connect(playDBRanking);
        try{
            Document doc = connection.get();
            MainPageDto.readRanking response = getRankString(doc);
        }catch(IOException e){
        }
        return null;
    }

    // 랭킹 정보 읽어온 후, 알맞게 MainPageDto.readRanking으로 변환 후 return
    private MainPageDto.readRanking getRankString(Document doc){

        return null;
    }
}
