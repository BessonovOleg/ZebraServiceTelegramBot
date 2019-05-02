package zebraservice.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import zebraservice.model.TelegramUser;

import java.util.List;

public interface TelegramUserRepository extends JpaRepository<TelegramUser,Long>{
    public TelegramUser findTelegramUserByTelegramCode(String telegramCode);
}
