package org.sego.pentasolution.util;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class DoubleLongTest {

	@Test
	public void test() {
		BigInteger bi = new BigInteger("41329531870494326784000000");
		DoubleLong dl = new DoubleLong(bi);
		assertEquals(bi, dl.toBigInteger());
		
		dl.addMutable(2);
		assertEquals(bi.add(BigInteger.valueOf(2)), dl.toBigInteger());
		
		dl.addMutable(Long.MAX_VALUE);
		assertEquals(bi.add(BigInteger.valueOf(2).add(BigInteger.valueOf(Long.MAX_VALUE))), dl.toBigInteger());
		
		//DoubleLong dld2 = dl.devide(2);
		//assertEquals(bi.divide(BigInteger.valueOf(2)), dl.toBigInteger());
	}
	
	@Test
	public void testDiv() {
		BigInteger bi = new BigInteger("41329531870494326784000001");
		DoubleLong dl = new DoubleLong(bi);
		assertEquals(bi, dl.toBigInteger());
		
		DoubleLong dld2 = dl.devide(2);
		assertEquals(bi.divide(BigInteger.valueOf(2)), dld2.toBigInteger());
		
		dld2 = dl.devide(3);
		assertEquals(bi.divide(BigInteger.valueOf(3)), dld2.toBigInteger());
		
		dld2 = dl.devide(Long.MAX_VALUE);
		assertEquals(bi.divide(BigInteger.valueOf(Long.MAX_VALUE)), dld2.toBigInteger());
		
		dld2 = dl.devide(Long.MAX_VALUE - 1);
		assertEquals(bi.divide(BigInteger.valueOf(Long.MAX_VALUE - 1)), dld2.toBigInteger());
	}

}
