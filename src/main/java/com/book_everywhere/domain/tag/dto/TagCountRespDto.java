package com.book_everywhere.domain.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagCountRespDto {
    private String content;
    private Long count;
}
