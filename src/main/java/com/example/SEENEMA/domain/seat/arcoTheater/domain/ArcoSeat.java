package com.example.SEENEMA.domain.seat.arcoTheater.domain;

import com.example.SEENEMA.domain.seat.arcoTheater.ArcoService;
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
public class ArcoSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private int x;
    private int y;
    private int z;
    @OneToMany(mappedBy = "arcoSeat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArcoPost> arcoPosts = new ArrayList<>();

    public String getSeatNumber() {
        return ArcoService.convertToSeatNumber(x, y, z);
    }

}
