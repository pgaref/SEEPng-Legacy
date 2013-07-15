package uk.co.imperial.lsds.seep.comm.serialization.controlhelpers;

import java.util.ArrayList;

public class InitRI {

	private int nodeId;
	private ArrayList<Integer> index;
	private ArrayList<Integer> key;
	
	public InitRI(){
		
	}
	
	public InitRI(int nodeId, ArrayList<Integer> index, ArrayList<Integer> key){
		this.nodeId = nodeId;
		this.index = index;
		this.key = key;
	}
	
	public int getNodeId() {
		return nodeId;
	}
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}
	public ArrayList<Integer> getIndex() {
		return index;
	}
	public void setIndex(ArrayList<Integer> index) {
		this.index = index;
	}
	public ArrayList<Integer> getKey() {
		return key;
	}
	public void setKey(ArrayList<Integer> key) {
		this.key = key;
	}

}