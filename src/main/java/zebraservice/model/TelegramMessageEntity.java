package zebraservice.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
public class TelegramMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional=false, cascade = CascadeType.MERGE)
    @JoinColumn(name="user_id")
    private TelegramUser telegramUser;

    public TelegramUser getTelegramUser() {
        return this.telegramUser;
    }

    @CreationTimestamp
    private Date dateEvent;

    private String messgaBody;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setTelegramUser(TelegramUser telegramUser) {
        this.telegramUser = telegramUser;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getMessgaBody() {
        return messgaBody;
    }

    public void setMessgaBody(String messgaBody) {
        this.messgaBody = messgaBody;
    }

    @Override
    public String toString() {
        return "TelegramMessageEntity{" +
                "id=" + id +
                ", telegramUser=" + telegramUser +
                ", dateEvent=" + dateEvent +
                ", messgaBody='" + messgaBody + '\'' +
                '}';
    }
}
