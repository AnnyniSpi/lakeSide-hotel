package dev.annynispi.lakesidehotel.service;

import dev.annynispi.lakesidehotel.dto.RoomDto;
import dev.annynispi.lakesidehotel.exception.ResoursNotFoundException;
import dev.annynispi.lakesidehotel.model.BookedRoom;
import dev.annynispi.lakesidehotel.model.Room;
import dev.annynispi.lakesidehotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService{

    private final RoomRepository roomRepository;

    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws IOException, SQLException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }

        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        Optional<Room> room = roomRepository.findById(roomId);

        if (room.isEmpty()){
            throw new ResoursNotFoundException("Sorry, Room not found!");
        }
        Blob photoBlob = room.get().getPhoto();
        if (photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }

        return null;
    }
}
