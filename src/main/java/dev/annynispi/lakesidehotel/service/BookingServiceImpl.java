package dev.annynispi.lakesidehotel.service;

import dev.annynispi.lakesidehotel.model.BookedRoom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements IBookingService{

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return null;
    }
}
