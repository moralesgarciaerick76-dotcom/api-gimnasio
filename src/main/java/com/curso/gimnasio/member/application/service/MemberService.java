package com.curso.gimnasio.member.application.service;

import com.curso.gimnasio.member.application.port.in.CreateMemberCommand;
import com.curso.gimnasio.member.application.port.in.CreateMemberUseCase;
import com.curso.gimnasio.member.application.port.in.DeleteMemberUseCase;
import com.curso.gimnasio.member.application.port.in.GetMemberUseCase;
import com.curso.gimnasio.member.application.port.out.MemberBookingsPort;
import com.curso.gimnasio.member.application.port.out.MemberRepositoryPort;
import com.curso.gimnasio.member.domain.exception.MemberBusinessRuleException;
import com.curso.gimnasio.member.domain.exception.MemberNotFoundException;
import com.curso.gimnasio.member.domain.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService implements CreateMemberUseCase, GetMemberUseCase, DeleteMemberUseCase {

    private final MemberRepositoryPort repository;
    private final MemberBookingsPort bookingsPort;

    public MemberService(MemberRepositoryPort repository, MemberBookingsPort bookingsPort) {
        this.repository = repository;
        this.bookingsPort = bookingsPort;
    }

    @Override
    @Transactional
    public Member create(CreateMemberCommand command) {
        String email = command.getEmail().trim();
        if (repository.existsByEmail(email)) {
            throw new MemberBusinessRuleException("Ya existe un socio con el email: " + email);
        }

        Member member = new Member();
        member.setName(command.getName().trim());
        member.setEmail(email);

        return repository.save(member);
    }

    @Override
    public Member findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Socio no encontrado: " + id));
    }

    @Override
    public Member findByEmail(String email) {
        return repository.findByEmail(email.trim())
                .orElseThrow(() -> new MemberNotFoundException("Socio no encontrado con email: " + email));
    }

    @Override
    public List<Member> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Member member = findById(id);
        if (bookingsPort.hasBookings(id)) {
            throw new MemberBusinessRuleException("No se puede eliminar al socio '" + member.getName()
                    + "' porque tiene reservas registradas");
        }
        repository.delete(member);
    }
}
