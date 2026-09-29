package com.curso.gimnasio.member.infrastructure.adapter.out;

import com.curso.gimnasio.member.application.port.out.MemberBookingsPort;
import com.curso.gimnasio.repository.BookingRepository;
import org.springframework.stereotype.Component;

@Component
public class MemberBookingsAdapter implements MemberBookingsPort {

    private final BookingRepository bookingRepository;

    public MemberBookingsAdapter(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public boolean hasBookings(Long memberId) {
        return bookingRepository.existsByMemberId(memberId);
    }
}
