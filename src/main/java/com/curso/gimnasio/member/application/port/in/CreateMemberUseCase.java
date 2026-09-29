package com.curso.gimnasio.member.application.port.in;

import com.curso.gimnasio.member.domain.model.Member;

public interface CreateMemberUseCase {

    Member create(CreateMemberCommand command);
}
