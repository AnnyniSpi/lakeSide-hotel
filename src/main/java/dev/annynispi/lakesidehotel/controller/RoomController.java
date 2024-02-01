package dev.annynispi.lakesidehotel.controller;

import dev.annynispi.lakesidehotel.dto.BookingDto;
import dev.annynispi.lakesidehotel.dto.RoomDto;
import dev.annynispi.lakesidehotel.exception.PhotoRetrievalException;
import dev.annynispi.lakesidehotel.model.BookedRoom;
import dev.annynispi.lakesidehotel.model.Room;
import dev.annynispi.lakesidehotel.service.IBookingService;
import dev.annynispi.lakesidehotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/rooms")
public class RoomController {

    private final IRoomService roomService;
    private final IBookingService bookingService;

    @PostMapping("/add/new-room")
    public ResponseEntity<RoomDto> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice")BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomDto dto = new RoomDto(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/types")
    public List<String> getRoomTypes(){
        return roomService.getAllRoomTypes();
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomDto> roomsDto = new ArrayList<>();
        for (Room room : rooms) {
            byte[] photosBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photosBytes != null && photosBytes.length > 0){
                String base64Photo = Base64.encodeBase64String(photosBytes);
                RoomDto roomDto = getRoomDto(room);
                roomDto.setPhoto(base64Photo);
                roomsDto.add(roomDto);
            }
        }
        return ResponseEntity.ok(roomsDto);
    }

    private RoomDto getRoomDto(Room room) {
        List<BookedRoom>  bookings = getAllBookingsByRoomId(room.getId());
        List<BookingDto> bookingInfo = bookings.stream()
                .map(bookedRoom -> new BookingDto(bookedRoom.getBookedRoomId(),
                        bookedRoom.getCheckInDate(),
                        bookedRoom.getCheckOutDate(),
                        bookedRoom.getBookingConfirmationCode())).toList();

        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if (photoBlob != null){
            try{
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            }catch (SQLException e){
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }

        return new RoomDto(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooking(), photoBytes, bookingInfo);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);

    }
}
