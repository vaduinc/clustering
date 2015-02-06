package edu.harvard.cscie99.clustering.algorithm;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.harvard.cscie99.clustering.result.ClusteringResult;
import edu.harvard.cscie99.clustering.util.InputParamEnum;
import edu.harvard.cscie99.clustering.util.Utility;

public class JarvisAlgoImpl implements IClusterAlgo {

	public final static int PRECISION = 1;
	
	@Override
	public ClusteringResult cluster(List<String> rowLabels,double[][] data,
			Map<String, Object> clusterParams) {
		// TODO Auto-generated method stub
		
		try {
			final int numNeighbors = Integer.valueOf(clusterParams.get(InputParamEnum.IN_NUM_NEIGH.value()).toString()); // TODO check for exception
			final int commonNeighbors = Integer.valueOf(clusterParams.get(InputParamEnum.IN_COMM_NEIGH.value()).toString()); // TODO check for exception
			final String distanceMetric = (String)clusterParams.get(InputParamEnum.IN_DIST_METRIC); //{"Euclidian"}
			final int rows = data.length;
			
			ClusteringResult results = new ClusteringResult(rows,rowLabels);
			List<ClusterJarvis> clusters = new ArrayList<ClusterJarvis>();
			
			// initialize
			initializeAllNeighborsAndDistances(data,clusters,results);
			
			// process data.
			mergeCommonNeighbors(rows, clusters, results, numNeighbors, commonNeighbors);
			
			return results;
		
		} catch (Exception e) {
			// TODO log the error
			e.printStackTrace();
			return null;
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
	
	
	
	/**
	 * TODO description
	 * 
	 * @param data
	 * @param clusters
	 * @param results
	 * @throws Exception
	 */
	private void initializeAllNeighborsAndDistances(double[][] data,List<ClusterJarvis> clusters, ClusteringResult results) throws Exception{
		
		final int rows = data.length;
		
		/*
		 * Distance { "From-To", distance}. Therefore, it is not
		 * necessary to calculate the distance twice when calculating
		 * distance between To-From
		 */
		Map<String,Double> distances = new HashMap<String, Double>();
		Double toFrom=null;

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
						toFrom = Utility.distance(data[fromIdx], data[toIdx]);
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
		
		
	}
	

	@Override
	public ClusteringResult cluster(Map<String, BitSet> data,
			Map<String, Object> clusterParams) {
		
		final int numNeighbors = Integer.valueOf(clusterParams.get(InputParamEnum.IN_NUM_NEIGH.value()).toString()); // TODO check for exception
		final int commonNeighbors = Integer.valueOf(clusterParams.get(InputParamEnum.IN_COMM_NEIGH.value()).toString()); // TODO check for exception
		final String distanceMetric = (String)clusterParams.get(InputParamEnum.IN_DIST_METRIC); //{"Euclidian"}
		final List<String> rowKeys = new ArrayList<String>(data.keySet());
		final int rows = rowKeys.size();
		
		ClusteringResult results = new ClusteringResult(rows,rowKeys);
		List<ClusterJarvis> clusters = new ArrayList<ClusterJarvis>();
		
		return null;
	}
	
	
	private void initializeAllNeighborsAndDistances(Map<String, BitSet> data,List<ClusterJarvis> clusters, ClusteringResult results) throws Exception{
		
		final int rows = data.size();
		final List<String> rowKeys = new ArrayList<String>(data.keySet());
		
		/*
		 * Distance { "From-To", distance}. Therefore, it is not
		 * necessary to calculate the distance twice when calculating
		 * distance between To-From
		 */
		Map<String,Integer> distances = new HashMap<String, Integer>();
		Integer toFrom=0;

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
						toFrom = Utility.distance(data.get(rowKeys.get(fromIdx)), data.get(rowKeys.get(toIdx)));
						distances.put(fromIdx+"-"+toIdx, toFrom);
					}
					
					// TODO change the BigDecimal object creation
					// add the distance to the cluster neighbors
					cluster.addCloseNeighbor(toFrom, toIdx);
				}	
			}
			
			// initialized the results collection.				
			results.addClusterToLabel(0);
		}
		
		
	}
	

}