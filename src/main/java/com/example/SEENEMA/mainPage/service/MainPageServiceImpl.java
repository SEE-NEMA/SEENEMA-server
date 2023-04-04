package com.example.SEENEMA.mainPage.service;

import com.example.SEENEMA.mainPage.domain.Concert;
import com.example.SEENEMA.mainPage.domain.Musical;
import com.example.SEENEMA.mainPage.dto.MainPageDto;
import com.example.SEENEMA.mainPage.dto.PlayDto;
import com.example.SEENEMA.mainPage.repository.ConcertRepository;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MainPageServiceImpl implements MainPageService {
    private final String interparkMusical = "http://ticket.interpark.com/contents/Ranking/RankList?pKind=01011&pType=D&pCate=01011";
    private final String interparkConcert = "http://ticket.interpark.com/contents/Ranking/RankList?pKind=01003&pCate=&pType=D";
    private final String playdbURL= "http://www.playdb.co.kr/playdb/playdblist.asp";
    private final MusicalRepository musicalRepository;
    private final ConcertRepository concertRepository;
    private final int MAX_PAGE = 100;

    /** 공연 랭킹 크롤링 */
    @Override
    public MainPageDto.responseDTO readRanking() {
        // 타겟 사이트에 연결 후 html 파일 읽어오기
        Connection musicalConnection = Jsoup.connect(interparkMusical);
        Connection concertConnection = Jsoup.connect(interparkConcert);
        //List<List<MainPageDto.readRanking>> response = new ArrayList<>();
        MainPageDto.responseDTO response = new MainPageDto.responseDTO();
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
            //if(isMusical == 1) title = realTitle(title); // 뮤지컬 랭킹만 / 위의 title에서 <>안의 제목만 가져와야함.;
            tmp.setRank(rank);
            tmp.setTitle(title);
            result.add(tmp);
            if(rank == 10) return result;   // 1-10위 정보만 필요
        }
        return null;
    }
    // web-crawling 해온 뮤지컬 랭킹의 title에서 불필요한 문구(앞에 뮤지컬 적혀있는거) 제거 후 제목만 넘김.
    // 뮤지컬 <> 외에도 제목 양식이 존재하기 때문에 오류방생 -> 기능 삭제


    /**** 뮤지컬 크롤링 ****/

    /** 뮤지컬 목록 **/
    @Override
    public List<PlayDto.musicalList> getMusicals() {

        List<PlayDto.musicalList> musicalList = new ArrayList<>();

        for (int page = 1; page <= MAX_PAGE; page++) {
            Connection conn = Jsoup.connect(playdbURL)
                    .data("Page", String.valueOf(page))
                    .data("sReqMainCategory", "000001")
                    .data("sReqSubCategory", "")
                    .data("sReqDistrict", "")
                    .data("sReqTab", "2")
                    .data("sPlayType", "2")
                    .data("sStartYear", "")
                    .data("sSelectType", "1");

            try {
                Document doc = conn.get();
                Elements rows = doc.select("div.container1 > table > tbody > tr:nth-child(11) > td > table > tbody > tr:nth-child(n+3):nth-child(odd) > td > table > tbody > tr > td[width=\"493\"]");

                for (Element row : rows) {
                    PlayDto.musicalList musical = new PlayDto.musicalList();

                    musical.setImgUrl(row.selectFirst("td[width=\"90\"] img").attr("src"));
                    musical.setTitle(row.selectFirst("td[width=\"375\"]> table > tbody > tr:first-child ").text());

                    String[] parts = row.selectFirst("td[width=\"375\"] > table > tbody > tr:nth-child(2) > td").html().split("<br>");

                    musical.setGenre(parts[0].replaceAll("세부장르 : ", "").trim());
                    musical.setDate(parts[1].replaceAll("일시 : ", "").trim());
                    musical.setPlace(Jsoup.parse(parts[2]).text().replaceAll("장소 : ", ""));

                    String cast = null;
                    if (parts.length >= 4) { // 출연 정보가 있는 경우
                        String castText = parts[3].replaceFirst("출연\\s*:\\s*", ""); // "출연 : " 문자열 제거
                        Elements castElements = Jsoup.parse(castText).select("a");
                        cast = castElements.isEmpty() ? null : castElements.stream().map(e -> e.text()).collect(Collectors.joining(", "));
                    }
                    musical.setCast(cast);

                    String detailUrl = null;
                    if (parts.length >= 4) { // detail url이 있는 경우
                        Element aElement = row.selectFirst("a:has(img)");
                        detailUrl = aElement != null ? aElement.attr("href") : null;
                    }
                    musical.setDetailUrl(detailUrl);

                    musicalList.add(musical);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return musicalList;
    }

    /** db 저장 */
    public void saveMusicals(List<PlayDto.musicalList> musicalList) {
        for (PlayDto.musicalList musical : musicalList) {
            if (!musicalRepository.findByTitleAndDateAndPlace(musical.getTitle(),musical.getDate(),musical.getPlace()).isEmpty()) {
                // 이미 저장된 데이터이므로 무시
            continue;
            }
            Musical savedMusical = Musical.builder()
                    .title(musical.getTitle())
                    .genre(musical.getGenre())
                    .date(musical.getDate())
                    .place(musical.getPlace())
                    .cast(musical.getCast())
                    .imgUrl(musical.getImgUrl())
                    .detailUrl(musical.getDetailUrl())
                    .build();
            musicalRepository.save(savedMusical);
        }
    }

    /** 종료된 뮤지컬 삭제 */
    public void deleteMusicals() {
        List<Musical> musicals = musicalRepository.findAll();
        for (Musical musical : musicals) {
            String[] dates = musical.getDate().split(" ~ ");
            if (dates.length != 2) {
                continue; // yyyy.mm.dd 형식이 아닌 데이터는 삭제하지 않음
            }
            String endDateString = dates[1];
            try {
                LocalDate endDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                if (endDate.isBefore(LocalDate.now())) {
                    musicalRepository.delete(musical);
                }
            } catch (DateTimeParseException e) {
                // yyyy.mm.dd 형식으로 파싱할 수 없는 데이터는 삭제하지 않음
            }
        }
    }

    /** 24시간마다 갱신 */
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void scheduledMusicals() {
        List<PlayDto.musicalList> musicalList = getMusicals();
        saveMusicals(musicalList);
        deleteMusicals();
    }

    /** 뮤지컬 목록에 title,place,imgurl만 출력*/
    public List<PlayDto.musicalList> getMusicalList(){
        List<Musical> musicals = musicalRepository.findAll();
        return musicals.stream().map(musical -> new PlayDto.musicalList(musical.getImgUrl(), musical.getTitle(), musical.getPlace(), musical.getDate())).collect(Collectors.toList());
    }

    /** 뮤지컬 상세 정보*/
    public PlayDto.musicalInfo getMusicalInfo(Long no){
        Musical musical = getMusical(no);
        return new PlayDto.musicalInfo(musical);
    }

    private Musical getMusical(Long no){
        return musicalRepository.findById(no).orElseThrow();
    }


    /**** 콘서트 크롤링 ****/

    /** 콘서트 목록 **/
    @Override
    public List<PlayDto.concertList> getConcerts() {

        List<PlayDto.concertList> concertList = new ArrayList<>();

        for (int page = 1; page <= MAX_PAGE; page++) {
            for (String sPlayType : new String[]{"2", "3"}) {
                Connection conn = Jsoup.connect(playdbURL)
                        .data("Page", String.valueOf(page))
                        .data("sReqMainCategory", "000003")
                        .data("sReqSubCategory", "")
                        .data("sReqDistrict", "")
                        .data("sReqTab", "2")
                        .data("sPlayType", sPlayType)
                        .data("sStartYear", "")
                        .data("sSelectType", "1");


                try {
                    Document doc = conn.get();
                    Elements rows = doc.select("div.container1 > table > tbody > tr:nth-child(11) > td > table > tbody > tr:nth-child(n+3):nth-child(odd) > td > table > tbody > tr > td[width=\"493\"]");

                    for (Element row : rows) {
                        PlayDto.concertList concert = new PlayDto.concertList();

                        concert.setImgUrl(row.selectFirst("td[width=\"90\"] img").attr("src"));
                        concert.setTitle(row.selectFirst("td[width=\"375\"]> table > tbody > tr:first-child ").text());

                        String[] parts = row.selectFirst("td[width=\"375\"] > table > tbody > tr:nth-child(2) > td").html().split("<br>");

                        concert.setGenre(parts[0].replaceAll("세부장르 : ", "").trim());
                        concert.setDate(parts[1].replaceAll("일시 : ", "").trim());
                        concert.setPlace(Jsoup.parse(parts[2]).text().replaceAll("장소 : ", ""));

                        String cast = null;
                        if (parts.length >= 4) { // 출연 정보가 있는 경우
                            String castText = parts[3].replaceFirst("출연\\s*:\\s*", ""); // "출연 : " 문자열 제거
                            Elements castElements = Jsoup.parse(castText).select("a");
                            cast = castElements.isEmpty() ? null : castElements.stream().map(e -> e.text()).collect(Collectors.joining(", "));
                        }
                        concert.setCast(cast);

                        String detailUrl = null;
                        if (parts.length >= 4) { // detail url이 있는 경우
                            Element aElement = row.selectFirst("a:has(img)");
                            detailUrl = aElement != null ? aElement.attr("href") : null;
                        }
                        concert.setDetailUrl(detailUrl);

                        concertList.add(concert);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return concertList;
    }

    /** db 저장 */
    public void saveConcerts(List<PlayDto.concertList> concertList) {
        for (PlayDto.concertList concert : concertList) {
            if (!concertRepository.findByTitleAndDateAndPlace(concert.getTitle(),concert.getDate(),concert.getPlace()).isEmpty()) {
                // 이미 저장된 데이터이므로 무시
                continue;
            }
            Concert savedConcert = Concert.builder()
                    .title(concert.getTitle())
                    .genre(concert.getGenre())
                    .date(concert.getDate())
                    .place(concert.getPlace())
                    .cast(concert.getCast())
                    .imgUrl(concert.getImgUrl())
                    .detailUrl(concert.getDetailUrl())
                    .build();
            concertRepository.save(savedConcert);
        }
    }

    /** 종료된 콘서트 삭제 */
    public void deleteConcerts() {
        List<Concert> concerts = concertRepository.findAll();
        for (Concert concert : concerts) {
            String[] dates = concert.getDate().split(" ~ ");
            if (dates.length != 2) {
                continue; // yyyy.mm.dd 형식이 아닌 데이터는 삭제하지 않음
            }
            String endDateString = dates[1];
            try {
                LocalDate endDate = LocalDate.parse(endDateString, DateTimeFormatter.ofPattern("yyyy.MM.dd"));
                if (endDate.isBefore(LocalDate.now())) {
                    concertRepository.delete(concert);
                }
            } catch (DateTimeParseException e) {
                // yyyy.mm.dd 형식으로 파싱할 수 없는 데이터는 삭제하지 않음
            }
        }
    }

    /** 24시간마다 갱신 */
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void scheduledConcerts() {
        List<PlayDto.concertList>concertList = getConcerts();
        saveConcerts(concertList);
        deleteConcerts();
    }

    /** 콘서트 목록에 title,place,imgurl만 출력*/
    public List<PlayDto.concertList> getConcertList(){
        List<Concert> concerts = concertRepository.findAll();
        return concerts.stream().map(concert -> new PlayDto.concertList(concert.getImgUrl(), concert.getTitle(), concert.getPlace(), concert.getDate())).collect(Collectors.toList());
    }

    /** 콘서트 상세 정보*/
    public PlayDto.concertInfo getConcertInfo(Long no){
        Concert concert = getConcert(no);
        return new PlayDto.concertInfo(concert);
    }

    private Concert getConcert(Long no){
        return concertRepository.findById(no).orElseThrow();
    }
}