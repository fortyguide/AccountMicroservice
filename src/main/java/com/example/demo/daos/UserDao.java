package com.example.demo.daos;

import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*Le interfacce del Dao Layer devono estendere l'interfaccia JpaRepository,
* che verrà implementata automaticamente da Spring all'avvio dell'applicazione.
* Questo ci eviterà di dover creare le implementazioni di UserDao, AccountDao
* e OperationDao, quindi non implementeremo delle classi UserDaoImpl,
* AccountDaoImpl e OperationDaoImpl. I due parametri dell'interfaccia JpaRepository
* sono l'Entity che ci interessa e il tipo del suo id.*/
public interface UserDao extends JpaRepository<User, String> {

    /*Di seguito abbiamo una Named Query, che serve per cercare uno,
    * o più record, in base alla colonna indicata nel nome della query
    * findByColonna */
    Optional<User> findById(String id);
}
