package com.example.SEENEMA.domain.seat.blueSquareShinhan.domain;

import com.example.SEENEMA.domain.seat.blueSquareShinhan.ShinhanService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "blue_square_shinhan")
public class ShinhanSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private Integer x;
    private Integer y;

    @OneToMany(mappedBy = "shinhanSeat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShinhanPost> shinhanPosts = new ArrayList<>();

    public String getSeatNumber(){
        return ShinhanService.convertToSeatNumber(x,y);
    }
}
