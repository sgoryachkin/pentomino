package org.sego.pentasolution.service;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.sego.pentasolution.model.Figure;
import org.sego.pentasolution.util.Num;

public class CombinationContext {
	
	
	public static class CommonContext {
		Figure[][] figuresAll;
		Num mc;
		/**
		 * Shifts of mc
		 */
		Num ncs[];
	}
	
	public CombinationContext(CommonContext ccc) {
		super();
		this.ccc = ccc;
	}

	CommonContext ccc;
	
	List<List<Figure>> solutions = new ArrayList<>();
	
	Num jump;
	Num startj;
	Num endj;
	
	/**
	 * Bit set to search solutios 
	 */
	BitSet goodSubCombinationBlocksBitSet;
	
	/**
	 * Current idx combination in figuresAll
	 */
	int[] goodSubCombinationCode;
	

	
}
