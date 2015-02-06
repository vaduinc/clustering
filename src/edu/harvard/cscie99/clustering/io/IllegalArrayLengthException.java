/*
 * Method to handle a single dimensional array difference in length
 */

package edu.harvard.cscie99.clustering.io;

/**
 *
 * @author henstock
 */
public class IllegalArrayLengthException extends Exception {
    private int passedLength;
    private int expectedLength;
    public IllegalArrayLengthException(String msg, int passedLength, int expectedLength) {
        super(msg);
        this.passedLength = passedLength;
        this.expectedLength = expectedLength;
    }
    
    @Override
    public String getMessage() {
        return super.getMessage() + "Passed length: " + passedLength + " expected length: " + expectedLength;
    }

}
