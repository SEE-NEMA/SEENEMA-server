package com.example.SEENEMA.post.view.domain;

import com.example.SEENEMA.post.view.service.SeatService;
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
@Table(name = "seats_ArcoTheater")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private int x;
    private int y;
    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatViewPost> seatViewPosts = new ArrayList<>();

    public String getSeatNumber() {
        return SeatService.convertToSeatNumber(x, y);
    }

}
