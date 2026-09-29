package com.curso.gimnasio.member.infrastructure.adapter.in;

import com.curso.gimnasio.member.application.port.in.CreateMemberUseCase;
import com.curso.gimnasio.member.application.port.in.DeleteMemberUseCase;
import com.curso.gimnasio.member.application.port.in.GetMemberUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Socios")
public class MemberController {

    private final CreateMemberUseCase createMemberUseCase;
    private final GetMemberUseCase getMemberUseCase;
    private final DeleteMemberUseCase deleteMemberUseCase;

    public MemberController(CreateMemberUseCase createMemberUseCase,
                            GetMemberUseCase getMemberUseCase,
                            DeleteMemberUseCase deleteMemberUseCase) {
        this.createMemberUseCase = createMemberUseCase;
        this.getMemberUseCase = getMemberUseCase;
        this.deleteMemberUseCase = deleteMemberUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar socio")
    public MemberDto create(@Valid @RequestBody MemberDto request) {
        return MemberWebMapper.toDto(createMemberUseCase.create(MemberWebMapper.toCommand(request)));
    }

    @GetMapping
    @Operation(summary = "Listar socios")
    public List<MemberDto> findAll() {
        return MemberWebMapper.toDtoList(getMemberUseCase.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar socio por id")
    public MemberDto findById(@PathVariable Long id) {
        return MemberWebMapper.toDto(getMemberUseCase.findById(id));
    }

    @GetMapping("/by-email")
    @Operation(summary = "Buscar socio por email")
    public MemberDto findByEmail(@RequestParam String email) {
        return MemberWebMapper.toDto(getMemberUseCase.findByEmail(email));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar socio (solo si no tiene reservas)")
    public void delete(@PathVariable Long id) {
        deleteMemberUseCase.delete(id);
    }
}
