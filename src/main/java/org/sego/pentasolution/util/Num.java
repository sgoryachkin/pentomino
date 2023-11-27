package org.sego.pentasolution.util;

import java.math.BigInteger;

public interface Num extends Comparable<Num>{

	public Num divide(int d);
	
	public Num divide(Num d);
	
	public int mod(int v);
	
	public Num multiply(int v);
	
	public Num add(int d);
	
	public Num addMutable(Num d);
	
	public Num setMutable(BigInteger v);
	
	public Num setMutable(int v);
	
	BigInteger getBigInteger();
	
}
