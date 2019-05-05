package zebraservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import zebraservice.model.TelegramMessageEntity;

import java.util.List;

public interface TelegramMessageRepository extends JpaRepository<TelegramMessageEntity,Long> {
    public List<TelegramMessageEntity> findAllByOrderByDateEvent();
}
