package com.example.board.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity // 엔티티 테이블과 연결되는 클래스
@Setter // 멤버필드의 값을 저장하는 메소드
@Getter //멤버필드의 값을 외부로 전달
@AllArgsConstructor // 모든 멤버 필드들을 입력으로하는 생성자를 자동으로 만들어줌
@NoArgsConstructor // 가본 빈 생성자를 자동으로 만들어줌
@Table(name="board") // 테이블 user와 연동
@ToString //객체의 내용을 문자열로 만들어줌
@Builder
public class MyBoard {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment
    private Long Id; // 이름

    @Column(nullable = false) // 널이 가능하냐? 불가능하다 펄스
    private String title;

    @Column(nullable = false, length = 500)
    private String content;


    @ManyToOne // 다 대 일
    @JoinColumn(name = "writer")
    private UserEntity userEntity; //참조만 하고 있는것

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime regDate; // 자동 입력

    @Builder.Default
    private Long hit = 0L; //조회 : hit
    @Builder.Default
    private Long like_cnt = 0L; // 좋아요 : 0
    @Builder.Default
    private Long dislike = 0L; // 싫어요 : 0
    @Builder.Default
    private Boolean deleted = false;
}
