package com.example.demo.daos;

import com.example.demo.entities.Operation;
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
public interface OperationDao extends JpaRepository<Operation, String> {
    /*Query normale*/
    @Query(value = "SELECT * FROM operations WHERE FK_ACCOUNT1 = :account " +
            "OR FK_ACCOUNT2 = :account", nativeQuery = true)
    List<Operation> findAllOperationsByAccount(@Param("account") String account);
}
