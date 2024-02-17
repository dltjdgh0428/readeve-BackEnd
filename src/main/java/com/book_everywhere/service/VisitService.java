package com.book_everywhere.service;

import com.book_everywhere.domain.pin.Pin;
import com.book_everywhere.domain.pin.PinRepository;
import com.book_everywhere.domain.user.User;
import com.book_everywhere.domain.user.UserRepository;
import com.book_everywhere.domain.visit.Visit;
import com.book_everywhere.domain.visit.VisitRepository;
import com.book_everywhere.web.dto.visit.VisitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class VisitService {
    private final UserRepository userRepository;
    private final PinRepository pinRepository;
    private final VisitRepository visitRepository;

    @Transactional
    public void 독후감쓰기전방문등록(Long pinId, VisitDto visitDto, @AuthenticationPrincipal OAuth2User oAuth2User) {
        //review가 올라가기전 visit에 등록되어있는지 확인후 없다면 visit등록
        long socialId = (Long) oAuth2User.getAttributes().get("id");
        User user = userRepository.findBySocialId(socialId).orElseThrow();
        Pin pin = pinRepository.findByPinId(pinId).orElseThrow();

        Optional<Visit> visited = visitRepository.findByUserAndPin(user, pin);

        if (visited.isEmpty()) {
            Visit visit = visitDto.toEntity(user, pin);
            visitRepository.save(visit);
        }
    }

}