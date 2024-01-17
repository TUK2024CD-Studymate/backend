package com.studymate.backend.member.domain;

import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private Part part;
    @Enumerated(value = EnumType.STRING)
    private Interests interests;
    @Column(unique = true)
    private String nickname;
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private ProfileImg profileUrl;
    private Boolean isDeleted;
    private boolean activated;

    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    @ManyToMany
    private Set<Authority> authorities;

    public void delete() {
        this.isDeleted = true;
    }
}
