package com.example.SEENEMA.post.theater.service;

import com.example.SEENEMA.comment.domain.Comment;
import com.example.SEENEMA.comment.dto.CommentDto;
import com.example.SEENEMA.comment.repository.CommentRepository;
import com.example.SEENEMA.post.theater.domain.TheaterPost;
import com.example.SEENEMA.post.theater.domain.TheaterPostHeart;
import com.example.SEENEMA.post.theater.dto.TheaterPostDto;
import com.example.SEENEMA.post.theater.repository.TheaterPostHeartRepository;
import com.example.SEENEMA.post.theater.repository.TheaterPostRepository;
import com.example.SEENEMA.post.file.Image;
import com.example.SEENEMA.post.file.ImageRepository;
import com.example.SEENEMA.theater.domain.Theater;
import com.example.SEENEMA.theater.repository.TheaterRepository;
import com.example.SEENEMA.user.domain.User;
import com.example.SEENEMA.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TheaterPostServiceImpl implements TheaterPostService{
    private final TheaterPostRepository theaterPostRepo;
    private final UserRepository userRepo;
    private final TheaterRepository theaterRepo;
//    private final TagRepository tagRepo;
    private final CommentRepository commentRepo;
    private final ImageRepository imageRepository;
    private final TheaterPostHeartRepository heartRepo;

    @Override
    @Transactional
    public TheaterPostDto.addResponse createTheaterPost(Long userId, TheaterPostDto.addRequest request){
        // 공연장 후기 게시글 작성
        User user = getUser(userId);

        String theaterName = findTheaterName(request.getTitle());
        Theater theater = getTheater(theaterName);
        List<Image> images = getImage(request.getImage());

//        List<Tag> tags = getTags(request.getTags());

        request.setUser(user);
        request.setTheater(theater);
//        request.setTags(tags);
        request.setImage(images);

        TheaterPost theaterPost = request.toEntity();
        theaterPost.setViewCount(1L);   // 조회수 초기값 = 1

        // ViewPost 엔티티에 저장된 Image 엔티티들을 영속화
        List<Image> persistedImages = new ArrayList<>();
        for(Image image : images) {
            persistedImages.add(imageRepository.save(image));
        }
        theaterPost.setImage(persistedImages);

        return new TheaterPostDto.addResponse(theaterPostRepo.save(theaterPost));
    }
    @Override
    @Transactional
    public List<TheaterPostDto.listResponse> listTheaterPost(){
        // 공연장 후기 게시글 메인 페이지
        List<TheaterPost> originPost = theaterPostRepo.findAll();
        List<TheaterPostDto.listResponse> result = new ArrayList<>();

        for(TheaterPost t : originPost){
            TheaterPostDto.listResponse tmp = new TheaterPostDto.listResponse(t);
            result.add(tmp);
        }
        return result;
    }

    @Override
    public String authUserForEdit(Long postNo, Long userId){
        // 공연장 후기 게시글 수정/삭제 전 사용자 인증
        Optional<TheaterPost> target = theaterPostRepo.findById(postNo);
        if(Objects.equals(target.get().getUser().getUserId(), userId)) return "SUCCESS";
        else return "NOT_SAME_USER";
    }

    @Override
    @Transactional
    public TheaterPostDto.deleteResponse deleteTheaterPost(Long postNo, Long userId){
        // 공연장 후기 게시글 삭제
        TheaterPostDto.deleteResponse response;
        Optional<TheaterPost> target = theaterPostRepo.findById(postNo);
        if(Objects.equals(target.get().getUser().getUserId(), userId)) {
            deleteCommentByPostNo(postNo);
            deleteHeartByPostNo(postNo);
            theaterPostRepo.deleteById(postNo);
            response = new TheaterPostDto.deleteResponse("SUCCESS");
        }
        else
            response = new TheaterPostDto.deleteResponse("CHECK_USER_ID");
        return response;
    }

    @Override
    @Transactional
    public TheaterPostDto.addResponse editTheaterPost(Long userId, Long postNo, TheaterPostDto.addRequest request){
        // 공연장 후기 게시글 수정
        //User user = getUser(userId);
        String theaterName = findTheaterName(request.getTitle());
        Theater theater = getTheater(theaterName);
//        List<Tag> tags = getTags(request.getTags());
        List<Image> images = getImage(request.getImage());

        TheaterPost t = getTheaterPost(postNo);
        // 작성자와 사용자 동일인 판별
        if(t.getUser().getUserId() != userId){
            // 동일인 X -> 수정 X
            return readTheaterPost(postNo);
        }
        else {
            t.setEditedAt(LocalDateTime.now());
            t.setTheater(theater);
//            t.setTags(tags);
            t.setImage(images);
            t.setTitle(request.getTitle());
            t.setContent(request.getContent());
            t.setViewCount(t.getViewCount() + 1L);

            TheaterPostDto.addResponse response = new TheaterPostDto.addResponse(theaterPostRepo.save(t));
            // 댓글 가져오기
            List<CommentDto.readComment> comments = findCommentByPostNo(postNo);
            response.setComments(comments);
            return response;
        }
    }

    @Override
    public TheaterPostDto.addResponse readTheaterPost(Long postNo){
        // 공연장 후기 게시글 조회
        TheaterPost t = getTheaterPost(postNo);
//        log.info(t.getTags().toString());
        t.setViewCount(t.getViewCount()+1L);
        t.setHeartCount((long) heartRepo.findByTheaterPost(t).size());
        t.getImage().size();
        TheaterPostDto.addResponse response = new TheaterPostDto.addResponse(t);

        // 댓글 가져오기
        List<CommentDto.readComment> comments = findCommentByPostNo(postNo);
        response.setComments(comments);
       return response;
    }
    @Override
    public TheaterPostDto.addResponse readTheaterPost(Long postNo, Long userId){
        // 로그인 한 사용자가 게시글 조회하는 경우 - 좋아요 여부 판단
        User u = getUser(userId);
        TheaterPost t = getTheaterPost(postNo);
        // 사용자가 이미 좋아요 한 게시글일 경우 addResponse의 heartedYN=true
        TheaterPostHeart tmp = heartRepo.findByUserAndTheaterPost(u,t);
        if (tmp!=null){
            TheaterPostDto.addResponse response = readTheaterPost(postNo);
            response.setHeartedYN(Boolean.TRUE);
            return response;
        }
        else return readTheaterPost(postNo);
    }

    @Override
    public List<TheaterPostDto.listResponse> searchTheaterPost(String title){
        // 공연장 후기 게시글 검색
        List<TheaterPostDto.listResponse> result =  findTheaterPostList(title);
        return result;
    }

    @Override
    public TheaterPostDto.addResponse writeCommentTheaterPost(Long userId, Long postNo, CommentDto.addRequest request){
        // 공연장 후기 게시글 댓글 작성
        TheaterPost t = getTheaterPost(postNo);
//        log.info(t.getTags().toString());
        t.getImage().size();
        TheaterPostDto.addResponse response = new TheaterPostDto.addResponse(t);

        // 댓글 작성
        request.setUser(getUser(userId));
        request.setTheaterPost(getTheaterPost(postNo));
        Comment comment = request.toEntity();
        commentRepo.save(comment);

        response.setComments(findCommentByPostNo(postNo));
        return response;
    }

    @Override
    public String authForEditComment(Long postNo, Long commentId, Long userId){
        // 공연장 후기 게시글 댓글 수정/삭제 전 인증
        Comment comment = commentRepo.findById(commentId).get();
        if(comment.getUser().getUserId().equals(userId)) return "SUCCESS";
        return "FAIL";
    }

    @Override
    public TheaterPostDto.addResponse editCommentTheaterPost(Long userId, Long postNo, Long commentId, CommentDto.addRequest request){
        // 공연장 후기 게시글 댓글 수정
        // 댓글 작성자와 로그인한 유저가 다르면 게시글 조회만 수행
        if(!commentRepo.findById(commentId).get().getUser().getUserId().equals(userId)) return readTheaterPost(postNo);

        Comment comment = commentRepo.findById(commentId).get();
        comment.setContent(request.getContent());
        commentRepo.save(comment);

        TheaterPost t = getTheaterPost(postNo);
//        log.info(t.getTags().toString());
        t.getImage().size();
        TheaterPostDto.addResponse response = new TheaterPostDto.addResponse(t);
        // 댓글 가져오기
        List<CommentDto.readComment> comments = findCommentByPostNo(postNo);
        response.setComments(comments);
        return response;
    }

    @Override
    public TheaterPostDto.addResponse deleteCommentTheaterPost(Long userId, Long postNo, Long commentId){
        // 공연장 후기 게시글 댓글 삭제
        // 댓글 작성자와 로그인한 유저가 다르면 게시글 조회만 수행
        if(!commentRepo.findById(commentId).get().getUser().getUserId().equals(userId)) return readTheaterPost(postNo);

        commentRepo.deleteById(commentId);
        return readTheaterPost(postNo);
    }

    @Override
    public TheaterPostDto.addResponse heartTheaterPost(Long userId, Long postNo){
        // 게시글 좋아요
        User u = getUser(userId);
        TheaterPost t = getTheaterPost(postNo);
        // 사용자가 이미 좋아요 한 게시글일 경우 무시
        TheaterPostHeart tmp = heartRepo.findByUserAndTheaterPost(u,t);
        if (tmp!=null){
            return readTheaterPost(postNo, userId);
        }

        TheaterPostHeart heart = TheaterPostHeart.builder()
                .theaterPost(t)
                .user(u)
                .build();
        heartRepo.save(heart);                  // 사용자와 게시글 좋아요 정보 저장
        assert t != null;
        t.setHeartCount(t.getHeartCount() + 1L);    // 좋아요 갯수 + 1
        theaterPostRepo.save(t);
        return readTheaterPost(postNo, userId);
    }
    @Override
    public TheaterPostDto.addResponse cancelHeart(Long userId, Long postNo){
        User u = getUser(userId);
        TheaterPost t = getTheaterPost(postNo);
        // 좋아요 취소
        TheaterPostHeart tmp = heartRepo.findByUserAndTheaterPost(u,t);
        if(tmp != null){
            heartRepo.deleteById(tmp.getId());
        }
        t.setHeartCount(t.getHeartCount() - 1L);    // 좋아요 갯수 - 1
        theaterPostRepo.save(t);
        return readTheaterPost(postNo, userId);
    }


    private User getUser(Long userId){
        return userRepo.findById(userId).get();
    }
    private String findTheaterName(String origin){
        String[] title = origin.split("\\+");
        String[] title2 = title[0].split(" ", 2);
        String theaterName = title2[1];
        theaterName = theaterName.trim();
        return theaterName;
    }
    private Theater getTheater(Long theaterId){
        return theaterRepo.findById(theaterId).get();
    }
    private Theater getTheater(String theaterName){
        List<Theater> theaters = theaterRepo.findAll();
        for(Theater t:theaters){
            if(t.getTheaterName().equals(theaterName)) {
                return t;
            }
        }
        return null;
    }
    private TheaterPost getTheaterPost(Long post_no){
        List<TheaterPost> theaterPost = theaterPostRepo.findAll();
        for(TheaterPost t:theaterPost){
            if(t.getPostNo().equals(post_no)){
                return t;
            }
        }
        return null;
    }
//    private List<Tag> getTags(List<Tag> tags){
//        List<Tag> tmp = tagRepo.findAll();
//        for (Tag t : tmp){
//            for(Tag ex : tags){
//                if(t.getTagId().equals(ex.getTagId()))
//                    ex.setTagName(t.getTagName());
//            }
//        }
//        return tags;
//    }
    private List<TheaterPostDto.listResponse> findTheaterPostList(String title){
        List<TheaterPost> originPost = theaterPostRepo.findAll();
        List<TheaterPostDto.listResponse> result = new ArrayList<>();
        for(TheaterPost t : originPost){
            if(t.getTitle().contains(title))
                result.add(new TheaterPostDto.listResponse(t));
        }
        return result;
    }
    // postNo에 해당하는 댓글들 반환
    private List<CommentDto.readComment> findCommentByPostNo(Long post_no){
        List<Comment> allComment = commentRepo.findAll();
        List<CommentDto.readComment> result = new ArrayList<>();
        for(Comment c : allComment){
            if(c.getTheaterPost().getPostNo() == post_no){
                CommentDto.readComment tmp = new CommentDto.readComment(c);
                result.add(tmp);
                //log.info(tmp.getNickname());
                //log.info(tmp.getContent());
                //log.info(tmp.getCreatedAt());
            }
        }
        return result;
    }
    // postNo의 댓글 삭제
    private void deleteCommentByPostNo(Long post_no){
        List<Comment> allComment = commentRepo.findAll();
        for(Comment c : allComment){
            if(c.getTheaterPost().getPostNo() == post_no)
                commentRepo.delete(c);
        }
    }

    private List<Image> getImage(List<Image> images){
        List<Image> tmp = imageRepository.findAll();
        for (Image a : tmp){
            for(Image i : images){
                if(a.getImgUrl().equals(i.getImgUrl()))
                    i.setImgUrl(a.getImgUrl());
            }
        }
        return images;
    }
    private void deleteHeartByPostNo(Long postNo){
        List<TheaterPostHeart> tmp = heartRepo.findAll();
        for (TheaterPostHeart h : tmp) {
            if (h.getTheaterPost() == getTheaterPost(postNo)) heartRepo.delete(h);
        }
    }
}
