package com.ChatApp.BusinessAccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ChatApp.DataAccess.KnownConnectionDal;

import com.ChatApp.Entities.KnownConnection;

@Service
public class KnownConnectionBal {

    private KnownConnectionDal KnownConnectionDal;
    
    @Autowired
    public KnownConnectionBal(KnownConnectionDal _KnownConnectionDal){
        this.KnownConnectionDal = _KnownConnectionDal;
    }

    public KnownConnection createConnection(){
        
    }
    
}
