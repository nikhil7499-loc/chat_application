package com.ChatApp.BusinessAccess;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ChatApp.DataAccess.KnownConnectionDal;

import com.ChatApp.Entities.KnownConnection;
import com.ChatApp.Entities.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class KnownConnectionBal {

    private KnownConnectionDal KnownConnectionDal;
    
    @Autowired
    public KnownConnectionBal(KnownConnectionDal _KnownConnectionDal){
        this.KnownConnectionDal = _KnownConnectionDal;
    }

    public void updateConnectionOnMessage(User sender,User receiver){
        if (sender==null || receiver == null || sender.getId().equals(receiver.getId())) {
            return;
        } 
        Instant now =Instant.now();

        //sender connection
        KnownConnection senderConn=KnownConnectionDal.findByUserAndContact(sender, receiver).orElseGet(()-> new KnownConnection(sender,receiver));
        senderConn.setLastMessageAt(now);
        KnownConnectionDal.save(senderConn);

        //reciever connection
        KnownConnection receiverConn=KnownConnectionDal.findByUserAndContact(receiver, sender).orElseGet(()-> new KnownConnection(receiver,sender));
        receiverConn.setLastMessageAt(now);
        KnownConnectionDal.save(receiverConn);
    }
    
}
