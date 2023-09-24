package com.example.SEENEMA.domain.user.service;

import com.example.SEENEMA.domain.post.comment.domain.Comment;
import com.example.SEENEMA.domain.post.comment.repository.CommentRepository;
import com.example.SEENEMA.domain.post.theater.domain.TheaterPost;
import com.example.SEENEMA.domain.post.theater.domain.TheaterPostHeart;
import com.example.SEENEMA.domain.post.theater.dto.TheaterPostDto;
import com.example.SEENEMA.domain.post.theater.repository.TheaterPostHeartRepository;
import com.example.SEENEMA.domain.post.theater.repository.TheaterPostRepository;
import com.example.SEENEMA.domain.post.view.domain.ViewPost;
import com.example.SEENEMA.domain.post.view.dto.ViewPostDto;
import com.example.SEENEMA.domain.post.view.repository.ViewPostHeartRepository;
import com.example.SEENEMA.domain.post.view.repository.ViewPostRepository;
import com.example.SEENEMA.domain.seat.arcoTheater.domain.ArcoPost;
import com.example.SEENEMA.domain.seat.arcoTheater.repository.ArcoPostRepository;
import com.example.SEENEMA.domain.seat.blueSquareMasterCard.domain.MastercardPost;
import com.example.SEENEMA.domain.seat.blueSquareMasterCard.repository.MastercardPostRepository;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.domain.ShinhanPost;
import com.example.SEENEMA.domain.seat.blueSquareShinhan.repository.ShinhanPostRepository;
import com.example.SEENEMA.domain.seat.chungmu.domain.ChungmuPost;
import com.example.SEENEMA.domain.seat.chungmu.repository.ChungmuPostRepository;
import com.example.SEENEMA.domain.user.domain.User;
import com.example.SEENEMA.domain.post.view.domain.ViewPostHeart;
import com.example.SEENEMA.domain.user.dto.MyPageDto;
import com.example.SEENEMA.domain.user.repository.RewardRepository;
import com.example.SEENEMA.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MyPageServiceImpl implements MyPageService{
    private final UserRepository userRepo;
    private final TheaterPostRepository theaterPostRepo;
    private final TheaterPostHeartRepository theaterHeartRepo;
    private final CommentRepository commentRepo;
    private final ViewPostRepository viewPostRepo;
    private final ViewPostHeartRepository viewHeartRepo;
    private final RewardRepository rewardRepo;
    private final ArcoPostRepository arcoPostRepo;
    private final ChungmuPostRepository chungmuPostRepo;
    private final ShinhanPostRepository shinhanPostRepo;
    private final MastercardPostRepository mastercardPostRepo;
    @Override
    public MyPageDto.MyPageResponse loadMyPage(User user) {
        MyPageDto.MyPageResponse response = new MyPageDto.MyPageResponse(user);
        return response;
    }
    @Override
    public MyPageDto.WithPoints loadMyPageWithPoints(User user){
        MyPageDto.WithPoints response = new MyPageDto.WithPoints(user);
        response.setPoints(rewardRepo.findByUser(user).getPoint());
        return response;
    }

    @Override
    public String checkNickname(User user, MyPageDto.EditProfileRequest request) {
        if(user.getNickname().equals(request.getNickname()))
            return "기존 닉네임과 동일합니다.";
        List<User> allUser = userRepo.findAll();
        for(User u : allUser){
            if(u.getNickname().equals(request.getNickname()))
                return "이미 존재하는 닉네임입니다.";
        }
        return "사용할 수 있는 닉네임입니다.";
    }

    @Override
    public MyPageDto.MyPageResponse editProfile(User user, MyPageDto.EditProfileRequest request) {
        // 프로필 수정
        User origin = userRepo.findById(user.getUserId()).get();
        origin.setNickname(request.getNickname());
        userRepo.save(origin);

        MyPageDto.MyPageResponse response = new MyPageDto.MyPageResponse(origin);
        return response;
    }

    @Override
    public MyPageDto.MyPageResponse interparkLogin(User user, MyPageDto.InterparkLoginInfo request) {
        // 프로필 수정
        User origin = userRepo.findById(user.getUserId()).get();
        origin.setInterparkId(request.getInterparkId());
        origin.setInterparkPwd(request.getInterparkPwd());
        userRepo.save(origin);

        MyPageDto.MyPageResponse response = new MyPageDto.MyPageResponse(origin);
        return response;
    }

    @Override
    public List<TheaterPostDto.listResponse> listMyTheaterReview(User user) {
        List<TheaterPostDto.listResponse> response = new ArrayList<>();
        List<TheaterPost> theaterPosts = theaterPostRepo.findByUser(user);
        for(TheaterPost t : theaterPosts){
            TheaterPostDto.listResponse tmp = new TheaterPostDto.listResponse(t);
            response.add(tmp);
        }
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    @Override
    public List<MyPageDto.MyCommentList> listMyComment(User user) {
        List<MyPageDto.MyCommentList> response = new ArrayList<>();
        List<Comment> comments = commentRepo.findByUser(user);
        for(Comment c : comments){
            MyPageDto.MyCommentList tmp = new MyPageDto.MyCommentList(c);
            response.add(tmp);
        }
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    @Override
    public List<ViewPostDto.viewListResponse> listMyViewReview(User user) {
        List<ViewPostDto.viewListResponse> response = new ArrayList<>();
        List<ViewPost> viewPosts = viewPostRepo.findByUser(user);
        for(ViewPost v : viewPosts){
            ViewPostDto.viewListResponse tmp = new ViewPostDto.viewListResponse(v);
            response.add(tmp);
        }
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    @Override
    public List<ViewPostDto.viewListResponse> listHeartedViewReview(User user) {
        List<ViewPostDto.viewListResponse> response = new ArrayList<>();
        List<ViewPostHeart> viewPostHearts = viewHeartRepo.findByUser(user);
        for(ViewPostHeart h : viewPostHearts){
            ViewPostDto.viewListResponse tmp = new ViewPostDto.viewListResponse(h.getViewPost());
            response.add(tmp);
        }
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    @Override
    public List<TheaterPostDto.listResponse> listHeartedTheaterReview(User user) {
        List<TheaterPostDto.listResponse> response = new ArrayList<>();
        List<TheaterPostHeart> theaterPostHearts = theaterHeartRepo.findByUser(user);
        for(TheaterPostHeart h : theaterPostHearts){
            TheaterPostDto.listResponse tmp = new TheaterPostDto.listResponse(h.getTheaterPost());
            response.add(tmp);
        }
        Collections.sort(response, Collections.reverseOrder());
        return response;
    }

    @Override
    public List<MyPageDto.MySeatList> listMySeatReview(User user) {
        List<MyPageDto.MySeatList> response = new ArrayList<>();
        List<ArcoPost> arcoPosts = arcoPostRepo.findByUser(user);
        List<ChungmuPost> chungmuPosts = chungmuPostRepo.findByUser(user);
        List<ShinhanPost> shinhanPosts = shinhanPostRepo.findByUser(user);
        List<MastercardPost> mastercardPosts = mastercardPostRepo.findByUser(user);

        for(ArcoPost a : arcoPosts){
            MyPageDto.MySeatList tmp = new MyPageDto.MySeatList(a);
            response.add(tmp);
        }
        for(ChungmuPost c : chungmuPosts){
            MyPageDto.MySeatList tmp = new MyPageDto.MySeatList(c);
            response.add(tmp);
        }
        for(ShinhanPost s : shinhanPosts){
            MyPageDto.MySeatList tmp = new MyPageDto.MySeatList(s);
            response.add(tmp);
        }
        for(MastercardPost m : mastercardPosts){
            MyPageDto.MySeatList tmp = new MyPageDto.MySeatList(m);
            response.add(tmp);
        }
        Collections.sort(response, Collections.reverseOrder());

        return response;
    }
}
