package com.ChatApp.BusinessAccess;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ChatApp.DataAccess.KnownConnectionDal;


import com.ChatApp.Entities.KnownConnection;
import com.ChatApp.Entities.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class KnownConnectionBal {

    private KnownConnectionDal knownConnectionDal;
    
    @Autowired
    public KnownConnectionBal(KnownConnectionDal _KnownConnectionDal){
        this.knownConnectionDal = _KnownConnectionDal;
    }

    public void updateConnectionOnMessage(User sender,User receiver){
        if (sender==null || receiver == null || sender.getId().equals(receiver.getId())) {
            return;
        } 
        Instant now =Instant.now();

        //sender connection
        KnownConnection senderConn=knownConnectionDal.findByUserAndContact(sender, receiver).orElseGet(()-> new KnownConnection(sender,receiver));
        senderConn.setLastMessageAt(now);
        knownConnectionDal.save(senderConn);

        //reciever connection
        KnownConnection receiverConn=knownConnectionDal.findByUserAndContact(receiver, sender).orElseGet(()-> new KnownConnection(receiver,sender));
        receiverConn.setLastMessageAt(now);
        knownConnectionDal.save(receiverConn);
    }

    

//get known connection (for sidebar)

    @Transactional(readOnly = true)
    public List<KnownConnection> getKnownConnections(User user) {
        return knownConnectionDal.findByUserOrderByfavoriteAndLastMessage(user);
    }

    //mark/unmark favourite

    public boolean toggleFavourite(User user,User contact,boolean favourite){
        Optional<KnownConnection>connectionOpt=knownConnectionDal.findByUserAndContact(user,contact);
        if(connectionOpt.isEmpty()) return false;
         KnownConnection connection =connectionOpt.get();
         connection.setIsFavorite(favourite);
        knownConnectionDal.save(connection);
        return  true;
        }

        //remove connection

        public void removeConnection(User user, User contact,boolean  favourite){
          knownConnectionDal.deletebyUserAndContact(user, contact);
        }

        //block user

        public boolean blockUser(User user,User contact){
            if(user==null || contact ==null) return false;
            KnownConnection connection=knownConnectionDal.findByUserAndContact(user, contact)
            .orElseGet(()->new KnownConnection(user,contact));

            connection.setIsBlocked(true);
            connection.setBlockedByUserId(user.getId());
            knownConnectionDal.save(connection);
            return true;
        }

        //unblock user

        public boolean unBlockUser(User user ,User contact){

            Optional<KnownConnection>connectionOpt=KnownConnectionDal.findByUserAndContaact(user,contact);
            if(connectionOpt.isEmpty()) return false;

            KnownConnection connection=connectionOpt.get();
            connection.setIsBlocked(false);
            connection.setBlockedByUserId(null);
            knownConnectionDal.save(connection);
            return true;
        }

        @Transactional(readOnly=true)
        public List<KnownConnection>getBlockedConnection(User user){
            return KnownConnectionDal.findBlockedConnection(user);
        }
}