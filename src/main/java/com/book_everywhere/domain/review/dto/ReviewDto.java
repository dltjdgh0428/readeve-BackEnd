package com.book_everywhere.domain.review.dto;

import com.book_everywhere.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String writer;
    private String title;
    private String content;
    private boolean isPrivate;
    private Long likeCount;
    private boolean likeState;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static ReviewDto toDto(Review review, Long likeCount, boolean likeState) {
        return new ReviewDto(
                review.getId(),
                review.getWriter(),
                review.getTitle(),
                review.getContent(),
                review.isPrivate(),
                likeCount,
                likeState,
                review.getCreatedDate(),
                review.getUpdatedDate()
        );
    }
}
