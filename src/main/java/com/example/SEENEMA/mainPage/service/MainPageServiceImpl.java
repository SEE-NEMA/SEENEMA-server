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
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MainPageServiceImpl implements MainPageService{
    private final String playDBRanking = "http://www.playdb.co.kr/ranking/TotalRanking.asp";
    @Override
    public List<MainPageDto.readRanking> readRanking() {
        // 타겟 사이트에 연결 후 html 파일 읽어오기
        Connection connection = Jsoup.connect(playDBRanking);
        List<MainPageDto.readRanking> response = new ArrayList<>();
        try{
            Document doc = connection.get();
            response = getRankString(doc);
        }catch(IOException e){
        }
        return response;
    }

    // 랭킹 정보 읽어온 후, 알맞게 MainPageDto.readRanking으로 변환 후 return
    // Long rank, String title
    private List<MainPageDto.readRanking> getRankString(Document doc){
        int index = 0;
        Elements divBody = doc.select("div.container1 table tbody tr td table tbody tr td");
        Element musicalRankTD = null, concertRankTD = null;
        // 뮤지컬, 콘서트 랭킹 td 뽑아내기
        for(Element e : divBody){
            if(index == 0) musicalRankTD = e;
            else if(index == 2) concertRankTD = e;
            index++;
        }

        if(musicalRankTD != null){

        }

        return null;
    }
}
