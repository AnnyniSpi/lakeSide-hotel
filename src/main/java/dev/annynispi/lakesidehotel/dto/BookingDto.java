package dev.annynispi.lakesidehotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long BookedRoomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestFullName;
    private String guestEmail;
    private int NumOfAdults;
    private int NumOfChildren;
    private int totalNumberOfGuest;
    private String bookingConfirmationCode;
    private RoomDto room;

    public BookingDto(Long bookedRoomId, LocalDate checkInDate, LocalDate checkOutDate, String bookingConfirmationCode) {
        BookedRoomId = bookedRoomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }


}
