package kyonggi.bookslyserver.domain.shop.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"name", "selfIntro"})
public class Employee extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="employee_id")
    private Long id;


    private String profileImg_uri;

    private String name;

    private String selfIntro;

    private int SchedulingCycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "employee")
    private List<Review> reviews = new ArrayList<>();
}
