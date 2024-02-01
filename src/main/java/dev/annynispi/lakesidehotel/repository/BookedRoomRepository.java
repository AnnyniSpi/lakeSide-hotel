package dev.annynispi.lakesidehotel.repository;

import dev.annynispi.lakesidehotel.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookedRoomRepository  extends JpaRepository<BookedRoom, Long> {
}
