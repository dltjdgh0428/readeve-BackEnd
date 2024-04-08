package com.book_everywhere.domain.tag.service;

import com.book_everywhere.domain.review.dto.ReviewRespDto;
import com.book_everywhere.domain.tag.dto.TagDto;

import java.util.List;

public interface TaggedService {
    void 태그등록(ReviewRespDto reviewRespDto);
    List<TagDto> 모든태그조회();
    void 태그삭제(String address, Long socialId);
}
