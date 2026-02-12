package com.ChatApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.KnownConnection;
import com.ChatApp.Entities.User;

@Repository
public interface  KnownConnectionRepository extends JpaRepository<KnownConnection,String>{

        // this is for getting connections for a user
        List<KnownConnection> findByUserOrderByIsFavouriteDescLastMessageAtDesc(User user);
        /*
                select * from connections where user_id='asdfasdklsdf' order by is_faviourite, last_message_at desc;
        */

        // this is to check if connection exists
        Optional<KnownConnection>findByUserAndContact(User user,User contact);

        // delete a connection by user and contact id
        void deleteByUserAndContact(User user,User contact);

        // who has this user as  a contact
        List<KnownConnection> findByContact(User contact);

        // get all block contacts
        List<KnownConnection>findByUserAndIsBlockedTrue(User user);

        // ckeck if the user has blocked a particular contact
        boolean exitsByUserAndContactAndIsBlockedTrue(User user,User contact);
}
    
