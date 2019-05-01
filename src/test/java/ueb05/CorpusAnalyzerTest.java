package ueb05;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
//test
class CorpusAnalyzerTest {

	@Test
	void countTheses() throws IOException {
		CorpusAnalyzer ca = new CorpusAnalyzer(CorpusReader.thesisTitleIterator());

		assertEquals(379, ca.countTheses());
	}

	@Test
	void averageThesisTitleLength() throws IOException {
		CorpusAnalyzer ca = new CorpusAnalyzer(CorpusReader.thesisTitleIterator());

		assertEquals(9, ca.averageThesisTitleLength());
	}

	@Test
	void uniqueFirstWords() throws IOException {
		CorpusAnalyzer ca = new CorpusAnalyzer(CorpusReader.thesisTitleIterator());

		List<String> ufw = ca.uniqueFirstWords();

		assertEquals(188, ufw.size());
		assertEquals("generisches", ufw.get(13));
		assertEquals("Workflowgetriebene", ufw.get(29));
		assertEquals("Testfallbasierte", ufw.get(49));
	}

	@Test
	void censoredIterator() throws IOException {
		CorpusAnalyzer ca = new CorpusAnalyzer(CorpusReader.thesisTitleIterator());

		Set<String> blacklist = new HashSet<>();

		blacklist.add("Konzeption");
		blacklist.add("Implementierung");
		blacklist.add("Entwicklung");

		List<String> li = new LinkedList<>();
		Iterator<String> it = ca.censoredIterator(blacklist);
		while (it.hasNext())
			li.add(it.next());

		assertTrue(li.contains("Analyse und *************** einer Persistenzschicht am konkreten Beispiel einer Webanwendung"));
		assertFalse(li.contains("Analyse und Implementierung einer Persistenzschicht am konkreten Beispiel einer Webanwendung"));
		assertTrue(li.contains("*********** eines Frameworks für Client Server Architekturen basierend auf WPF und WCF und dem Halvotec Framework"));
		assertFalse(li.contains("Entwicklung eines Frameworks für Client Server Architekturen basierend auf WPF und WCF und dem Halvotec Framework"));
		assertTrue(li.contains("********** und *************** einer fachlichen Oberfläche für die Einbindung von SAP Businessobjekten an ein bestehendes BI Modellierungstool"));
		assertFalse(li.contains("Konzeption und Implementierung einer fachlichen Oberfläche für die Einbindung von SAP Businessobjekten an ein bestehendes BI Modellierungstool"));
	}

	@Test
	void normalizedTheses() throws IOException {
		CorpusAnalyzer ca = new CorpusAnalyzer(CorpusReader.thesisTitleIterator());

		Map<String, String> blacklist = new HashMap<>();

		blacklist.put("Konzeption", "Fantasie");
		blacklist.put("Implementierung", "Verbrechen");
		blacklist.put("Entwicklung", "Vergehen");

		List<String> li = ca.normalizedTheses(blacklist);
		
		assertTrue(li.contains("Analyse und Verbrechen einer Persistenzschicht am konkreten Beispiel einer Webanwendung"));
		assertFalse(li.contains("Analyse und Implementierung einer Persistenzschicht am konkreten Beispiel einer Webanwendung"));
		assertTrue(li.contains("Vergehen eines Frameworks für Client Server Architekturen basierend auf WPF und WCF und dem Halvotec Framework"));
		assertFalse(li.contains("Entwicklung eines Frameworks für Client Server Architekturen basierend auf WPF und WCF und dem Halvotec Framework"));
		assertTrue(li.contains("Fantasie und Verbrechen einer fachlichen Oberfläche für die Einbindung von SAP Businessobjekten an ein bestehendes BI Modellierungstool"));
		assertFalse(li.contains("Konzeption und Implementierung einer fachlichen Oberfläche für die Einbindung von SAP Businessobjekten an ein bestehendes BI Modellierungstool"));
	}
}