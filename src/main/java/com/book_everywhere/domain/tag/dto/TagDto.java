package com.book_everywhere.domain.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagDto {
    private String content;
    private boolean isSelected;
    private String category;
}
