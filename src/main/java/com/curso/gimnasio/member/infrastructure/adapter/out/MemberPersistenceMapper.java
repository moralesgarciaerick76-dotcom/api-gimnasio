package com.curso.gimnasio.member.infrastructure.adapter.out;

import com.curso.gimnasio.member.domain.model.Member;
import com.curso.gimnasio.member.infrastructure.entities.MemberEntity;

public class MemberPersistenceMapper {

    private MemberPersistenceMapper() {
    }

    public static MemberEntity toEntity(Member member) {
        MemberEntity entity = new MemberEntity(member.getName(), member.getEmail());
        entity.setId(member.getId());
        return entity;
    }

    public static Member toDomain(MemberEntity entity) {
        return new Member(entity.getId(), entity.getName(), entity.getEmail());
    }
}
