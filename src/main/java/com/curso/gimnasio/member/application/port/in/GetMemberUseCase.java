package com.curso.gimnasio.member.application.port.in;

import com.curso.gimnasio.member.domain.model.Member;

import java.util.List;

public interface GetMemberUseCase {

    Member findById(Long id);

    Member findByEmail(String email);

    List<Member> findAll();
}
