package com.god.kahit.Repository;

import com.god.kahit.model.Debuff;

public class Repository {
    private static Repository repository;
    
    public static Repository getInstance() {
        if(repository == null){
            repository = new Repository();
        }
        return repository;
    }

    public void debuffPlayer(Debuff item) {
    }
}
