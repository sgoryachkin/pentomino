package org.sego.pentasolution.util;

import java.math.BigInteger;

public class DoubleLong {
	
	public DoubleLong(long l0, long l1) {
		super();
		this.l0 = l0;
		this.l1 = l1;
	}
	
	public DoubleLong(BigInteger bigInteger) {
		super();
		this.l0 = bigInteger.divide(BigInteger.valueOf(Long.MAX_VALUE)).longValue();
		this.l1 = bigInteger.mod(BigInteger.valueOf(Long.MAX_VALUE)).longValue();
	}
	
	long l0;
	long l1;

	public DoubleLong devide(long d) {
		long a = (l0 / d);
		long a0 = ((l0 % d) / d);
		long a1 = (l0 % d) % d;
		long b = (l1 / d);
		DoubleLong result = new DoubleLong(a + a0, b + a1).addMutable(b);
		return result;
	}
	
	public DoubleLong addMutable(long d) {
		if (l1 + d < l1) {
			l0++;
			l1 = (l1 - Long.MAX_VALUE) + d;
		} else {
			l1+=d;
		}
		return this;
	}
	
	public BigInteger toBigInteger() {
		return BigInteger.valueOf(l0).multiply(BigInteger.valueOf(Long.MAX_VALUE)).add(BigInteger.valueOf(l1));
	}

	@Override
	public String toString() {
		return "DoubleLong [l0=" + l0 + ", l1=" + l1 + "]";
	}
	
}
