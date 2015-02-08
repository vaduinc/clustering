package edu.harvard.cscie99.clustering.io;

import java.io.IOException;

/**
 * Common interface for the readers (Matrix and Fingerprint)
 *
 */
public interface IReader {

	public void loadData(String filename) throws IOException;
}
