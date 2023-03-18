package com.example.SEENEMA.mainPage.service;

import com.example.SEENEMA.mainPage.domain.Musical;
import com.example.SEENEMA.mainPage.dto.MainPageDto;
import com.example.SEENEMA.mainPage.dto.PlayDto;
import com.example.SEENEMA.mainPage.repository.MusicalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.jsoup.nodes.Document;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MainPageServiceImpl implements MainPageService {
    private final String interparkMusical = "http://ticket.interpark.com/contents/Ranking/RankList?pKind=01011&pType=D&pCate=01011";
    private final String interparkConcert = "http://ticket.interpark.com/contents/Ranking/RankList?pKind=01003&pCate=&pType=D&pDate=20230314";
    private final String kopisMusicalList = "http://www.kopis.or.kr/openApi/restful/pblprfr?service=1a03c66c324840908e9f30ff983b4350&shcate=GGGA&stdate=20230315&eddate=20230615&cpage=1&rows=200&prfstate=02&prfstate=01&signgucode=11&kidstate=N";
    private final MusicalRepository musicalRepository;


    /** 공연 랭킹 크롤링 */
    @Override
    public MainPageDto.reasponseDTO readRanking() {
        // 타겟 사이트에 연결 후 html 파일 읽어오기
        Connection musicalConnection = Jsoup.connect(interparkMusical);
        Connection concertConnection = Jsoup.connect(interparkConcert);
        //List<List<MainPageDto.readRanking>> response = new ArrayList<>();
        MainPageDto.reasponseDTO response = new MainPageDto.reasponseDTO();
        try{
            Document doc = musicalConnection.get();
            List<MainPageDto.readRanking> musicalRank = getRankList(doc, 1);    // 뮤지컬 랭킹 읽기 및 add
            response.setMusicalRank(musicalRank);
            doc = concertConnection.get();
            List<MainPageDto.readRanking> concertRank = getRankList(doc, 0);     // 콘서트 랭킹 읽기 및 add
            response.setConcertRank(concertRank);
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
    // web-crawling 해온 뮤지컬 랭킹의 title에서 불필요한 문구(앞에 뮤지컬 적혀있는거) 제거 후 제목만 넘김.
    private String realTitle(String origin){
        String[] tmp = origin.split(" ", 2);
        String result = tmp[1].trim();
        return result;
    }

    /** 뮤지컬 크롤링 */
    public List<PlayDto.musicalList> getMusicalList() {
        List<PlayDto.musicalList> musicalList = new ArrayList<>();

        Connection conn = Jsoup.connect(kopisMusicalList);

        try {
            Document doc = conn.get();
            Elements elements = doc.select("db");

            for (Element element : elements) {
                PlayDto.musicalList musical = new PlayDto.musicalList();
                musical.setMusicalId(element.select("mt20id").text());
                musical.setTitle(element.select("prfnm").text());
                musical.setPlace(element.select("fcltynm").text());
                musical.setStartDate(LocalDate.parse(element.select("prfpdfrom").text(), DateTimeFormatter.ofPattern("yyyy.MM.dd")));
                musical.setEndDate(LocalDate.parse(element.select("prfpdto").text(), DateTimeFormatter.ofPattern("yyyy.MM.dd")));
                musical.setImgUrl(element.select("poster").text());
                musicalList.add(musical);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return musicalList;
    }

    public void saveMusicals(List<PlayDto.musicalList> musicalList) {
        for (PlayDto.musicalList musical : musicalList) {
            if (!musicalRepository.findByMusicalId(musical.getMusicalId()).isEmpty()) {
                // 이미 저장된 데이터이므로 무시
                continue;
            }
            Musical savedMusical = Musical.builder()
                    .musicalId(musical.getMusicalId())
                    .title(musical.getTitle())
                    .place(musical.getPlace())
                    .imgUrl(musical.getImgUrl())
                    .startDate(musical.getStartDate())
                    .endDate(musical.getEndDate())
                    .build();
            musicalRepository.save(savedMusical);
        }
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000) // 24시간마다 실행
    public void scheduledMusicals() {
        List<PlayDto.musicalList> musicalList = getMusicalList();
        saveMusicals(musicalList);
    }

}