package com.example.springfsd.app.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "date_published", nullable = false)
    private LocalDateTime datePublished;

    @Column(name = "author_id", nullable = false)
    private Long authorId;

    // Define the relationship with Like
    @OneToMany(mappedBy = "postId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes; // Add this line to fetch likes for a post

}
