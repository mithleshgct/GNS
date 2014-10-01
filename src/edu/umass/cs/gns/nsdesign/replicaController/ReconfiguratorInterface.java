package edu.umass.cs.gns.nsdesign.replicaController;

import java.util.concurrent.ConcurrentHashMap;

import edu.umass.cs.gns.nsdesign.nodeconfig.GNSNodeConfig;

/**
@author V. Arun
 */
public interface ReconfiguratorInterface<NodeIDType> {
	public GNSNodeConfig getGnsNodeConfig();

	public ConcurrentHashMap<NodeIDType, Double> getNsRequestRates();

}
