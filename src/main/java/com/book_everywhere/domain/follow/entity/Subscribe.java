package com.book_everywhere.domain.follow.entity;

import com.book_everywhere.common.entity.BaseTimeEntity;
import com.book_everywhere.domain.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(
        name="subscribe",
        uniqueConstraints={
                @UniqueConstraint(
                        name = "subscribe_uk",
                        columnNames={"fromUserId","toUserId"}
                )
        }
)
public class Subscribe extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscribe_id")
    private Long id;


    @JoinColumn(name = "fromUserId")
    @ManyToOne
    private User fromUser;  // ~~로부터


    @JoinColumn(name = "toUserId")
    @ManyToOne
    private User toUser; // ~~를

}
