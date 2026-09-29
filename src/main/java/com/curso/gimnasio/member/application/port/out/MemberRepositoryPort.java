package com.curso.gimnasio.member.application.port.out;

import com.curso.gimnasio.member.domain.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryPort {

    Member save(Member member);

    List<Member> findAll();

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    void delete(Member member);
}
