package com.book_everywhere.domain.data.controller;

import com.book_everywhere.domain.data.dto.AllDataDto;
import com.book_everywhere.domain.data.service.DataService;
import com.book_everywhere.common.dto.CMRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @GetMapping("/api/data/all")
    public CMRespDto<?> findAllData(@RequestParam Boolean isPrivate) {
        List<AllDataDto> result = dataService.모든공유또는개인데이터조회(isPrivate);
        return new CMRespDto<>(HttpStatus.OK, result,"모든 데이터 조회 성공");
    }

    @GetMapping("/api/data/all/{userId}")
    public CMRespDto<?> findAllData(@PathVariable Long userId) {
        List<AllDataDto> result = dataService.유저독후감조회(userId);
        return new CMRespDto<>(HttpStatus.OK, result,"모든 데이터 조회 성공");
    }
}
