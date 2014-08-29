package xlong.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

import xlong.cell.instances.FlatSparseVectorMultiLabelInstances;
import xlong.cell.instances.FlatStringMultiLabelInstances;
import xlong.converter.StringToSparseVectorConverter;
import xlong.converter.tokenizer.SingleWordTokenizer;
import xlong.data.Entity;
import xlong.data.NTripleReader;
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
		
		// Read data file.
		ArrayList<String[]> types = null;
		ArrayList<String[]> urls = null;
		NTripleReader typeReader = new NTripleReader(typeFile, new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
		types = typeReader.readAll();
		NTripleReader urlReader = new NTripleReader(urlFile, new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
		urls = urlReader.readAll();
		
		// Generate Entities.
		System.out.println("Generate Entities");
		Collection<Entity> entities = Entity.generateEntities(types, urls, null);
		types = null; //Release types;
		urls = null;  //Release urls;
		
		// Get url map.
		System.out.println("Get URL Map");
		HashMap<String, TreeSet<String>> urlMap =  Entity.entities2UrlMap(entities);
		
		// ----------------------------Instance convert---------------------------------
		//Get FlatStringMultiLabelInstances
		System.out.println("Get String Instances");
		FlatStringMultiLabelInstances stringInstances = new FlatStringMultiLabelInstances(urlMap);
		
		//Convert to FlatSparseVectorMultiLabelInstances
		System.out.println("Convert");
		StringToSparseVectorConverter converter = new StringToSparseVectorConverter(new SingleWordTokenizer());
		FlatSparseVectorMultiLabelInstances sparseVectorInstances = converter.convert(stringInstances);
		
		System.out.println(sparseVectorInstances.size());
		
	}
}
