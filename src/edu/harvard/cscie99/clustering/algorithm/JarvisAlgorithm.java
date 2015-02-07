package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.InputParamEnum;

public abstract class JarvisAlgorithm<E extends Object> implements IClusterAlgo<E> {

	public final static int PRECISION = 1;

	public abstract List<String> getRowLabels(E data,	Map<String, Object> clusterParams);
	public abstract Number getDistance(E data, List<String> rowKeys , int fromIdx, int toIdx);
	
	
	@Override
	public ClusteringResult cluster(E data,
			Map<String, Object> clusterParams) {
		
		final int numNeighbors = Integer.valueOf(clusterParams.get(InputParamEnum.IN_NUM_NEIGH.value()).toString()); // TODO check for exception
		final int commonNeighbors = Integer.valueOf(clusterParams.get(InputParamEnum.IN_COMM_NEIGH.value()).toString()); // TODO check for exception
		final List<String> rowKeys = getRowLabels(data, clusterParams);
		final int rows = rowKeys.size();
		
		ClusteringResult results = new ClusteringResult(rows,rowKeys);
		List<ClusterJarvis> clusters = new ArrayList<ClusterJarvis>();
		
		// initialize
		initializeAllNeighborsAndDistances(data,clusters,results,rowKeys);
		
		// process data.
		mergeCommonNeighbors(rows, clusters, results, numNeighbors, commonNeighbors);
		
		return results;
	}
	
	
	protected void initializeAllNeighborsAndDistances(E data,List<ClusterJarvis> clusters, ClusteringResult results, List<String> rowKeys) {
		
		final int rows = rowKeys.size();
		
		/*
		 * Distance { "From-To", distance}. Therefore, it is not
		 * necessary to calculate the distance twice when calculating
		 * distance between To-From
		 */
		Map<String,Number> distances = new HashMap<String, Number>();
		Number toFrom=0;

		for (int fromIdx=0; fromIdx<rows;fromIdx++){
		
			ClusterJarvis cluster = new ClusterJarvis(fromIdx);
			cluster.addRow2Cluster(fromIdx);
			
			clusters.add(cluster);
			
			// Traverse all rows to add the distance between
			// current row (fromIdx) and the rest of the list
			for (int toIdx=0; toIdx<rows;toIdx++){
				
				if (fromIdx!=toIdx){ // to a different point than itself. This distance is 0  
					
					// Check whether the distance was already calculate backwards
					toFrom = distances.get(toIdx+"-"+fromIdx);
					
					// if not then calculate the distance
					if (toFrom==null){
						//toFrom = Utility.distance(data.get(rowKeys.get(fromIdx)), data.get(rowKeys.get(toIdx)));
						toFrom = getDistance(data, rowKeys, fromIdx, toIdx);
						distances.put(fromIdx+"-"+toIdx, toFrom);
					}
					
					// add the distance to the cluster neighbors
					cluster.addCloseNeighbor(toFrom, toIdx);
				}	
			}
			
			// initialized the results collection.				
			results.addClusterToLabel(0);
		}
	}

	
	/**
	 * TODO description 
	 * @param rows
	 * @param clusters
	 * @param results
	 * @param numNeighbors
	 * @param commonNeighbors
	 */
	private void mergeCommonNeighbors(final int rows,
			List<ClusterJarvis> clusters, ClusteringResult results,
			final int numNeighbors, final int commonNeighbors) {
		
		for (int fromIdx=0; fromIdx<rows;fromIdx++){
			ClusterJarvis currentCluster =  clusters.get(fromIdx);
			
			if (currentCluster.getStatus().equalsIgnoreCase(ClusterJarvis.STATUS_NOT_PROCESSED)){

				currentCluster.sortClosestNeighbors();
				// Just leave Z (numNeighbors) elements in the neighbor collection
				currentCluster.setClosestNeighbors(ClusterJarvis.truncateCollection(numNeighbors, currentCluster.getClosestNeighbors()));
				
				for (int toIdx=0; toIdx<rows;toIdx++){
					
					if (fromIdx!=toIdx){ 
						ClusterJarvis nextCluster =  clusters.get(toIdx);
						
						if (nextCluster.getStatus().equalsIgnoreCase(ClusterJarvis.STATUS_NOT_PROCESSED)){
							
							nextCluster.sortClosestNeighbors();
							// Just leave Z (numNeighbors) elements in the neighbor collection
							nextCluster.setClosestNeighbors(ClusterJarvis.truncateCollection(numNeighbors, nextCluster.getClosestNeighbors()));
							
							// Get the rows from the current cluster
							Collection<Integer> currentNeighborRows = currentCluster.getClosestNeighborsRowsId();
							
							// Get the rows from the next cluster
							Collection<Integer> nextNeighborsRows = nextCluster.getClosestNeighborsRowsId();
							
							// Check the contain each other Ids
							if (currentNeighborRows.contains(nextCluster.getId()) && nextNeighborsRows.contains(currentCluster.getId())){
							
								// Then just keep the common rows between both clusters
								currentNeighborRows.retainAll(nextNeighborsRows); 
								
								// Check if they have X (commonNeighbors) in common
								if ( currentNeighborRows.size() >=  commonNeighbors) { 
									// They belong to same cluster
									// Merge clusters (add all points from second cluster to first one)
									currentCluster.addRows2Cluster(nextCluster.getClusterRows());
									
									// and mark the second cluster as PROCESSED
									nextCluster.setStatus(ClusterJarvis.STATUS_MOVED);
								}
							}
						}
					}
				}
				
				// Mark the rows with their Cluster ID in Results collection.
				// Doing this here avoids an extra full (n) iteration later
				for (Integer currentRow: currentCluster.getClusterRows()){
					results.setClusterToLabelByIndex(currentRow ,currentCluster.getId()+1 );
				}
				
				// Mark cluster as processed.
				currentCluster.setStatus(ClusterJarvis.STATUS_PROCESSED);
			}
		}
	}

}