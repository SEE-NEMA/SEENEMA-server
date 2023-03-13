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
    private final String naverMusicalRanking = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=%EB%AE%A4%EC%A7%80%EC%BB%AC+%EB%9E%AD%ED%82%B9";
    private final String naverConcertRanking = "https://search.naver.com/search.naver?where=nexearch&sm=tab_etc&qvt=0&query=%EB%8C%80%EC%A4%91%EC%9D%8C%EC%95%85%20%EA%B3%B5%EC%97%B0%20%EC%98%88%EB%A7%A4%ED%98%84%ED%99%A9";
    @Override
    public List<MainPageDto.readRanking> readRanking() {
        // 타겟 사이트에 연결 후 html 파일 읽어오기
        Connection connection = Jsoup.connect(naverMusicalRanking);
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
        Elements divBody = doc.select("div.container1 table tbody tr td table tbody tr td"); // 마지막 td 지워야할 수도?
        Element musicalRankTD = null, concertRankTD = null;
        // 뮤지컬, 콘서트 랭킹 td 뽑아내기
        for(Element e : divBody){
            if(index == 0) musicalRankTD = e;
            else if(index == 2) {
                concertRankTD = e;
                break;
            }
            index++;
        }

        if(musicalRankTD != null){

        }
        if(concertRankTD != null){

        }

        return null;
    }
}
