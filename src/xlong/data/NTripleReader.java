/**
 * Project : Classify URLs
 */
package xlong.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

import xlong.data.processer.SimplifyProcesser;
import xlong.data.processer.Triple2PairProcesser;
import xlong.data.processer.StringArrayProcesser;
import xlong.data.processer.UrlNormalizeProcesser;
import xlong.util.MyWriter;
import xlong.util.PropertiesUtil;

/**
 * Class for reading Ntriples.
 * 
 * @author Xiang Long (longx13@mails.tinghua.edu.cn)
 */
public class NTripleReader {
	/** The NxParser used to parse file */
	protected NxParser nxp;
	/** Counts of triples */
	protected int cnt;
	/** Output logs or not. */
	protected static boolean outputLogs = true;
	
	private StringArrayProcesser stringArrayProcesser;
	
	private static final int MAXLINE = 100000000;
	
	/**
	 * Constructor
	 * 
	 * @param filePath
	 *            the path of the file to read.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public NTripleReader(String filePath, StringArrayProcesser stringArrayProcesser) throws IOException {
		nxp = new NxParser(new FileInputStream(filePath), false);
		cnt = 0;
		this.stringArrayProcesser = stringArrayProcesser;
	}
	
	public NTripleReader(String filePath) throws IOException {
		this(filePath, null);
	}	

	/**
	 * Set output logs or not.
	 * 
	 * @param outputLogs
	 */
	public static void setOutputLogs(boolean outputLogsPar) {
		outputLogs = outputLogsPar;
	}

	/**
	 * Get next triple
	 * 
	 * @return next triple
	 */
	protected String[] getNextTriple() {
		while (nxp.hasNext()) {
			Node[] ns = nxp.next();
			if (ns.length == 3) {
				cnt++;
				return stringArrayProcesser.pipeProcessTriple(Nodes2Strings(ns));
			}
		}
		return null;
	}
	
	private String[] Nodes2Strings(Node[] ns) {
		int l = ns.length;
		String[] ss = new String[l];
		for (int i = 0; i < l; i++) {
			ss[i] = ns[i].toString();
		}
		return ss;
	}

	/**
	 * Read Ntriples and write result into a file.
	 * 
	 * @param outFile
	 *            the file output reading result.
	 * @param maxNum
	 *            the max number of triples to read.
	 */
	public void readAll(String outFile, int maxNum) {
		if (!MyWriter.setFile(outFile, false)) {
			System.err.println("MyWriter setFile fail.");
			System.exit(0);
		}

		String[] ns;
		while ((ns = getNextTriple()) != null && cnt <= maxNum) {
			for (int i = 0; i < ns.length-1; i++){
				MyWriter.write(ns[i] + " ");
			}
			MyWriter.writeln(ns[ns.length-1]);
		}
		if (outputLogs) {
			System.out.println("Read lines: " + Math.min(cnt, maxNum));
		}

		MyWriter.close();
	}
	
	public ArrayList<String[]> readAll(int maxNum) {
		String[] ns;
		ArrayList<String[]> triples = new ArrayList<String[]>();
		while ((ns = getNextTriple()) != null && cnt <= maxNum) {
			triples.add(ns);
		}
		if (outputLogs) {
			System.out.println("Read lines: " + Math.min(cnt, maxNum));
		}
		return triples;
	}
	
	public void readAll(String outFile){
		readAll(outFile, MAXLINE);
	}
	
	public ArrayList<String[]> readAll() {
		return readAll(MAXLINE);
	}

	/**
	 * Testing code
	 */
	public static void main(String[] args) {
		int maxLines = 100;
		try {
			PropertiesUtil.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		PropertiesUtil.loadProperties();
		String typeFile = PropertiesUtil.getProperty("DBpedia_instance_types.nt");
		String urlFile = PropertiesUtil.getProperty("DBpedia_external_links.nt");
		try {
			NTripleReader reader = new NTripleReader(typeFile, 
					new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
			reader.readAll("result/instance_types.txt", maxLines);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			NTripleReader reader = new NTripleReader(urlFile, 
					new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
			reader.readAll("result/external_links.txt", maxLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			NTripleReader reader = new NTripleReader(typeFile, 
					new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
			ArrayList<String[]> types = reader.readAll(maxLines);
			for (String[] ns:types) {
				System.out.println(ns[0] + " " + ns[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			NTripleReader reader = new NTripleReader(urlFile, 
					new SimplifyProcesser(new UrlNormalizeProcesser(new Triple2PairProcesser())));
			ArrayList<String[]> urls = reader.readAll(maxLines);
			for (String[] ns:urls) {
				System.out.println(ns[0] + " " + ns[1]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
