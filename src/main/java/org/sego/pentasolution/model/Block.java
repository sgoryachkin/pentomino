package org.sego.pentasolution.model;

public class Block {

	private static int SHIFT = 4;
	public static int MAX = (1 << SHIFT) - 1; // 15

	private int v;

	public Block(int x, int y) {
		super();
		if (x > MAX || y > MAX) {
			throw new IllegalArgumentException();
		}
		this.v = x << SHIFT;
		this.v += y;
	}
	
	public Block(int row) {
		super();
		this.v = row;
	}

	int x() {
		return v >> SHIFT;
	}

	int y() {
		return v & MAX;
	}
	
	int row() {
		return v;
	}

	@Override
	public int hashCode() {
		return v;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Block other = (Block) obj;
		if (v != other.v)
			return false;
		return true;
	}

}
