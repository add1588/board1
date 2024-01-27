package com.example.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.engine.jdbc.mutation.group.PreparedStatementGroup;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="articles")
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Builder.Default
    private String author ="";

    @Column
    @Builder.Default
    private String title = "";

    @Column
    @Builder.Default
    private String description = "";

    @Column
    @Builder.Default
    private String url = "";

    @Column
    @Builder.Default
    private String urlToImage="";

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime regDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private SourceEntity source;
}
