package xlong.main;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

import xlong.cell.OntologyTree;
import xlong.cell.instance.StringMultiLabelInstance;
import xlong.cell.instances.SparseVectorMultiLabelFlatInstances;
import xlong.cell.instances.SparseVectorMultiLabelTreeInstances;
import xlong.cell.instances.StringMultiLabelFlatInstances;
import xlong.converter.FlatToTreeInstancesConverter;
import xlong.converter.StringToSparseVectorConverter;
import xlong.converter.tokenizer.SingleWordTokenizer;
import xlong.data.Entity;
import xlong.data.NTripleReader;
import xlong.data.SubClassRelationReader;
import xlong.data.UrlEntityMapIO;
import xlong.data.UrlMapIO;
import xlong.data.filter.ExistTypeFilter;
import xlong.data.filter.ExistUrlFilter;
import xlong.data.processer.SimplifyProcesser;
import xlong.data.processer.Triple2PairProcesser;
import xlong.data.processer.UrlNormalizeProcesser;
import xlong.util.PropertiesUtil;

public class FlatClassification {
	public static void main(String[] args) throws IOException{
		
		// ----------------------------Data process---------------------------------
		// Get properties.
		PropertiesUtil.init();
		PropertiesUtil.loadProperties();
		String typeFile = PropertiesUtil.getProperty("DBpedia_instance_types.nt");
		String urlFile = PropertiesUtil.getProperty("DBpedia_external_links.nt");
		String ontologyFile = PropertiesUtil.getProperty("DBpedia_ontology.owl");
		String typePairFile = "result/typePair.txt";
		String urlPairFile = "result/urlPair.txt";
		
		Collection<Entity> entities;
		HashMap<String, TreeSet<String>> urlMap;
		HashMap<String, TreeSet<Entity>> urlEntityMap;
		StringMultiLabelFlatInstances stringInstances;
		SparseVectorMultiLabelFlatInstances sparseVectorInstances;
		
//		// Read data file.
//		NTripleReader typeReader = new NTripleReader(typeFile, new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
//		NTripleReader urlReader = new NTripleReader(urlFile, new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
//		typeReader.readAll(typePairFile);
//		urlReader.readAll(urlPairFile);
////		ArrayList<String[]> types = typeReader.readAll();
////		ArrayList<String[]> urls = urlReader.readAll();
//
//		
//		// Generate Entities.
//		System.out.println("Generate Entities");
////		entities = Entity.generateEntities(types, urls, null);
////		types = null; //Release types;
////		urls = null;  //Release urls;
//		entities = Entity.generateEntities(typePairFile, urlPairFile);
//		System.out.println(entities.size());
//		entities = Entity.filtEntities(entities, new ExistUrlFilter(new ExistTypeFilter()));
//		System.out.println(entities.size());
//		Entity.write(entities, "result/entities.txt");	
		entities = null;
		entities = Entity.read("result/entities.txt");
		System.out.println(entities.size());
		
		// Get UrlEntity map. For test.
		urlEntityMap = Entity.entities2UrlEntityMap(entities);
		UrlEntityMapIO.writeOverlapUrl(urlEntityMap, "result/overlap.txt");
		
//		// Get url map.
//		System.out.println("Get URL Map");
//		urlMap =  Entity.entities2UrlMap(entities);
//		entities = null;
//		System.out.println(urlMap.size());
//		UrlMapIO.write(urlMap, "result/UrlMap.txt");
//		urlMap = null;
//		urlMap = UrlMapIO.read("result/UrlMap.txt");
//		System.out.println(urlMap.size());
//		
//		
//		// ----------------------------Instance convert---------------------------------
//		//Get FlatStringMultiLabelInstances
//		System.out.println("Get String Instances");
//		stringInstances = new StringMultiLabelFlatInstances(urlMap);
//		
//		//Convert to FlatSparseVectorMultiLabelInstances
//		System.out.println("Convert");
//		StringToSparseVectorConverter converter = new StringToSparseVectorConverter(new SingleWordTokenizer());
//		sparseVectorInstances = converter.convert(stringInstances);
//		stringInstances = null;
//		
//		System.out.println(converter.getDictionary().size());
//		System.out.println(sparseVectorInstances.size());
//		
//		sparseVectorInstances.startIterator();
//		System.out.println(sparseVectorInstances.next().toString());
//		
//		sparseVectorInstances.write("result/sparseVectors.txt");
//		sparseVectorInstances = new SparseVectorMultiLabelFlatInstances();
//		sparseVectorInstances.load("result/sparseVectors.txt");
//		System.out.println(sparseVectorInstances.size());
//		
//		Map<String, HashSet<String>> subClassOfMap = SubClassRelationReader.getSubClassOf(ontologyFile);	
//		OntologyTree ontology = OntologyTree.getTree(subClassOfMap);	
//		
//		SparseVectorMultiLabelTreeInstances treeInstances = new SparseVectorMultiLabelTreeInstances();
//		FlatToTreeInstancesConverter.convert(sparseVectorInstances, treeInstances, ontology);
//		
//		System.out.println(treeInstances.size());
		
	}
}
