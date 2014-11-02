package uk.ac.imperial.lsds.seep.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.imperial.lsds.seep.infrastructure.EndPoint;

public class PhysicalSeepQuery {

	private List<PhysicalOperator> physicalOperators;
	private List<PhysicalOperator> sources;
	private PhysicalOperator sink;
	private Map<PhysicalOperator, List<PhysicalOperator>> instancesPerOriginalOp;
	
	private PhysicalSeepQuery(List<PhysicalOperator> physicalOperators, List<PhysicalOperator> pSources, PhysicalOperator pSink,
			Map<PhysicalOperator, List<PhysicalOperator>> instancesPerOriginalOp) {
		this.physicalOperators = physicalOperators;
		this.sources = pSources;
		this.sink = pSink;
		this.instancesPerOriginalOp = instancesPerOriginalOp;
	}
	
	public static PhysicalSeepQuery buildPhysicalQueryFrom(Set<SeepQueryPhysicalOperator> physicalOperators, 
			Map<PhysicalOperator, List<PhysicalOperator>> instancesPerOriginalOp, LogicalSeepQuery lsq) {
		// create physical connections
		for(Operator o : physicalOperators) {
			// update all downstream connections -> this will update the downstream's upstreams
			for(DownstreamConnection dc : o.downstreamConnections()) {
				// this will replace a still logical connection with a physical one
				// note that we don't need to update connectionType, this info is already there
				o.connectTo(dc.getDownstreamOperator(), dc.getStreamId());
			}
		}
		List<PhysicalOperator> pOps = new ArrayList<>(physicalOperators);
		List<PhysicalOperator> pSources = new ArrayList<>();
		PhysicalOperator pSink = null;
		for(Operator o : lsq.getSources()) {
			// if this necessary?
			for(Operator po : pOps){
				if(po.getOperatorId() == o.getOperatorId()){
					pSources.add((PhysicalOperator)po);
				}
				if(po.getOperatorId() == lsq.getSink().getOperatorId()){
					pSink = (PhysicalOperator)po;
				}
			}
		}
		return new PhysicalSeepQuery(pOps, pSources, pSink, instancesPerOriginalOp);
	}
	
	public List<PhysicalOperator> getOperators(){
		return physicalOperators;
	}
	
	public List<PhysicalOperator> getSources(){
		return sources;
	}
	
	public PhysicalOperator getSink(){
		return sink;
	}
	
	public PhysicalOperator getOperatorWithId(int opId){
		for(PhysicalOperator po : this.physicalOperators){
			if(po.getOperatorId() == opId){
				return po;
			}
		}
		return null;
	}
	
	public PhysicalOperator getOperatorLivingInExecutionUnitId(int euId){
		for(PhysicalOperator po : this.physicalOperators){
			if(po.getIdOfWrappingExecutionUnit() == euId){
				return po;
			}
		}
		return null;
	}
	
	public List<EndPoint> getMeshTopology(int euId){
		List<EndPoint> meshTopology = new ArrayList<>();
		for(PhysicalOperator po : physicalOperators) {
			if( (!isSource(po)) && (!isSink(po)) && po.getOperatorId() != euId){
				meshTopology.add(po.getWrappingEndPoint());
			}
		}
		return meshTopology;
	}
	
	private boolean isSource(PhysicalOperator po){
		for(PhysicalOperator src : sources){
			if(src.getOperatorId() == po.getOperatorId()){
				return true;
			}
		}
		return false;
	}
	
	private boolean isSink(PhysicalOperator po){
		return po.getOperatorId() == sink.getOperatorId();
	}
	
	public Set<Integer> getIdOfEUInvolved(){
		Set<Integer> ids = new HashSet<>();
		for(PhysicalOperator o : this.physicalOperators){
			ids.add(o.getIdOfWrappingExecutionUnit());
		}
		return ids;
	}

}