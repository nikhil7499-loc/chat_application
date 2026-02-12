package com.ChatApp.DataAccess;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.ChatApp.Entities.KnownConnection;
import com.ChatApp.Entities.User;
import com.ChatApp.Exceptions.DatabaseOperationException;
import com.ChatApp.Repository.KnownConnectionRepository;

import jakarta.transaction.Transactional;


@Component
@Transactional
public class KnownConnectionDal {
    private final KnownConnectionRepository knownConnectionRepository;

    public KnownConnectionDal(KnownConnectionRepository knownConnectionRepository){
        this.knownConnectionRepository=knownConnectionRepository;
    }
    public  KnownConnection save(KnownConnection connection){
        try {
            return knownConnectionRepository.save(connection);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseOperationException("constraints violation while saving known connection",e);
        }catch(Exception e){
            throw new DatabaseOperationException("failed to save known connection",e);
        }
    }

    public Optional<KnownConnection>findByUserAndContact(User user,User contact){
        try {
            return knownConnectionRepository.findByUserAndContact(user ,contact);
        } catch (Exception e) {
            throw  new DatabaseOperationException("Failed to fetch known connection",e);
        }
    }

    public List<KnownConnection>findByUserOrderByfavoriteAndLastMessage(User user){
        try {
            return knownConnectionRepository.findByUserOrderByIsFavouriteDescLastMessageAtDesc(user);
        } catch (Exception e) {
            throw new DatabaseOperationException("failed to fetch known connection for user",e);
        }
    }

    public void deletebyUserAndContact(User user,User contact){
        try{
            knownConnectionRepository.deleteByUserAndContact(user, contact);
        }catch(Exception e){
            throw new DatabaseOperationException("Failed to check block status",e);
        }
    }

    public List<KnownConnection> findBlockedConnections(User user){
        try{
            return knownConnectionRepository.findByUserAndIsBlockedTrue(user);
        }catch(Exception e){
            throw new DatabaseOperationException("Failed to fetch blocked connections",e);
        }
    }
}
