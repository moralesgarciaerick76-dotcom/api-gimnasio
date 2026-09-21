package com.curso.gimnasio.controller;

import com.curso.gimnasio.dto.MemberDto;
import com.curso.gimnasio.service.MemberService;
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

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar socio")
    public MemberDto create(@Valid @RequestBody MemberDto request) {
        return memberService.create(request);
    }

    @GetMapping
    @Operation(summary = "Listar socios")
    public List<MemberDto> findAll() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar socio por id")
    public MemberDto findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @GetMapping("/by-email")
    @Operation(summary = "Buscar socio por email")
    public MemberDto findByEmail(@RequestParam String email) {
        return memberService.findByEmail(email);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar socio (solo si no tiene reservas)")
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }
}
