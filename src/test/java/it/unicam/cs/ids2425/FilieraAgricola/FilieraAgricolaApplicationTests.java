//package it.unicam.cs.ids2425.FilieraAgricola;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class FilieraAgricolaApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//
//}
package it.unicam.cs.ids2425.FilieraAgricola;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// --- INIZIO MODIFICA CHIAVE ---

// @SpringBootTest dice a Spring di caricare l'intero contesto dell'applicazione.
@SpringBootTest
// @ActiveProfiles("test") dice a Spring di cercare e usare il file
// "application-test.properties" invece di quello di default.
@ActiveProfiles("test")

// --- FINE MODIFICA CHIAVE ---
class FilieraAgricolaApplicationTests {

	@Test
	void contextLoads() {
		// Questo test ora ha a disposizione un ambiente completo e funzionante,
		// con un database in-memory, e si avvierà senza errori.
	}

}