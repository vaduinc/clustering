package edu.harvard.cscie99.clustering.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.InputParamEnum;

/**
 * Class to support the Jarvis-Patrick clustering algorithm.
 * It supports both Matrix and Fingerprint input data.
 * 
 * Following is the algorithm definition.
 * 
 * Taken from:
 * http://www.improvedoutcomes.com/docs/WebSiteDocs/Clustering/Jarvis-Patrick_Clustering_Overview.htm
 * 
 * The first parameter, Neighbors to Examine, specifies how many of each item's neighbors to consider
 * when counting the number of mutual neighbors shared with another item. This value must be at least 2. 
 * Lower values cause the algorithm to finish faster, but the final set of clusters will have many 
 * small clusters. Higher values cause the algorithm to take longer to finish, but may result in fewer
 * clusters and clusters that form longer chains.
 *
 * The second parameter, Neighbors in Common, specifies the minimum number of mutual nearest neighbors
 * two items must have for them to be in the same cluster. This value must be at least 1, and must not 
 * exceed the value of the Neighbors to Examine parameter. 
 * Lower values result in clusters that are compact. Higher values result in clusters that are more
 * dispersed.
 *
 * Basic Procedure
 *	
 * For each object, find its J-nearest neighbors where "J" corresponds to the Neighbors to Examine 
 * parameter on the Partitional Clustering dialog. Two items cluster together if they are in each 
 * other's list of J-nearest neighbors and K of their J-nearest neighbors are in common, where the 
 * K value corresponds to the Neighbors in Common parameter on the Partitional Clustering dialog.
 *
 *
 * @param <E> is either <double[][]> or Map<String, BitSet>
 */

public abstract class JarvisAlgorithm<E extends Object> implements IClusterAlgo<E> {

	public final static int PRECISION = 1; // number of decimals

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
	
	
	/**
	 * Creates a cluster for each row and includes all the distances between
	 * this row and the rest of the rows
	 * 
	 * @param data
	 * @param clusters	
	 * @param results	to write the output
	 * @param rowKeys	row labels
	 */
	private void initializeAllNeighborsAndDistances(E data,List<ClusterJarvis> clusters, ClusteringResult results, List<String> rowKeys) {
		
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
						toFrom = getDistance(data, rowKeys, fromIdx, toIdx);
						distances.put(fromIdx+"-"+toIdx, toFrom);
					}
					
					// add the distance to the cluster neighbors
					cluster.addCloseNeighbor(toFrom, toIdx);
				}	
			}
			
			// initialized the results collection.				
			results.addClusterToLabel(0);
			
			// debugging System.out.println(cluster.toString());
		}
	}

	
	/**
	 * Compares all the clusters and merge them when:
	 * 
	 * Two items cluster together if they are in each other's list and have 
	 * commonNeighbors in common.
	 * 
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