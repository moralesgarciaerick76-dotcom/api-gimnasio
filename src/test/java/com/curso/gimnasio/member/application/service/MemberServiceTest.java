package com.curso.gimnasio.member.application.service;

import com.curso.gimnasio.member.application.port.in.CreateMemberCommand;
import com.curso.gimnasio.member.application.port.out.MemberBookingsPort;
import com.curso.gimnasio.member.application.port.out.MemberRepositoryPort;
import com.curso.gimnasio.member.domain.exception.MemberBusinessRuleException;
import com.curso.gimnasio.member.domain.exception.MemberNotFoundException;
import com.curso.gimnasio.member.domain.model.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepositoryPort repository;

    @Mock
    MemberBookingsPort bookingsPort;

    @InjectMocks
    MemberService memberService;

    @Test
    void shouldThrowWhenMemberNotExists() {

        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.findById(99L));
    }

    @Test
    void shouldReturnMemberIfExists() {

        Long memberId = 10L;
        Member member = new Member(memberId, "Carla Mendoza", "carla.mendoza@mail.com");
        when(repository.findById(memberId)).thenReturn(Optional.of(member));

        Member result = memberService.findById(memberId);

        assertNotNull(result);
        assertEquals(memberId, result.getId());
        assertEquals("Carla Mendoza", result.getName());

        verify(repository).findById(eq(memberId));
        verify(repository, never()).save(any(Member.class));
    }

    @Test
    void shouldThrowWhenEmailNotExists() {
        when(repository.findByEmail("nadie@mail.com")).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.findByEmail("nadie@mail.com"));
    }

    @Test
    void shouldTrimEmailBeforeSearching() {
        Member member = new Member(1L, "Carla Mendoza", "carla.mendoza@mail.com");
        when(repository.findByEmail("carla.mendoza@mail.com")).thenReturn(Optional.of(member));

        Member result = memberService.findByEmail("  carla.mendoza@mail.com  ");

        assertEquals("carla.mendoza@mail.com", result.getEmail());
        verify(repository).findByEmail(eq("carla.mendoza@mail.com"));
    }

    @Test
    void shouldReturnAllMembers() {
        when(repository.findAll()).thenReturn(List.of(
                new Member(1L, "Carla", "carla@mail.com"),
                new Member(2L, "Diego", "diego@mail.com")));

        List<Member> result = memberService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldSaveMemberOnce() {

        Member saved = new Member(10L, "Carla Mendoza", "carla.mendoza@mail.com");
        when(repository.existsByEmail("carla.mendoza@mail.com")).thenReturn(false);
        when(repository.save(any(Member.class))).thenReturn(saved);

        Member result = memberService.create(new CreateMemberCommand("Carla Mendoza", "carla.mendoza@mail.com"));

        assertEquals(10L, result.getId());
        verify(repository, times(1)).save(any(Member.class));
    }

    @Test
    void shouldTrimNameAndEmailWhenCreating() {
        when(repository.existsByEmail("carla.mendoza@mail.com")).thenReturn(false);
        when(repository.save(any(Member.class))).thenAnswer(inv -> inv.getArgument(0));

        memberService.create(new CreateMemberCommand("  Carla Mendoza  ", "  carla.mendoza@mail.com "));

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(repository).save(captor.capture());
        assertEquals("Carla Mendoza", captor.getValue().getName());
        assertEquals("carla.mendoza@mail.com", captor.getValue().getEmail());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {

        when(repository.existsByEmail("carla.mendoza@mail.com")).thenReturn(true);

        MemberBusinessRuleException ex = assertThrows(MemberBusinessRuleException.class,
                () -> memberService.create(new CreateMemberCommand("Otra Carla", "carla.mendoza@mail.com")));

        assertTrue(ex.getMessage().contains("carla.mendoza@mail.com"));
        verify(repository, never()).save(any(Member.class));
    }

    @Test
    void shouldDeleteMemberWithoutBookings() {
        Long memberId = 5L;
        Member member = new Member(memberId, "Diego Salazar", "diego.salazar@mail.com");
        when(repository.findById(memberId)).thenReturn(Optional.of(member));
        when(bookingsPort.hasBookings(memberId)).thenReturn(false);

        memberService.delete(memberId);

        verify(repository, times(1)).delete(member);
    }

    @Test
    void shouldNotDeleteMemberWithBookings() {
        Long memberId = 5L;
        Member member = new Member(memberId, "Diego Salazar", "diego.salazar@mail.com");
        when(repository.findById(memberId)).thenReturn(Optional.of(member));
        when(bookingsPort.hasBookings(memberId)).thenReturn(true);

        assertThrows(MemberBusinessRuleException.class, () -> memberService.delete(memberId));

        verify(repository, never()).delete(any(Member.class));
    }

    @Test
    void shouldThrowWhenDeletingMemberThatNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.delete(99L));

        verify(bookingsPort, never()).hasBookings(any());
        verify(repository, never()).delete(any(Member.class));
    }
}
