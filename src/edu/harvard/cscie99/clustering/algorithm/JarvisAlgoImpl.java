package edu.harvard.cscie99.clustering.algorithm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;

public class JarvisAlgoImpl implements IClusterAlgo {

	public final static int PRECISION = 1;
	
	@Override
	public ClusteringResult cluster(double[][] data,
			Map<String, String> clusterParams) {
		// TODO Auto-generated method stub
		
		try {
			final int numNeighbors = Integer.valueOf(clusterParams.get("numNeighbors")); // TODO check for exception
			final int commonNeighbors = Integer.valueOf(clusterParams.get("commonNeighbors")); // TODO check for exception
			final String distanceMetric = clusterParams.get("distanceMetric"); //{"Euclidian"}
			final int rows = data.length;
			
			
			ClusteringResult results = new ClusteringResult(rows);
			
			// row index - cluster
			List<ClusterJarvis> clusters = new ArrayList<ClusterJarvis>();
			
			/*
			 * Distance { "From-To", distance}. Therefore, it is not
			 * necessary to calculate the distance twice when calculating
			 * distance between To-From
			 */
			Map<String,Double> distances = new HashMap<String, Double>();
			Double toFrom=0D;

			for (int fromIdx=0; fromIdx<rows;fromIdx++){
			
				ClusterJarvis cluster = new ClusterJarvis(fromIdx);
				//cluster.addNeighbor(0D, fromIdx); // include itself into the neighbor group
				cluster.addRow2Cluster(fromIdx);
				
				clusters.add(cluster);
				
				// Traverse all rows to add the distance between
				// current row (fromIdx) and the rest of the list
				for (int toIdx=0; toIdx<rows;toIdx++){
					
					if (fromIdx!=toIdx){ // to a different point than itself. This distance is 0  
						
						// Check whether the distance was already calculate backwards
						//toFrom = distances.get(toIdx+"-"+fromIdx);
						toFrom = null;
						
						// if not then calculate the distance
						if (toFrom==null){
							toFrom = JarvisAlgoImpl.distance(data[fromIdx], data[toIdx]);
							distances.put(fromIdx+"-"+toIdx, toFrom);
						}
						
						// TODO change the BigDecimal object creation
						// add the distance to the cluster neighbors
						cluster.addCloseNeighbor(new BigDecimal(toFrom).setScale(PRECISION,BigDecimal.ROUND_HALF_UP).doubleValue(), toIdx);
					}	
				}
				
				// initialized the results collection.				
				results.addClusterToLabel(0);
			}
			
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

			
			
//			for (int fromIdx=0; fromIdx<rows;fromIdx++){
//				ClusterJarvis currentCluster =  clusters.get(fromIdx);
//
//				if (currentCluster.getStatus().equalsIgnoreCase(ClusterJarvis.STATUS_PROCESSED)){
//				
//					for (Integer currentRow: currentCluster.getClusterRows()){
//						results.setClusterToLabelByIndex(currentRow ,currentCluster.getId()+1 );
//					}
//				}
//			}	
			
			return results;
		
		} catch (Exception e) {
			// TODO log the error
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * TODO move to UTIL static -- throws exception
	 * 
	 * @param point1
	 * @param point2
	 * @return double 
	 */
	private static double distance(double[] point1, double[] point2)
			throws Exception {
		
		// Make sure both have the same length 
		if (point1.length == point2.length) {
			Double sum = 0D;
			for (int i = 0; i < point1.length; i++) {
				sum = sum + (point2[i] - point1[i]) * (point2[i] - point1[i]);
			}
			return Math.sqrt(sum);
		} else {
			throw new Exception("Error: array lengths are not equal");
		}
	}
	

	@Override
	public ClusteringResult cluster(Map<String, List<Integer>> data,
			Map<String, Object> clusterParams) {
		// TODO Auto-generated method stub
		return null;
	}

}