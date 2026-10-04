package com.curso.gimnasio.booking.application.service;

import com.curso.gimnasio.booking.application.port.in.BookingItemCommand;
import com.curso.gimnasio.booking.application.port.in.CreateBookingCommand;
import com.curso.gimnasio.booking.application.port.out.BookingClassPort;
import com.curso.gimnasio.booking.application.port.out.BookingMemberPort;
import com.curso.gimnasio.booking.application.port.out.BookingNotificationPort;
import com.curso.gimnasio.booking.application.port.out.BookingRepositoryPort;
import com.curso.gimnasio.booking.domain.model.Booking;
import com.curso.gimnasio.booking.domain.model.BookingItem;
import com.curso.gimnasio.booking.domain.model.BookingStatus;
import com.curso.gimnasio.exception.BusinessRuleException;
import com.curso.gimnasio.exception.ResourceNotFoundException;
import com.curso.gimnasio.gymclass.domain.model.GymClass;
import com.curso.gimnasio.member.domain.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepositoryPort repository;

    @Mock
    BookingMemberPort memberPort;

    @Mock
    BookingClassPort classPort;

    @Mock
    BookingNotificationPort notificationPort;

    @InjectMocks
    BookingService bookingService;

    Member carla;

    @BeforeEach
    void setUp() {
        carla = new Member(1L, "Carla Mendoza", "carla.mendoza@mail.com");
    }

    @Test
    void shouldThrowWhenMemberNotExists() {
        when(memberPort.findByIdForUpdate(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.create(command(99L, item(1L, 2))));

        verify(classPort, never()).findByIdForUpdate(anyLong());
        verify(repository, never()).save(any(Booking.class));
    }

    @Test
    void shouldLockMemberBeforeCountingActiveBookings() {
        when(memberPort.findByIdForUpdate(1L)).thenReturn(Optional.of(carla));
        when(repository.countByMemberAndStatus(1L, BookingStatus.ACTIVE)).thenReturn(3L);

        assertThrows(BusinessRuleException.class, () -> bookingService.create(command(1L, item(1L, 2))));

        InOrder inOrder = inOrder(memberPort, repository);
        inOrder.verify(memberPort).findByIdForUpdate(1L);
        inOrder.verify(repository).countByMemberAndStatus(1L, BookingStatus.ACTIVE);
        verify(classPort, never()).findByIdForUpdate(anyLong());
        verify(repository, never()).save(any(Booking.class));
    }

    @Test
    void shouldRejectRepeatedClassExceedingTenSpots() {
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> bookingService.create(command(1L, item(1L, 6), item(1L, 6))));

        assertTrue(ex.getMessage().contains("12"));
        verify(memberPort, never()).findByIdForUpdate(anyLong());
        verify(classPort, never()).findByIdForUpdate(anyLong());
    }

    @Test
    void shouldMergeRepeatedClassWithinLimit() {
        GymClass yoga = gymClass(1L, "Yoga Matutino", "25.00", 12);
        when(memberPort.findByIdForUpdate(1L)).thenReturn(Optional.of(carla));
        when(repository.countByMemberAndStatus(1L, BookingStatus.ACTIVE)).thenReturn(0L);
        when(classPort.findByIdForUpdate(1L)).thenReturn(Optional.of(yoga));
        when(repository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking result = bookingService.create(command(1L, item(1L, 4), item(1L, 4)));

        assertEquals(1, result.getItems().size());
        assertEquals(8, result.getItems().get(0).getSpots());
        assertEquals(4, yoga.getAvailableSpots());
        verify(classPort, times(1)).findByIdForUpdate(1L);
    }

    @Test
    void shouldRejectWhenClassHasNoSpots() {
        GymClass spinning = gymClass(4L, "Spinning Principiantes", "18.00", 0);
        when(memberPort.findByIdForUpdate(1L)).thenReturn(Optional.of(carla));
        when(repository.countByMemberAndStatus(1L, BookingStatus.ACTIVE)).thenReturn(0L);
        when(classPort.findByIdForUpdate(4L)).thenReturn(Optional.of(spinning));

        assertThrows(BusinessRuleException.class, () -> bookingService.create(command(1L, item(4L, 1))));

        verify(classPort, never()).updateAvailableSpots(any(GymClass.class));
        verify(repository, never()).save(any(Booking.class));
        verify(notificationPort, never()).sendConfirmation(any(Booking.class));
    }

    @Test
    void shouldCreateBookingLockingClassesInIdOrder() {
        GymClass yoga = gymClass(1L, "Yoga Matutino", "25.00", 12);
        GymClass spinning = gymClass(3L, "Spinning Intenso", "20.00", 15);
        when(memberPort.findByIdForUpdate(1L)).thenReturn(Optional.of(carla));
        when(repository.countByMemberAndStatus(1L, BookingStatus.ACTIVE)).thenReturn(2L);
        when(classPort.findByIdForUpdate(1L)).thenReturn(Optional.of(yoga));
        when(classPort.findByIdForUpdate(3L)).thenReturn(Optional.of(spinning));
        when(repository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking result = bookingService.create(command(1L, item(3L, 1), item(1L, 2)));

        InOrder inOrder = inOrder(classPort);
        inOrder.verify(classPort).findByIdForUpdate(1L);
        inOrder.verify(classPort).findByIdForUpdate(3L);

        assertEquals(BookingStatus.ACTIVE, result.getStatus());
        assertEquals(2, result.getItems().size());
        assertEquals(3, result.getTotalSpots());
        assertEquals(new BigDecimal("70.00"), result.getTotal());
        assertEquals(10, yoga.getAvailableSpots());
        assertEquals(14, spinning.getAvailableSpots());

        verify(classPort, times(2)).updateAvailableSpots(any(GymClass.class));
        verify(repository, times(1)).save(any(Booking.class));
        verify(notificationPort).sendConfirmation(result);
        verify(notificationPort).generateReceipt(result);
    }

    @Test
    void shouldCleanNotesWhenCreating() {
        GymClass yoga = gymClass(1L, "Yoga Matutino", "25.00", 12);
        when(memberPort.findByIdForUpdate(1L)).thenReturn(Optional.of(carla));
        when(repository.countByMemberAndStatus(1L, BookingStatus.ACTIVE)).thenReturn(0L);
        when(classPort.findByIdForUpdate(1L)).thenReturn(Optional.of(yoga));
        when(repository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking result = bookingService.create(new CreateBookingCommand(1L, "   ", List.of(item(1L, 1))));

        assertNull(result.getNotes());
    }

    @Test
    void shouldThrowWhenBookingNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.findById(99L));
    }

    @Test
    void shouldThrowWhenCancellingBookingThatNotExists() {
        when(repository.findByIdForUpdate(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.cancel(99L));

        verify(classPort, never()).findByIdForUpdate(anyLong());
    }

    @Test
    void shouldRejectCancelWhenAlreadyCancelled() {
        Booking cancelled = booking(10L, BookingStatus.CANCELLED, new BookingItem(1L, "Yoga Matutino", 2, new BigDecimal("25.00")));
        when(repository.findByIdForUpdate(10L)).thenReturn(Optional.of(cancelled));

        assertThrows(BusinessRuleException.class, () -> bookingService.cancel(10L));

        verify(classPort, never()).findByIdForUpdate(anyLong());
        verify(repository, never()).save(any(Booking.class));
    }

    @Test
    void shouldCancelLockingClassesAndReleasingSpots() {
        Booking active = booking(10L, BookingStatus.ACTIVE,
                new BookingItem(3L, "Spinning Intenso", 1, new BigDecimal("20.00")),
                new BookingItem(1L, "Yoga Matutino", 2, new BigDecimal("25.00")));
        GymClass yoga = gymClass(1L, "Yoga Matutino", "25.00", 10);
        GymClass spinning = gymClass(3L, "Spinning Intenso", "20.00", 14);
        when(repository.findByIdForUpdate(10L)).thenReturn(Optional.of(active));
        when(classPort.findByIdForUpdate(1L)).thenReturn(Optional.of(yoga));
        when(classPort.findByIdForUpdate(3L)).thenReturn(Optional.of(spinning));
        when(repository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        Booking result = bookingService.cancel(10L);

        InOrder inOrder = inOrder(classPort);
        inOrder.verify(classPort).findByIdForUpdate(1L);
        inOrder.verify(classPort).findByIdForUpdate(3L);

        assertEquals(BookingStatus.CANCELLED, result.getStatus());
        assertNotNull(result.getCancelledAt());
        assertEquals(12, yoga.getAvailableSpots());
        assertEquals(15, spinning.getAvailableSpots());

        ArgumentCaptor<Booking> captor = ArgumentCaptor.forClass(Booking.class);
        verify(repository).save(captor.capture());
        assertEquals(10L, captor.getValue().getId());
        verify(repository, never()).delete(any(Booking.class));
    }

    @Test
    void shouldReleaseSpotsWithLockWhenDeletingActiveBooking() {
        Booking active = booking(10L, BookingStatus.ACTIVE, new BookingItem(1L, "Yoga Matutino", 2, new BigDecimal("25.00")));
        GymClass yoga = gymClass(1L, "Yoga Matutino", "25.00", 10);
        when(repository.findByIdForUpdate(10L)).thenReturn(Optional.of(active));
        when(classPort.findByIdForUpdate(1L)).thenReturn(Optional.of(yoga));

        bookingService.delete(10L);

        assertEquals(12, yoga.getAvailableSpots());
        verify(classPort).updateAvailableSpots(yoga);
        verify(repository).delete(active);
    }

    @Test
    void shouldNotReleaseSpotsWhenDeletingCancelledBooking() {
        Booking cancelled = booking(10L, BookingStatus.CANCELLED, new BookingItem(1L, "Yoga Matutino", 2, new BigDecimal("25.00")));
        when(repository.findByIdForUpdate(10L)).thenReturn(Optional.of(cancelled));

        bookingService.delete(10L);

        verify(classPort, never()).findByIdForUpdate(anyLong());
        verify(classPort, never()).updateAvailableSpots(any(GymClass.class));
        verify(repository).delete(cancelled);
    }

    private static CreateBookingCommand command(Long memberId, BookingItemCommand... items) {
        return new CreateBookingCommand(memberId, "voy con un invitado", List.of(items));
    }

    private static BookingItemCommand item(Long classId, int spots) {
        return new BookingItemCommand(classId, spots);
    }

    private static GymClass gymClass(Long id, String name, String price, int availableSpots) {
        return new GymClass(id, name, "Lun 07:00", new BigDecimal(price), availableSpots, 1L, "Lucía Fernández");
    }

    private static Booking booking(Long id, BookingStatus status, BookingItem... items) {
        Booking booking = new Booking(id, LocalDateTime.now(), status,
                status == BookingStatus.CANCELLED ? LocalDateTime.now() : null,
                null, 1L, "Carla Mendoza", "carla.mendoza@mail.com");
        for (BookingItem item : items) {
            booking.addItem(item);
        }
        return booking;
    }
}
