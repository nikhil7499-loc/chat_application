package com.ChatApp.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ChatApp.Entities.KnownConnection;
import com.ChatApp.Entities.User;
import com.mailjet.client.resource.Contact;

@Repository
public interface  KnownConnectionRepository extends JpaRepository<KnownConnection,String>{

        List<KnownConnection> findByUserOrderByIsFavouriteDescLastMessageAtDesc(User user);

        Optional<KnownConnection>findByUserAndContact(User user,User contact);

        void deleteByUserAndContact(User user,User contact);
        List<KnownConnection>findByUseAndIsBlockedTrue(User user);
        boolean exitsByUserAndIsBlockedTrue(User user,User contact);
}
    
