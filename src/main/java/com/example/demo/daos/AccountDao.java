package com.example.demo.daos;

import com.example.demo.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*Le interfacce del Dao Layer devono estendere l'interfaccia JpaRepository,
 * che verrà implementata automaticamente da Spring all'avvio dell'applicazione.
 * Questo ci eviterà di dover creare le implementazioni di UserDao, AccountDao
 * e OperationDao, quindi non implementeremo delle classi UserDaoImpl,
 * AccountDaoImpl e OperationDaoImpl. I due parametri dell'interfaccia JpaRepository
 * sono l'Entity che ci interessa e il tipo del suo id.*/
public interface AccountDao extends JpaRepository<Account, String> {
    /*Query normale*/
    @Query(value = "SELECT * FROM accounts WHERE FK_USER = :user", nativeQuery = true)
    List<Account> getAllAccountsPerUser(@Param("user") String user);

    /*Named Query*/
    List<Account> findByFkUser(String fkUser);
}
