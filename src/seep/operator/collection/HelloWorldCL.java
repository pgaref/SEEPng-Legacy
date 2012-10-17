package seep.operator.collection;

import seep.comm.serialization.DataTuple;
import seep.operator.*;

public class HelloWorldCL extends Operator{

	public int att;

	public HelloWorldCL(int opID, int att){
		super(opID);
		this.att = att;
	}

	public void processData(DataTuple dt){
		System.out.println("HelloWorld Object loaded "+att);
	}

	@Override
	public boolean isOrderSensitive() {
		// TODO Auto-generated method stub
		return false;
	}
}