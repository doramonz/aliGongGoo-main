package com.doramonz.aligonggoo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    private String id;

    @Column(name = "user_loginAt")
    private LocalDateTime loginAt;

}
