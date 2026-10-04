package com.curso.gimnasio.booking.application.port.out;

import com.curso.gimnasio.member.domain.model.Member;

import java.util.Optional;

public interface BookingMemberPort {

    Optional<Member> findByIdForUpdate(Long memberId);
}
