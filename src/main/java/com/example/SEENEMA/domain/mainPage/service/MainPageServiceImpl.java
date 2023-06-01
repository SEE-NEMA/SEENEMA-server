package com.example.SEENEMA.domain.mainPage.service;

import com.example.SEENEMA.domain.mainPage.domain.Concert;
import com.example.SEENEMA.domain.mainPage.domain.Musical;
import com.example.SEENEMA.domain.mainPage.domain.ConcertRanking;
import com.example.SEENEMA.domain.mainPage.domain.MusicalRanking;
import com.example.SEENEMA.domain.mainPage.dto.PlayDto;
import com.example.SEENEMA.domain.mainPage.repository.ConcertRankingRepository;
import com.example.SEENEMA.domain.mainPage.repository.ConcertRepository;
import com.example.SEENEMA.domain.mainPage.repository.MusicalRankingRepository;
import com.example.SEENEMA.domain.mainPage.repository.MusicalRepository;
import com.example.SEENEMA.domain.mainPage.dto.MainPageDto;
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
    private final String interparkConcert = "http://ticket.interpark.com/contents/Ranking/RankList?pKind=01003&pType=D&pCate=01003";
    private final String playdbURL = "http://www.playdb.co.kr/playdb/playdblist.asp";
    private final MusicalRepository musicalRepository;
    private final ConcertRepository concertRepository;
    private final ConcertRankingRepository concertRankingRepository;
    private final MusicalRankingRepository musicalRankingRepository;
    private final int MAX_PAGE = 100;

    /** 공연 랭킹 크롤링 */
    public List<MainPageDto.musicalRanking> getMusicalRank() {
        List<MainPageDto.musicalRanking> musicalRankings = new ArrayList<>();

        Connection musicalConnection = Jsoup.connect(interparkMusical);

        try {
            Document doc = musicalConnection.get();
            Elements divClass = doc.select("td.prds");

            for (Element e : divClass) {
                int rank = Integer.parseInt(e.select("div.ranks i").text());
                if (rank > 10) {
                    break;  // rank가 10보다 크면 반복문 종료
                }
                String title = e.select("div.prdInfo a b").text();
                String imgUrl = e.select("a").select("img").attr("src");

                MainPageDto.musicalRanking dto = new MainPageDto.musicalRanking();
                dto.setRanking(rank);
                dto.setTitle(title);
                dto.setImgUrl(imgUrl);

                musicalRankings.add(dto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return musicalRankings;
    }

    public List<MainPageDto.concertRanking> getConcertRank() {
        List<MainPageDto.concertRanking> concertRankings = new ArrayList<>();

        Connection concertConnection = Jsoup.connect(interparkConcert);

        try {
            Document doc = concertConnection.get();
            Elements divClass = doc.select("td.prds");

            for (Element e : divClass) {
                int rank = Integer.parseInt(e.select("div.ranks i").text());
                if (rank > 10) {
                    break;  // rank가 10보다 크면 반복문 종료
                }
                String title = e.select("div.prdInfo a b").text();
                String imgUrl = e.select("a").select("img").attr("src");

                MainPageDto.concertRanking dto = new MainPageDto.concertRanking();
                dto.setRanking(rank);
                dto.setTitle(title);
                dto.setImgUrl(imgUrl);

                concertRankings.add(dto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return concertRankings;
    }

    public void saveMusicalRanking(List<MainPageDto.musicalRanking> musicalList) {
        List<MusicalRanking> musicalRankings = new ArrayList<>();
        for (MainPageDto.musicalRanking dto : musicalList) {
            if (!musicalRankingRepository.findByTitleAndImgUrl(dto.getTitle(), dto.getImgUrl()).isEmpty()) {
                continue;
            }

            List<Musical> matchingMusicals = musicalRepository.findByTitleContainingIgnoreCase(dto.getTitle());
            if (!matchingMusicals.isEmpty()) {
                Musical musical = matchingMusicals.get(0); // 첫 번째 일치하는 Musical 객체만 가져옴
                MusicalRanking musicalRanking = MusicalRanking.builder()
                        .ranking(dto.getRanking())
                        .title(dto.getTitle())
                        .imgUrl(dto.getImgUrl())
                        .musical(musical)
                        .build();
                musicalRankings.add(musicalRanking);
            }
        }
        musicalRankingRepository.saveAll(musicalRankings);
    }

    public void saveConcertRanking(List<MainPageDto.concertRanking> concertList) {
        List<ConcertRanking> concertRankings = new ArrayList<>();
        for (MainPageDto.concertRanking dto : concertList) {
            if (!concertRankingRepository.findByTitleAndImgUrl(dto.getTitle(), dto.getImgUrl()).isEmpty()) {
                continue;
            }

            List<Concert> matchingConcerts = concertRepository.findByTitleContainingIgnoreCase(dto.getTitle());
            if (!matchingConcerts.isEmpty()) {
                Concert concert = matchingConcerts.get(0); // 첫 번째 일치하는 Concert 객체만 가져옴
                ConcertRanking concertRanking = ConcertRanking.builder()
                        .ranking(dto.getRanking())
                        .title(dto.getTitle())
                        .imgUrl(dto.getImgUrl())
                        .concert(concert)
                        .build();
                concertRankings.add(concertRanking);
            }
        }
        concertRankingRepository.saveAll(concertRankings);
    }
    @Scheduled(fixedDelay = 3600000)
    public void scheduledMusicalrank() {
        List<MainPageDto.musicalRanking> musical = getMusicalRank();
        saveMusicalRanking(musical);
    }
    @Scheduled(fixedDelay = 3600000)
    public void scheduledConcertrank() {
        List<MainPageDto.concertRanking> concert = getConcertRank();
        saveConcertRanking(concert);
    }


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

                    String imgURL_before = row.selectFirst("td[width=\"90\"] img").attr("onclick");
                    String imgURL_after = imgURL_before.split("'")[1];
                    Connection conForImg = Jsoup.connect("http://www.playdb.co.kr/playdb/playdbDetail.asp?sReqPlayno="+imgURL_after);
                    try{
                        Document imgDoc = conForImg.get();
                        Elements rowsIMG = imgDoc.select("div.pddetail > h2");
                        imgURL_after = rowsIMG.select("img").attr("src");
                        musical.setImgUrl(imgURL_after);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
//        saveMusicals(musicalList);
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
        return musicals.stream().map(musical -> new PlayDto.musicalList(musical.getNo(),musical.getImgUrl(), musical.getTitle(), musical.getPlace(), musical.getDate())).collect(Collectors.toList());
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

                        String imgURL_before = row.selectFirst("td[width=\"90\"] img").attr("onclick");
                        String imgURL_after = imgURL_before.split("'")[1];
                        Connection conForImg = Jsoup.connect("http://www.playdb.co.kr/playdb/playdbDetail.asp?sReqPlayno="+imgURL_after);
                        try{
                            Document imgDoc = conForImg.get();
                            Elements rowsIMG = imgDoc.select("div.pddetail > h2");
                            imgURL_after = rowsIMG.select("img").attr("src");
                            concert.setImgUrl(imgURL_after);
                        }catch (IOException e) {
                            e.printStackTrace();
                        }

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
        return concerts.stream().map(concert -> new PlayDto.concertList(concert.getNo(), concert.getImgUrl(), concert.getTitle(), concert.getPlace(), concert.getDate())).collect(Collectors.toList());
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