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
    private final String playdbURL = "http://www.playdb.co.kr/playdb/playdblist.asp";
    private final String wmpURL = "https://ticket.wemakeprice.com/category/10002";
    private final String elevenURL = "https://ticket.11st.co.kr/Product/List";
    private final String rankingMusical = "http://ticket.interpark.com/contents/Ranking/RankList?pKind=01011&pType=D&pCate=01011";
    private final String rankingConcert = "http://ticket.interpark.com/contents/Ranking/RankList?pKind=01003&pType=D&pCate=01003";

    private final ConcertRankingRepository concertRankingRepository;
    private final MusicalRankingRepository musicalRankingRepository;
    private final MusicalRepository musicalRepository;
    private final ConcertRepository concertRepository;
    private final int MAX_PAGE = 50;

    /** 공연 랭킹 크롤링 */
    public List<MainPageDto.musicalRanking> getMusicalRank() {
        List<MainPageDto.musicalRanking> musicalRankings = new ArrayList<>();
        Connection musicalConnection = Jsoup.connect(rankingMusical);
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
        Connection concertConnection = Jsoup.connect(rankingConcert);
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
        // 기존 랭킹 데이터 삭제
        musicalRankingRepository.deleteAll();

        List<MusicalRanking> musicalRankings = new ArrayList<>();
        for (MainPageDto.musicalRanking dto : musicalList) {
            MusicalRanking musicalRanking = MusicalRanking.builder()
                    .ranking(dto.getRanking())
                    .title(dto.getTitle())
                    .imgUrl(dto.getImgUrl())
                    .build();
            musicalRankings.add(musicalRanking);
        }
        musicalRankingRepository.saveAll(musicalRankings);
    }

    public void saveConcertRanking(List<MainPageDto.concertRanking> concertList) {
        // 기존 랭킹 데이터 삭제
        concertRankingRepository.deleteAll();

        List<ConcertRanking> concertRankings = new ArrayList<>();
        for (MainPageDto.concertRanking dto : concertList) {
            ConcertRanking concertRanking = ConcertRanking.builder()
                    .ranking(dto.getRanking())
                    .title(dto.getTitle())
                    .imgUrl(dto.getImgUrl())
                    .build();
            concertRankings.add(concertRanking);
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



    /**** playdb 뮤지컬 크롤링 ****/
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

                    String interparkUrl = row.selectFirst("td[width=\"375\"] > table > tbody > tr:first-child a").attr("onclick");
                    int startIndex = interparkUrl.indexOf('\'') + 1;
                    int endIndex = interparkUrl.lastIndexOf('\'');
                    String numericPart = interparkUrl.substring(startIndex, endIndex);
                    int numericValue = Integer.parseInt(numericPart);

                    musical.setInterparkUrl("http://www.playdb.co.kr/playdb/playdbDetail.asp?sReqPlayno="+numericValue);

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
                    .interparkUrl(musical.getInterparkUrl())
                    .melonUrl(musical.getMelonUrl())
                    .elevenUrl(musical.getElevenUrl())
                    .build();
            musicalRepository.save(savedMusical);
        }
    }
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


    /** 11번가 뮤지컬 */
    @Override
    public List<PlayDto.musicalList> getElevenMusicals() {
        List<PlayDto.musicalList> musicalList = new ArrayList<>();

        for (int CurrentPageIndex = 1; CurrentPageIndex <= 5; CurrentPageIndex++) {
            Connection conn = Jsoup.connect(elevenURL)
                    .data("genreId", "14123")
                    .data("genreSubId", "")
                    .data("regionId", "")
                    .data("performanceThema", "")
                    .data("period", "0")
                    .data("sortcolumn", "5")
                    .data("hdHashTagList", "")
                    .data("searchText", "")
                    .data("CurrentPageIndex", String.valueOf(CurrentPageIndex));

            try {
                Document doc = conn.get();
                Elements rows = doc.select("body > div#wrapBody > div#layBodyWrap > div.l_content > form > div > div > div > div > div.tk_result_list > div.tk_content > div.tk_section.play > ul.tk_list > li");
                for (Element row : rows) {
                    PlayDto.musicalList musical = new PlayDto.musicalList();

                    musical.setTitle(row.selectFirst("a > span.tk_guest").text());

                    String elevenUrl = "https://ticket.11st.co.kr" + row.selectFirst("a").attr("href");
                    musical.setElevenUrl(elevenUrl);
                    musical.setImgUrl(row.selectFirst("a > img").attr("src"));

                    String dateRange = row.selectFirst("a > span.tk_date").text();
                    dateRange = dateRange.replace("-", " ~ ");
                    musical.setDate(dateRange);

                    Elements placeElements = row.select("a > span.tk_place");
                    if (!placeElements.isEmpty()) {
                        musical.setPlace(placeElements.get(0).text());
                    }

                    // elevenUrl에 접속하여 genre와 cast 정보를 가져옴
                    Connection urlConn = Jsoup.connect(elevenUrl);

                    try {
                        Document urlDoc = urlConn.get();
                        Elements ticketInfoDiv = urlDoc.select("body > div#wrapBody> div#layBodyWrap > div.l_content > div > div:first-child > div.l_product_cont > div.l_product_view_wrap > div.l_product_summary > div.l_product_side_info > div.c_product_info_title > dl.c_ticket_info > div");

                        // 3번째 div의 <dd> 텍스트 값 가져오기
                        if (ticketInfoDiv.size() >= 3) {
                            Element thirdDiv = ticketInfoDiv.get(2);
                            Element thirdDivDd = thirdDiv.selectFirst("dd");
                            if (thirdDivDd != null) {
                                String genre = thirdDivDd.text();
                                musical.setGenre(genre);
                            }
                        }
                        // 6번째 div의 <dd> 텍스트 값 가져오기
                        if (ticketInfoDiv.size() >= 6) {
                            Element sixthDiv = ticketInfoDiv.get(5);
                            Element sixthDivDd = sixthDiv.selectFirst("dd");
                            if (sixthDivDd != null) {
                                String cast = sixthDivDd.text();
                                musical.setCast(cast);
                            }
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    musicalList.add(musical);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return musicalList;
    }

    /** db 저장 */
    public void saveElevenMusicals(List<PlayDto.musicalList> musicalList) {
        for (PlayDto.musicalList musical : musicalList) {
            // 데이터베이스에서 날짜(date)와 장소(place)가 일치하는 데이터를 찾습니다.
            List<Musical> existingMusicals = musicalRepository.findByDateAndPlaceContaining(musical.getDate(), musical.getPlace());

            if (!existingMusicals.isEmpty()) {
                // 일치하는 데이터가 있으면 해당 데이터의 elevenUrl 속성에 값을 집어넣습니다.
                for (Musical existingMusical : existingMusicals) {
                    existingMusical.setElevenUrl(musical.getElevenUrl());

                    // cast와 genre가 비어있을 때만 값을 넣어줍니다.
                    if (existingMusical.getCast() == null || existingMusical.getCast().isEmpty()) {
                        existingMusical.setCast(musical.getCast());
                    }
                    if (existingMusical.getGenre() == null || existingMusical.getGenre().isEmpty()) {
                        existingMusical.setGenre(musical.getGenre());
                    }

                    musicalRepository.save(existingMusical);
                }
            } else {
                // 일치하는 데이터가 없으면 새로운 데이터를 생성하여 저장합니다.
                Musical newMusical = Musical.builder()
                        .title(musical.getTitle())
                        .date(musical.getDate())
                        .place(musical.getPlace())
                        .cast(musical.getCast())
                        .genre(musical.getGenre())
                        .elevenUrl(musical.getElevenUrl())
                        .imgUrl(musical.getImgUrl())
                        .build();
                musicalRepository.save(newMusical);
            }
        }
    }
    /** 종료된 뮤지컬 삭제 */
    public void deleteElevenMusicals() {
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
    public void scheduledElevenMusicals() {
        List<PlayDto.musicalList> musicalLists = getElevenMusicals();
        saveElevenMusicals(musicalLists);
        deleteElevenMusicals();
    }

    /** 뮤지컬 목록에 title,place,imgurl만 출력*/
    public List<PlayDto.musicalList> getMusicalList(){
        List<Musical> musicals = musicalRepository.findAll();
        return musicals.stream().map(musical -> new PlayDto.musicalList(musical.getNo(),musical.getImgUrl(), musical.getTitle(), musical.getPlace(), musical.getDate(), musical.getInterparkUrl(), musical.getMelonUrl(), musical.getElevenUrl())).collect(Collectors.toList());
    }

    /** 뮤지컬 상세 정보*/
    public PlayDto.musicalInfo getMusicalInfo(Long no){
        Musical musical = getMusical(no);
        return new PlayDto.musicalInfo(musical);
    }

    private Musical getMusical(Long no){
        return musicalRepository.findById(no).orElseThrow();
    }

    /**** playdb 콘서트 크롤링 ****/

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

                        String interparkUrl = row.selectFirst("td[width=\"375\"] > table > tbody > tr:first-child a").attr("onclick");
                        int startIndex = interparkUrl.indexOf('\'') + 1;
                        int endIndex = interparkUrl.lastIndexOf('\'');
                        String numericPart = interparkUrl.substring(startIndex, endIndex);
                        int numericValue = Integer.parseInt(numericPart);

                        concert.setInterparkUrl("http://www.playdb.co.kr/playdb/playdbDetail.asp?sReqPlayno="+numericValue);

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
                    .interparkUrl(concert.getInterparkUrl())
                    .melonUrl(concert.getMelonUrl())
                    .elevenUrl(concert.getElevenUrl())
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

    /** 11번가 콘서트 */
    @Override
    public List<PlayDto.concertList> getElevenConcerts() {
        List<PlayDto.concertList> concertList = new ArrayList<>();

        for (int CurrentPageIndex = 1; CurrentPageIndex <= 5; CurrentPageIndex++) {
            Connection conn = Jsoup.connect(elevenURL)
                    .data("genreId", "14124")
                    .data("genreSubId", "")
                    .data("regionId", "")
                    .data("performanceThema", "")
                    .data("period", "0")
                    .data("sortcolumn", "4")
                    .data("hdHashTagList", "")
                    .data("searchText", "")
                    .data("CurrentPageIndex", String.valueOf(CurrentPageIndex));

            try {
                Document doc = conn.get();
                Elements rows = doc.select("body > div#wrapBody > div#layBodyWrap > div.l_content > form > div > div > div > div > div.tk_result_list > div.tk_content > div.tk_section.play > ul.tk_list > li");
                for (Element row : rows) {
                    PlayDto.concertList concert = new PlayDto.concertList();

                    concert.setTitle(row.selectFirst("a > span.tk_guest").text());

                    String elevenUrl = "https://ticket.11st.co.kr" + row.selectFirst("a").attr("href");
                    concert.setElevenUrl(elevenUrl);
                    concert.setImgUrl(row.selectFirst("a > img").attr("src"));

                    concert.setImgUrl(row.selectFirst("a > img").attr("src"));

                    String dateRange = row.selectFirst("a > span.tk_date").text();
                    dateRange = dateRange.replace("-", " ~ ");
                    concert.setDate(dateRange);

                    Elements placeElements = row.select("a > span.tk_place");
                    if (!placeElements.isEmpty()) {
                        concert.setPlace(placeElements.get(0).text());
                    }
                    // elevenUrl에 접속하여 genre와 cast 정보를 가져옴
                    Connection urlConn = Jsoup.connect(elevenUrl);

                    try {
                        Document urlDoc = urlConn.get();
                        Elements ticketInfoDiv = urlDoc.select("body > div#wrapBody> div#layBodyWrap > div.l_content > div > div:first-child > div.l_product_cont > div.l_product_view_wrap > div.l_product_summary > div.l_product_side_info > div.c_product_info_title > dl.c_ticket_info > div");

                        // 3번째 div의 <dd> 텍스트 값 가져오기
                        if (ticketInfoDiv.size() >= 3) {
                            Element thirdDiv = ticketInfoDiv.get(2);
                            Element thirdDivDd = thirdDiv.selectFirst("dd");
                            if (thirdDivDd != null) {
                                String genre = thirdDivDd.text();
                                concert.setGenre(genre);
                            }
                        }
                        // 6번째 div의 <dd> 텍스트 값 가져오기
                        if (ticketInfoDiv.size() >= 6) {
                            Element sixthDiv = ticketInfoDiv.get(5);
                            Element sixthDivDd = sixthDiv.selectFirst("dd");
                            if (sixthDivDd != null) {
                                String cast = sixthDivDd.text();
                                concert.setCast(cast);
                            }
                        }

                        System.out.println(concert.getCast());
                        System.out.println(concert.getGenre());
                        System.out.println("==========================");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    concertList.add(concert);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return concertList;
    }
    /** db 저장 */
    public void saveElevenConcerts(List<PlayDto.concertList> concertList) {
        for (PlayDto.concertList concert : concertList) {
            // 데이터베이스에서 날짜(date)와 장소(place)가 일치하는 데이터를 찾습니다.
            List<Concert> existingConcerts = concertRepository.findByDateAndPlaceContaining(concert.getDate(), concert.getPlace());

            if (!existingConcerts.isEmpty()) {
                // 일치하는 데이터가 있으면 해당 데이터의 elevenUrl 속성에 값을 집어넣습니다.
                for (Concert existingConcert : existingConcerts) {
                    existingConcert.setElevenUrl(concert.getElevenUrl());

                    // cast와 genre가 비어있을 때만 값을 넣어줍니다.
                    if (existingConcert.getCast() == null || existingConcert.getCast().isEmpty()) {
                        existingConcert.setCast(concert.getCast());
                    }
                    if (existingConcert.getGenre() == null || existingConcert.getGenre().isEmpty()) {
                        existingConcert.setGenre(concert.getGenre());
                    }

                    concertRepository.save(existingConcert);
                }
            } else {
                // 일치하는 데이터가 없으면 새로운 데이터를 생성하여 저장합니다.
                Concert newConcert = Concert.builder()
                        .title(concert.getTitle())
                        .date(concert.getDate())
                        .place(concert.getPlace())
                        .genre(concert.getGenre())
                        .cast(concert.getCast())
                        .elevenUrl(concert.getElevenUrl())
                        .imgUrl(concert.getImgUrl())
                        .build();
                concertRepository.save(newConcert);
            }
        }
    }
    /** 종료된 콘서트 삭제 */
    public void deleteElevenConcerts() {
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
    public void scheduledElevenConcerts() {
        List<PlayDto.concertList> concertList = getElevenConcerts();
        saveElevenConcerts(concertList);
        deleteElevenConcerts();
    }

    /** 콘서트 목록에 title,place,imgurl만 출력*/
    public List<PlayDto.concertList> getConcertList(){
        List<Concert> concerts = concertRepository.findAll();
        return concerts.stream().map(concert -> new PlayDto.concertList(concert.getNo(), concert.getImgUrl(), concert.getTitle(), concert.getPlace(), concert.getDate(), concert.getInterparkUrl(), concert.getMelonUrl(), concert.getElevenUrl())).collect(Collectors.toList());
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