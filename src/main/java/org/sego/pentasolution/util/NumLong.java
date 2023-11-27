package org.sego.pentasolution.util;

import java.math.BigInteger;

public class NumLong implements Num {

	private long value;

	public NumLong() {
		super();
		this.value = 0l;
	}

	public NumLong(long value) {
		super();
		this.value = value;
	}

	@Override
	public Num divide(int d) {
		return new NumLong(value / d);
	}

	@Override
	public Num divide(Num d) {
		return new NumLong(value / ((NumLong) d).value);
	}

	@Override
	public int mod(int v) {
		return (int) (value % v);
	}

	public Num multiply(int v) {
		return new NumLong(value * v);
	}

	@Override
	public Num add(int d) {
		return new NumLong(value + d);
	}

	@Override
	public Num addMutable(Num d) {
		value += ((NumLong) d).value;
		return this;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public int compareTo(Num o) {
		long x = this.value;
		long y = ((NumLong) o).value;
		return (x < y) ? -1 : ((x == y) ? 0 : 1);
	}

	@Override
	public Num setMutable(BigInteger v) {
		if (BigInteger.valueOf(Long.MAX_VALUE).compareTo(v) <= 0) {
			throw new IllegalArgumentException(v + " to big value for long type");
		}
		value = v.longValue();
		return this;
	}

	@Override
	public Num setMutable(int v) {
		value = v;
		return this;
	}

	@Override
	public BigInteger getBigInteger() {
		return BigInteger.valueOf(value);
	}

}
