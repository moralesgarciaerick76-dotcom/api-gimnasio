package com.curso.gimnasio.service;

import com.curso.gimnasio.dto.MemberDto;
import com.curso.gimnasio.entity.Member;
import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.repository.BookingRepository;
import com.curso.gimnasio.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BookingRepository bookingRepository;

    public MemberService(MemberRepository memberRepository, BookingRepository bookingRepository) {
        this.memberRepository = memberRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public MemberDto create(MemberDto request) {
        String email = request.getEmail().trim();
        if (memberRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessRuleException("Ya existe un socio con el email: " + email);
        }
        Member saved = memberRepository.save(new Member(request.getName().trim(), email));
        return toDto(saved);
    }

    public List<MemberDto> findAll() {
        return memberRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public MemberDto findById(Long id) {
        return toDto(findMember(id));
    }

    public MemberDto findByEmail(String email) {
        Member member = memberRepository.findByEmailIgnoreCase(email.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con email: " + email));
        return toDto(member);
    }

    @Transactional
    public void delete(Long id) {
        Member member = findMember(id);
        if (bookingRepository.existsByMemberId(id)) {
            throw new BusinessRuleException("No se puede eliminar al socio '" + member.getName()
                    + "' porque tiene reservas registradas");
        }
        memberRepository.delete(member);
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado: " + id));
    }

    private MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getEmail());
    }
}
