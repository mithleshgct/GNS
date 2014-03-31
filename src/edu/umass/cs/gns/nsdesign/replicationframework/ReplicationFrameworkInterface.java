package edu.umass.cs.gns.nsdesign.replicationframework;

import edu.umass.cs.gns.exceptions.FieldNotFoundException;
import edu.umass.cs.gns.nsdesign.recordmap.ReplicaControllerRecord;
import edu.umass.cs.gns.nsdesign.replicaController.ReplicaController;

import java.util.Set;



/*************************************************************
 * This is a replication framework interface. Classes that 
 * want to be part of the replication framework need to 
 * implement this interface.
 * 
 * @author Hardeep Uppal
 ************************************************************/
public interface ReplicationFrameworkInterface {

  public final static int NUM_RETRY = 1000;

  /*************************************************************
   * Returns a new set of active nameservers for a name
   * @param record ReplicaControllerRecord for this name.
   * @param numReplica Number of replicas
   * @return A Set containing active nameservers id.
   ************************************************************/
  public Set<Integer> newActiveReplica(ReplicaController rc, ReplicaControllerRecord record, int numReplica, int count) throws FieldNotFoundException;
}