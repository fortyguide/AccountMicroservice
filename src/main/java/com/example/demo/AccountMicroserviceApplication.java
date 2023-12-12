package com.example.demo;

import com.example.demo.daos.AccountDao;
import com.example.demo.daos.OperationDao;
import com.example.demo.daos.UserDao;
import com.example.demo.entities.Account;
import com.example.demo.entities.Operation;
import com.example.demo.entities.User;
import com.example.demo.utils.EncryptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;


@SpringBootApplication
public class AccountMicroserviceApplication implements CommandLineRunner {

	/*Con @Autowired vado a iniettare, laddove serve, le classi implementate
	* nel progetto Spring. Nel nostro caso abbiamo di seguito delle interfacce,
	* ma Spring non inietterà ovviamente le interfacce, ma le classi che le
	* implementano, che sono appunto invisibili al programmatore,
	* perchè sono state implementate automaticamente da Spring*/
	@Autowired
	UserDao userDao;

	@Autowired
	AccountDao accountDao;

	@Autowired
	OperationDao operationDao;

	@Autowired
	EncryptionUtils encryptionUtils;

	/* La classe Logger serve per loggare lo stato del sistema */
	private static final Logger log = LoggerFactory.getLogger(AccountMicroserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AccountMicroserviceApplication.class, args);
	}

	/*Il metodo run(), ereditato dall'interfaccia CommandLineRunner, è il metodo
	* in cui possiamo implementare delle istruzioni che verranno eseguite prima
	* dell'avvio dell'applicazione*/
	@Override
	public void run(String... strings) throws Exception {
		//...
		log.info("Hello 1"); //Hello 1

		/*Il metodo save() è uno di quei metodi che appartengono all'interfaccia
		* JpaRepository, utilizzata per estendere le nostre 3 interfacce AccountDao,
		* OperationDao e UserDao. Con questo metodo possiamo riempire il nostro database H2,
		* che è un database in memoria. Un database in memoria è un tipo di database che
		* viene creato ogni qualvolta si avvia l'applicazione e viene distrutto quando
		* l'applicazione viene arrestata. Proprio per questo stiamo inserendo dei dati
		* nel metodo run(), perchè viene eseguito prima dell'avvio dell'applicazione,
		* per cui andrà a inserire i dati in fase di creazione del database H2*/

		/*UserDao con password non criptata*/
		//userDao.save(new User("RGNLSN87H13D761R", "Alessandro Argentieri", "Abba", "user"));
		//userDao.save(new User("FRNFBA85M08D761M", "Fabio Fiorenza", "melograno", "user"));
		//userDao.save(new User("DSTLCU89R52D761R", "Lucia Distante", "salut", "user"));

		/*UserDao con password criptata*/
		String encryptedPwd = encryptionUtils.encrypt("Abba");
		userDao.save(new User("RGNLSN87H13D761R", "Alessandro Argentieri", encryptedPwd, "user"));

		encryptedPwd = encryptionUtils.encrypt("melograno");
		userDao.save(new User("FRNFBA85M08D761M", "Fabio Fiorenza", encryptedPwd, "user"));

		encryptedPwd = encryptionUtils.encrypt("salut");
		userDao.save(new User("DSTLCU89R52D761R", "Lucia Distante", encryptedPwd, "user"));


		accountDao.save(new Account("cn4563df3", "RGNLSN87H13D761R", 3000.00));
		accountDao.save(new Account("cn7256su9", "RGNLSN87H13D761R", 4000.00));
		accountDao.save(new Account("cn6396dr7", "FRNFBA85M08D761M", 7000.00));
		accountDao.save(new Account("cn2759ds4", "DSTLCU89R52D761R", 2000.00));
		accountDao.save(new Account("cn2874da2", "DSTLCU89R52D761R", 8000.00));


		operationDao.save(new Operation("3452", new Date(),"Bonifico bancario", 100.00, "cn4563df3","cn4563df3"));
		operationDao.save(new Operation("3453", new Date(),"Pagamento tasse", -100.00, "cn4563df3","cn4563df3"));
		operationDao.save(new Operation("3454", new Date(),"Postagiro", 230.00, "cn4563df3","cn2759ds4"));
		operationDao.save(new Operation("3455", new Date(),"Vaglia postale", 172.00, "cn4563df3","cn4563df3"));
		operationDao.save(new Operation("3456", new Date(),"Acquisto azioni", -3400.00, "cn2759ds4",""));
		operationDao.save(new Operation("3457", new Date(),"Vendita azione", 100.00, "cn4563df3",""));
		operationDao.save(new Operation("3458", new Date(),"Prelevamento", -100.00, "cn4563df3",""));
		operationDao.save(new Operation("3459", new Date(),"Deposito", 1100.00, "cn4563df3",""));
		operationDao.save(new Operation("3460", new Date(),"Bonifico bancario", 100.00, "cn2874da2","cn4563df3"));
		operationDao.save(new Operation("3461", new Date(),"Bonifico bancario", 100.00, "cn4563df3","cn2874da2"));
		operationDao.save(new Operation("3462", new Date(),"Bonifico bancario", 100.00, "cn4563df3","cn4563df3"));
		operationDao.save(new Operation("3463", new Date(),"Postagiro", 230.00, "cn7256su9","cn2759ds4"));
		operationDao.save(new Operation("3464", new Date(),"Vaglia postale", 172.00, "cn4563df3","cn7256su9"));
		operationDao.save(new Operation("3465", new Date(),"Acquisto azioni", -3400.00, "cn7256su9",""));
	}
}
