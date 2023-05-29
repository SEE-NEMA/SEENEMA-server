package com.example.SEENEMA.domain.seat.blueSquareMasterCard.domain;

import com.example.SEENEMA.domain.seat.blueSquareMasterCard.MastercardService;
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
@Table(name = "blue_square_mastercard")
public class MastercardSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private int x;
    private int y;
    private int z;
    @OneToMany(mappedBy = "mastercardSeat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MastercardPost> mastercardPosts = new ArrayList<>();
    public MastercardSeat(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public String getSeatNumber() { return MastercardService.convertToSeatNumber(x, y, z); }
}
