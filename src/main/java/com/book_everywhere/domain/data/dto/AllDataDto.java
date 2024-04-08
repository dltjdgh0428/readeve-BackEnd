package com.book_everywhere.domain.data.dto;
import com.book_everywhere.domain.book.dto.BookRespDto;
import com.book_everywhere.domain.pin.dto.PinRespDto;
import com.book_everywhere.domain.tag.dto.TagRespDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class AllDataDto {
    private Long reviewId;
    private Long socialId;
    //3월 2일 추가 모든 독후감 기록 페이지
    private String writer;
    private String title;
    private boolean isPrivate;
    private PinRespDto pinRespDto;
    private BookRespDto bookRespDto;
    private List<TagRespDto> tags;
    private String content;
    private LocalDateTime createdDate;

}

