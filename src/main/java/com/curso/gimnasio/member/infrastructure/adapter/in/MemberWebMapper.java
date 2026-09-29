package com.curso.gimnasio.member.infrastructure.adapter.in;

import com.curso.gimnasio.member.application.port.in.CreateMemberCommand;
import com.curso.gimnasio.member.domain.model.Member;

import java.util.List;

public class MemberWebMapper {

    private MemberWebMapper() {
    }

    public static MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getName(), member.getEmail());
    }

    public static List<MemberDto> toDtoList(List<Member> members) {
        return members.stream()
                .map(MemberWebMapper::toDto)
                .toList();
    }

    public static CreateMemberCommand toCommand(MemberDto request) {
        return new CreateMemberCommand(request.getName(), request.getEmail());
    }
}
