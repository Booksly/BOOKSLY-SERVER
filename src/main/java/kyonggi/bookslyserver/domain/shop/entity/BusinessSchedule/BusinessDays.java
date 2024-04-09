package kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule;

import jakarta.persistence.*;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BusinessDays extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private DayName dayName;

    @ManyToOne
    @JoinColumn(name="id")
    private BusinessHrs businessHrs;

}
