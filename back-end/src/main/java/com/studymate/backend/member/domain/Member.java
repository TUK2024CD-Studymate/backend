package com.studymate.backend.member.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.member.dto.MemberUpdateRequest;
import com.studymate.backend.studycalender.domain.StudyCalender;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE user_id =?")
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column
    private String email;
    private String tel;
    @Column
    private String password;
    @Column
    private String blogUrl;
    @Column
    private int solved;
    @Column
    private int matchingCount;
    @Column
    private int reviewCount;
    @Column
    private int heart;
    @Column
    private String publicRelations;
    @Column
    private String job;
    @Column
    private int star;
    @Column(precision = 5, scale = 2)
    @DecimalMax(value = "5")
    @DecimalMin(value = "0")
    private BigDecimal starAverage;
    @Column
    private String name;
    @Enumerated(value = EnumType.STRING)
    @Column
    private Part part;
    @Enumerated(value = EnumType.STRING)
    @Column
    private Interests interests;
    @Column(unique = true)
    private String nickname;
//    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
//    private ProfileImg profileUrl;
    @Column
    private Boolean isDeleted;
    @Column
    private boolean activated;

    @Column(length = 1000)
    private String fcmToken;

    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    @ManyToMany
    private Set<Authority> authorities;

    @OneToMany(mappedBy = "member")
    private final List<StudyCalender> studyCalenders = new ArrayList<>();

    public void update(MemberUpdateRequest request) {
        this.name = request.getName();
        this.part = request.getPart();
        this.interests = request.getInterests();
        this.nickname = request.getNickname();
        this.blogUrl = request.getBlogUrl();
        this.publicRelations = request.getPublicRelations();
        this.job = request.getJob();
    }

    public void updateSolved() {
        this.solved++;
    }

    public void updateHeart() {
        this.heart++;
    }

    public void updateReviewCount() {
        this.reviewCount++;
    }

    public void updateMatchingCount() {
        this.matchingCount++;
    }
    public void setStarAverage(int reviewCount) {
        this.starAverage = BigDecimal.valueOf(this.star).divide(BigDecimal.valueOf(reviewCount),2, RoundingMode.HALF_UP);
    }

    public void subSolved() {
        this.solved--;
    }
}
