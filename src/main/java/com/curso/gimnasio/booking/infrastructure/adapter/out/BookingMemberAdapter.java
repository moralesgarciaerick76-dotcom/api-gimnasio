package com.curso.gimnasio.booking.infrastructure.adapter.out;

import com.curso.gimnasio.booking.application.port.out.BookingMemberPort;
import com.curso.gimnasio.member.domain.model.Member;
import com.curso.gimnasio.member.infrastructure.adapter.out.MemberJpaRepository;
import com.curso.gimnasio.member.infrastructure.adapter.out.MemberPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BookingMemberAdapter implements BookingMemberPort {

    private final MemberJpaRepository memberJpaRepository;

    public BookingMemberAdapter(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return memberJpaRepository.findById(memberId).map(MemberPersistenceMapper::toDomain);
    }
}
