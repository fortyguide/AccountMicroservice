package com.example.demo.services;

import com.example.demo.entities.Account;
import com.example.demo.entities.Operation;

import java.util.List;

public interface OperationService {

    List<Operation> getAllOperationPerAccount(String accountId);

    List<Account>getAllAccountsPerUser(String userId);

    Operation saveOperation(Operation operation);
}
