package com.example.SEENEMA.domain.seat.chungmu.domain;

import com.example.SEENEMA.domain.seat.chungmu.ChungmuService;
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
@Table(name = "seat_chungmu")
public class ChungmuSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private int z;
    private int x;
    private int y;
    @OneToMany(mappedBy = "chungmuSeat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChungmuPost> chungmuPosts = new ArrayList<>();

    public String getSeatNumber() {
        return ChungmuService.convertToSeatNumber(x, y, z);
    }

}
