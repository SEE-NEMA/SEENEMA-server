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
    private final String interparkMusical = "http://ticket.interpark.com/contents/Ranking/RankList?pKind=01011&pType=D&pCate=01011";
    private final String interparkConcert = "http://ticket.interpark.com/contents/Ranking/RankList?pKind=01003&pCate=&pType=D&pDate=20230314";
    @Override
    public List<List<MainPageDto.readRanking>> readRanking() {
        // 타겟 사이트에 연결 후 html 파일 읽어오기
        Connection musicalConnection = Jsoup.connect(interparkMusical);
        Connection concertConnection = Jsoup.connect(interparkConcert);
        List<List<MainPageDto.readRanking>> response = new ArrayList<>();
        try{
            Document doc = musicalConnection.get();
            response.add(getRankList(doc, 1));  // 뮤지컬 랭킹 읽기 및 add
            doc = concertConnection.get();
            response.add(getRankList(doc, 0));  // 콘서트 랭킹 읽기 및 add
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
