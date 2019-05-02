package zebraservice.bot;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import zebraservice.model.TelegramUser;
import zebraservice.repositories.TelegramUserRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class ZebraServiceBot extends TelegramLongPollingBot {
    private static final String TOKEN = "825425001:AAFebWLgj2IzCMsYqeU1ahnZMcaU6gq9i-I";
    private static final String BOTNAME = "@testbotkrambot";

    @Autowired
    private TelegramUserRepository userRepository;

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return BOTNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        Message message;
        for(Update curUpd:updates){
            message = curUpd.getMessage();

            if(message!= null){

                TelegramUser telegramUser = userRepository.findTelegramUserByTelegramCode(message.getFrom().getId().toString());
                if(telegramUser == null){
                    telegramUser = new TelegramUser(message.getFrom());
                    userRepository.save(telegramUser);
                }

                //TODO - если пользователь заблокирован, отправить ему сообщение -

                if(message.getText().equals("Наша визитка")){
                    sendCard(message);
                }else if(message.getText().equals("Заказ услуги")){
                    sendAnswer(message);
                }else if(message.getText().equals("/start")){
                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append("Вас приветствует \"ZEBRA Service Centre\"");
                    stringBuilder.append("\n");
                    stringBuilder.append("Мы Выполняем:");
                    stringBuilder.append("\n");
                    stringBuilder.append(EmojiParser.parseToUnicode(":printer:"));
                    stringBuilder.append(" Качественную заправку и восстановление картриджей.");
                    stringBuilder.append("\n");
                    stringBuilder.append(EmojiParser.parseToUnicode(":computer:"));
                    stringBuilder.append(" Ремонт компьютеров и ноутбуков, периферии и другой офисной техники.");
                    stringBuilder.append("\n");
                    stringBuilder.append(EmojiParser.parseToUnicode(":wrench:"));
                    stringBuilder.append(" Ремонт ИБП (источников бесперебойного питания).");
                    stringBuilder.append("\n");
                    stringBuilder.append(EmojiParser.parseToUnicode(":point_up_2:"));
                    stringBuilder.append(" Помощь в установке программного обеспечения, драйверов.");
                    stringBuilder.append("\n");
                    stringBuilder.append(EmojiParser.parseToUnicode(":office:"));
                    stringBuilder.append(" Продажа офисной техники, комплектующих и расходных материалов под заказ.");
                    stringBuilder.append("\n");
                    stringBuilder.append(EmojiParser.parseToUnicode(":taxi:"));
                    stringBuilder.append(" Услуга по доставке и заправке картриджей производится в короткие сроки.");
                    stringBuilder.append("\n");
                    stringBuilder.append("Это быстро и удобно для Вас.");
                    stringBuilder.append("\n");
                    stringBuilder.append(EmojiParser.parseToUnicode(":thumbsup:"));
                    stringBuilder.append(" Работаем на качество и совесть!");

                    sendMsg(message,stringBuilder.toString());
                }else {
                    if (!telegramUser.isAdmin() && !telegramUser.isBlocked()){
                        sendMessageToAmins(telegramUser,message.getText().toString());
                    }
                    sendMsg(message,"Выберите действие!");
                }
            }
        }
    }

    private void sendCard(Message message) {

        SendPhoto sdMessage = new SendPhoto();
        sdMessage.setChatId(message.getChatId().toString());
        try {
            sdMessage.setPhoto(new File("D:\\zebra.jpg"));
        }catch (Exception ex){
            ex.printStackTrace();
        }

        sdMessage.setReplyMarkup(getMarkUpMenu());
        try {
            execute(sdMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendAnswer(Message message){
        SendMessage sdMessage = new SendMessage();
        sdMessage.setChatId(message.getChatId().toString());
        sdMessage.setText("Введите номер телефона, мы Вам перезвоним!");

        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(message.getChatId().toString());
        sendChatAction.setAction(ActionType.TYPING);

        try {
            execute(sendChatAction);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        try {
            execute(sdMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup getMarkUpMenu(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Наша визитка");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Заказ услуги");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        return keyboardMarkup;
    }


    private void sendMsg(Message message,String txtMsg){
        SendMessage sdMessage = new SendMessage();
        sdMessage.setChatId(message.getChatId().toString());
        sdMessage.setReplyMarkup(getMarkUpMenu());
        sdMessage.setText(txtMsg);
        try {
            execute(sdMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    private void sendMessageToAmins(TelegramUser telegramUserFrom,String textMessage){
        List<TelegramUser> adminUsers = userRepository.findByIsAdminTrue();
        StringBuilder msgBuilder = new StringBuilder();

        if(telegramUserFrom.getCompanyName() != null){
            msgBuilder.append("Сообщение от [");
            msgBuilder.append(telegramUserFrom.getCompanyName());
            msgBuilder.append("]: ");
        }else {
            msgBuilder.append("Неизвестный пользователь [");
            msgBuilder.append(telegramUserFrom.getFirstName());
            msgBuilder.append(" ");
            msgBuilder.append(telegramUserFrom.getLastName());
            msgBuilder.append("]: ");
        }

        msgBuilder.append(textMessage);

        for (TelegramUser adminUser:adminUsers){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(adminUser.getTelegramCode());
            sendMessage.setText(msgBuilder.toString());
            try {
                execute(sendMessage);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToAllUsers(String messageText){
        List<TelegramUser> activeUsers = userRepository.findByIsBlockedFalse();

        for (TelegramUser activeUser:activeUsers){
            if(activeUser.getTelegramCode() != null) {
                SendMessage sdMessage = new SendMessage();
                sdMessage.setChatId(activeUser.getTelegramCode());
                sdMessage.setText(messageText);
                try {
                    execute(sdMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
