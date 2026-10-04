package com.curso.gimnasio.member.infrastructure.adapter.out;

import com.curso.gimnasio.booking.infrastructure.adapter.out.BookingJpaRepository;
import com.curso.gimnasio.member.application.port.out.MemberBookingsPort;
import org.springframework.stereotype.Component;

@Component
public class MemberBookingsAdapter implements MemberBookingsPort {

    private final BookingJpaRepository bookingJpaRepository;

    public MemberBookingsAdapter(BookingJpaRepository bookingJpaRepository) {
        this.bookingJpaRepository = bookingJpaRepository;
    }

    @Override
    public boolean hasBookings(Long memberId) {
        return bookingJpaRepository.existsByMemberId(memberId);
    }
}
