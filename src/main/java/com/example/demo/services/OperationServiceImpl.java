package com.example.demo.services;

import com.example.demo.daos.AccountDao;
import com.example.demo.daos.OperationDao;
import com.example.demo.entities.Account;
import com.example.demo.entities.Operation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/*Oltre a @Service, possiamo notare l'aggiunta di @Transactional.
* @Transactional non è da utilizzare obbligatoriamente, ma è consigliato
* il suo utilizzo quando il @Service deve eseguire un accesso massivo
* ai database attraverso i dao. Utilizzando @Transactional permettiamo a
* Spring di automatizzare il processo delle transazioni e gestirle.
*/
@Service @Transactional
public class OperationServiceImpl implements OperationService{

    @Autowired
    AccountDao accountDao;

    @Autowired
    OperationDao operationDao;

    @Override
    public List<Operation> getAllOperationPerAccount(String accountId) {
        return operationDao.findAllOperationsByAccount(accountId);
    }

    @Override
    public List<Account> getAllAccountsPerUser(String userId) {
        return accountDao.getAllAccountsPerUser(userId);
    }

    @Override
    public Operation saveOperation(Operation operation) {

        if(operation.getDate() == null){
            operation.setDate(new Date());
        }

        return operationDao.save(operation);
    }
}
