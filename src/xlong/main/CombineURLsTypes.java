package xlong.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import xlong.data.Entity;
import xlong.data.IO.NTripleReader;
import xlong.data.IO.SubClassRelationReader;
import xlong.data.IO.TypeMapIO;
import xlong.data.IO.UrlTypePairIO;
import xlong.data.filter.ExistTypeFilter;
import xlong.data.filter.MultipleTypeFilter;
import xlong.data.filter.ExistUrlFilter;
import xlong.data.filter.SingleTypeFilter;
import xlong.data.processer.SimplifyProcesser;
import xlong.data.processer.Triple2PairProcesser;
import xlong.data.processer.UrlNormalizeProcesser;
import xlong.util.PropertiesUtil;

@SuppressWarnings("unused")


public class CombineURLsTypes {

	public static void main(String[] args) throws IOException {
		int maxLines = 100000000;
		try {
			PropertiesUtil.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PropertiesUtil.loadProperties();
		String typeFile = PropertiesUtil.getProperty("DBpedia_instance_types.nt");
		String urlFile = PropertiesUtil.getProperty("DBpedia_external_links.nt");
		ArrayList<String[]> types = null;
		ArrayList<String[]> urls = null;
		try {
			NTripleReader reader = new NTripleReader(typeFile, 
					new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
			types = reader.readAll(maxLines);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			NTripleReader reader = new NTripleReader(urlFile, 
					new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
			urls = reader.readAll(maxLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String ontologyFile = PropertiesUtil.getProperty("DBpedia_ontology.owl");
		Map<String, HashSet<String>> subClassMap = SubClassRelationReader.getSubClassOf(ontologyFile);
		System.out.println("get entities");
		Collection<Entity> entities = Entity.generateEntities(types, urls, subClassMap);
		System.out.println(entities.size());
		System.out.println("filt entities");
		
//		entities = SimpleEntity.filtEntities(entities, new ExistUrlFilter(new MultipleTypeFilter()));
		
//		entities = SimpleEntity.filtEntities(entities, new ExistUrlFilter(new ExistTypeFilter()));
//		System.out.println(entities.size());
//		entities = SimpleEntity.filtEntities(entities, new SingleTypeFilter());
		
		entities = Entity.filtEntities(entities, new ExistUrlFilter(new SingleTypeFilter()));
		
		System.out.println(entities.size());
//		for (SimpleEntity en:entities) {
//			System.out.println(en.toString());
//		}
		
//		System.out.println("get pairs");
//		ArrayList<String[]> pairs = SimpleEntity.entities2UrlTypePairs(entities);
//		System.out.println(pairs.size());
//		System.out.println("write pairs");
//		UrlTypePairIO.write(pairs, "result/pairs.txt");
		
		System.out.println("get typeMap");
		HashMap<String, HashSet<String>> pairs = Entity.entities2TypeMap(entities);
		System.out.println(pairs.size());
		System.out.println("write typeMap");
		TypeMapIO.write(pairs, "result/types");
	}

}
