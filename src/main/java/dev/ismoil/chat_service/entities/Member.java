package dev.ismoil.chat_service.entities;


import dev.ismoil.chat_service.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Id
    Long id;


    String name;
    String nickname;
    String email;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String phoneNumber;
    @Column(nullable = true)
    LocalDate birthDay;
    String role;

}
