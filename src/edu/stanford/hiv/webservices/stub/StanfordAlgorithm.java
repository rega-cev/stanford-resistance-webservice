/**
 * StanfordAlgorithm.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package edu.stanford.hiv.webservices.stub;

public interface StanfordAlgorithm extends java.rmi.Remote {
	public static final int SIMPLE_REPORT = 0;
	public static final int DETAIL_REPORT = 1;
	
    public java.lang.String processSequence(java.lang.String in0, java.lang.String in1) throws java.rmi.RemoteException;
    public java.lang.String processSequences(java.lang.String in0, java.lang.Integer in1, java.lang.String[] in2) throws java.rmi.RemoteException;
    public java.lang.String processSequencesInFasta_REGA(java.lang.String in0, java.lang.Integer in1, java.lang.String in2) throws java.rmi.RemoteException;
    public java.lang.String processMutationLists(java.lang.String in0, java.lang.Integer in1, java.lang.String[][][] in2) throws java.rmi.RemoteException;
}
