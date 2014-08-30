package xlong.classifier;

import java.util.ArrayList;
import java.util.Collection;

import xlong.cell.SparseVector;
import xlong.cell.instances.SparseVectorMultiLabelFlatInstances;

public class SparseVectorMultiLabelFlatClassifier extends FlatClassifier<SparseVectorMultiLabelFlatInstances> {

	private int cntLabels;
	private String[] labels;
	private SparseVectorMultiLabelBinaryClassifer classifier;
	private SparseVectorMultiLabelBinaryClassifer[] classifiers;
	
	public SparseVectorMultiLabelFlatClassifier(SparseVectorMultiLabelBinaryClassifer classifier){
		this.classifier = classifier;
	}
	
	@Override
	public void train(SparseVectorMultiLabelFlatInstances instances) {
		labels = instances.getLabels().toArray(new String[0]);
		cntLabels = labels.length;
		classifiers = classifier.getInstances(cntLabels);
		for(int i = 0; i < cntLabels; i++){
			classifiers[i].setPositiveLabel(labels[i]);
			instances.startIterator();
			while(instances.hasNext()){
				classifiers[i].train(instances.next());
			}
		}
	}
	
	public Collection<String> classify(SparseVector vector) {
		Collection<String> labels = new ArrayList<String>();
		for(int i = 0; i < cntLabels; i++){
			if (classifiers[i].classify(vector)) {
				labels.add(classifiers[i].getPositiveLabel());
			}
		}
		return labels;
	}
	
	public double[] score(SparseVector vector) {
		double[] scores = new double[cntLabels];
		for(int i = 0; i < cntLabels; i++){
			scores[i] = classifiers[i].score(vector);
		}	
		return scores;
	}
	
}
