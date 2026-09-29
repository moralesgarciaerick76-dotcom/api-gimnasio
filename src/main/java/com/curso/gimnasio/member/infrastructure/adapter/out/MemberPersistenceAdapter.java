package com.curso.gimnasio.member.infrastructure.adapter.out;

import com.curso.gimnasio.member.application.port.out.MemberRepositoryPort;
import com.curso.gimnasio.member.domain.model.Member;
import com.curso.gimnasio.member.infrastructure.entities.MemberEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MemberPersistenceAdapter implements MemberRepositoryPort {

    private final MemberJpaRepository memberJpaRepository;

    public MemberPersistenceAdapter(MemberJpaRepository memberJpaRepository) {
        this.memberJpaRepository = memberJpaRepository;
    }

    @Override
    public Member save(Member member) {
        MemberEntity saved = memberJpaRepository.save(MemberPersistenceMapper.toEntity(member));
        return MemberPersistenceMapper.toDomain(saved);
    }

    @Override
    public List<Member> findAll() {
        return memberJpaRepository.findAll().stream()
                .map(MemberPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id).map(MemberPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmailIgnoreCase(email).map(MemberPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public void delete(Member member) {
        memberJpaRepository.deleteById(member.getId());
    }
}
