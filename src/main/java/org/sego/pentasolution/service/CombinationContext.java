package org.sego.pentasolution.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.sego.pentasolution.model.Figure;

public class CombinationContext {
	
	
	public static class CommonContext {
		Figure[][] figuresAll;
		BigInteger mc;
		/**
		 * Shifts of mc
		 */
		BigInteger ncs[];
	}
	
	public CombinationContext(CommonContext ccc) {
		super();
		this.ccc = ccc;
	}

	CommonContext ccc;
	
	List<List<Figure>> solutions = new ArrayList<>();
	BigInteger jump;
	BigInteger startj;
	BigInteger endj;
	
	/**
	 * Bit set to search solutios 
	 */
	BitSet goodSubCombinationBlocksBitSet;
	
	/**
	 * Current idx combination in figuresAll
	 */
	int[] goodSubCombinationCode;
	

	
}
