package dev.ismoil.chat_service.repositories;

import dev.ismoil.chat_service.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository  extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
