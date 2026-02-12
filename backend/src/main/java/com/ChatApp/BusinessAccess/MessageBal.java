package  com.ChatApp.BusinessAccess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ChatApp.DataAccess.MessageDal;
import com.ChatApp.Entities.Message;
import com.mailjet.client.resource.Sender;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MessageBal {

    private final MessageDal MessageDal;
    @Autowired
    public MessageBal(MessageDal messageDal){
        this.MessageDal=messageDal;
    }

    public List<Message>saveItAllMessages(){
        return MessageDal.save(message);
    }

    public List<Message>messageSender(){
        return MessageDal.findBySender(Sender);
    }

    public List<Message>messageReciever(){
        return MessageDal.findByReceiver(receiver);
    }
}
