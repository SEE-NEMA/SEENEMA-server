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
    public List<List<MainPageDto.readRanking>> readRanking() {
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
    // int rank, String title
    private List<MainPageDto.readRanking> getRankList(Document doc, int isMusical){ // if isMusical == 1 -> true
        Elements divClass = doc.select("td.prds");
        List<MainPageDto.readRanking> result = new ArrayList<>(); // 함수 return 값
        for(Element e : divClass){
            MainPageDto.readRanking tmp = new MainPageDto.readRanking();
            int rank = Integer.parseInt(e.select("div.ranks i").text());
            String title = e.select("div.prdInfo a b").text();
            if(isMusical == 1) title = realTitle(title); // 뮤지컬 랭킹만 / 위의 title에서 <>안의 제목만 가져와야함.;
            tmp.setRank(rank);
            tmp.setTitle(title);
            result.add(tmp);
            if(rank == 10) return result;   // 1-10위 정보만 필요
        }
        return null;
    }
    // web-crawling 해온 뮤지컬 랭킹의 title에서 불필요한 문구 제거 후 제목만 넘김.
    private String realTitle(String origin){
        if(!origin.contains("〈")) return origin;
        String[] tmp = origin.split("〈|〉");
        String result = tmp[1];
        return result;
    }
}
