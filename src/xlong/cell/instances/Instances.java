package xlong.cell.instances;

import java.io.IOException;


abstract public class Instances {
	
	abstract public void write(String filePath);
	
	abstract public void load(String filePath) throws IOException;
}
