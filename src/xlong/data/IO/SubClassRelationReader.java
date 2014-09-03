/**
 * Project : Classify URLs
 */
package xlong.data.IO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import xlong.util.MyWriter;
import xlong.util.PropertiesUtil;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Class for reading subclass of relationship from DBpedia ontology owl file.
 * 
 * @author Xiang Long (longx13@mails.tsinghua.edu.cn)
 */

public class SubClassRelationReader {

	private static final String typeStart = "http://dbpedia.org/ontology/";
	
	/**
	 * Read subclasof relationship from DBpedia ontology owl file.
	 * @param owlPath
	 *            ontology owl file path.
	 * @return the subclassof relationship map.
	 * @throws IOException
	 */
	public static Map<String, HashSet<String>> getSubClassOf(String owlPath)
			throws IOException {
		final Model dbpedia = ModelFactory.createOntologyModel();
		dbpedia.read(new FileInputStream(owlPath), "");
		final StmtIterator stmts = dbpedia.listStatements(null,
				RDFS.subClassOf, (RDFNode) null);
		Map<String, HashSet<String>> subClassMap = new HashMap<String, HashSet<String>>();

		while (stmts.hasNext()) {
			final Statement stmt = stmts.next();
			String sub = stmt.getSubject().toString().trim();
			String obj = stmt.getObject().toString().trim();
			if (!sub.equals(obj) && sub.startsWith(typeStart) && obj.startsWith(typeStart)) {
				sub = sub.substring(typeStart.length());
				obj = obj.substring(typeStart.length());
				if (sub.length() != 0 && obj.length() != 0){
					if (!subClassMap.containsKey(sub)) {
						subClassMap.put(sub, new HashSet<String>());
					}
					subClassMap.get(sub).add(obj);
				}
			}
		}
		return subClassMap;
	}


	/**
	 * Testing code
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		try {
			PropertiesUtil.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PropertiesUtil.loadProperties();
		String ontologyFile = PropertiesUtil.getProperty("DBpedia_ontology.owl");
		MyWriter.setFile("result/ontologySub.txt" , false);
		Map<String, HashSet<String>> subClassMap = getSubClassOf(ontologyFile);
		for (Entry<String, HashSet<String>> e : subClassMap.entrySet()) {
			String name = e.getKey();
			HashSet<String> fars = e.getValue();
			String str = name + " isSubClassOf ";
			for (String far : fars) {
				str += far + " ";
			}
			MyWriter.writeln(str);
		}
		MyWriter.close();
	}

}
