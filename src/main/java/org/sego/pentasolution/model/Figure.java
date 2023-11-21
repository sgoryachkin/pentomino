package org.sego.pentasolution.model;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Figure {
	
	public static final Figure X = Figure.builder().addBlock(1, 0).addBlock(0, 1).addBlock(1, 1).addBlock(2, 1)
			.addBlock(1, 2).build();

	public static final Figure L = Figure.builder().addBlock(0, 0).addBlock(1, 0).addBlock(2, 0).addBlock(3, 0)
			.addBlock(3, 1).build();

	public static final Figure N = Figure.builder().addBlock(1, 0).addBlock(2, 0).addBlock(3, 0).addBlock(0, 1)
			.addBlock(1, 1).build();

	public static final Figure T = Figure.builder().addBlock(0, 0).addBlock(1, 0).addBlock(2, 0).addBlock(1, 1)
			.addBlock(1, 2).build();

	public static final Figure U = Figure.builder().addBlock(0, 0).addBlock(1, 0).addBlock(2, 0).addBlock(0, 1)
			.addBlock(2, 1).build();
	
	public static final Figure F = Figure.builder().addBlock(1, 0).addBlock(2, 0).addBlock(0, 1).addBlock(1, 1)
			.addBlock(1, 2).build();
	
	public static final Figure Z = Figure.builder().addBlock(0, 0).addBlock(1, 0).addBlock(1, 1).addBlock(1, 2)
			.addBlock(2, 2).build();
	
	public static final Figure V = Figure.builder().addBlock(0, 0).addBlock(0, 1).addBlock(0, 2).addBlock(1, 2)
			.addBlock(2, 2).build();
	
	public static final Figure Y = Figure.builder().addBlock(0, 0).addBlock(1, 0).addBlock(2, 0).addBlock(3, 0)
			.addBlock(2, 1).build();
	
	public static final Figure P = Figure.builder().addBlock(0, 0).addBlock(1, 0).addBlock(0, 1).addBlock(1, 1)
			.addBlock(0, 2).build();
	
	public static final Figure W = Figure.builder().addBlock(0, 0).addBlock(1, 0).addBlock(1, 1).addBlock(2, 1)
			.addBlock(2, 2).build();
	
	public static final Figure I = Figure.builder().addBlock(0, 0).addBlock(1, 0).addBlock(2, 0).addBlock(3, 0)
			.addBlock(4, 0).build();
	
	
	private static final char[] DROW_OPTIONS = {'0','Z','E','N','G','C','O','X','Y','D','A','B','K'};
	
	private int[] blocks;

	private Set<Block> getBlocks() {
		return Arrays.stream(blocks).mapToObj(v -> new Block(v)).collect(Collectors.toSet());
	}
	
	public int[] getRowBlocks() {
		return blocks;
	}

	public Figure(Set<Block> blocks) {
		super();
		this.blocks = blocks.stream().mapToInt(v-> v.row()).toArray();
		Arrays.sort(this.blocks);
	}

	public static Builder builder() {
		return new Builder();
	}

	public Set<Figure> getAllRotations() {
		Figure.Builder swapBuilder = Figure.builder();
		Figure.Builder origYReversBuilder = Figure.builder();
		Figure.Builder origXReverBuilder = Figure.builder();
		Figure.Builder origXYReversBuilder = Figure.builder();
		Figure.Builder swapXReversBuilder = Figure.builder();
		Figure.Builder swapYReversBuilder = Figure.builder();
		Figure.Builder swapXYReversBuilder = Figure.builder();

		Set<Block> blocks = getBlocks();
		
		for (Block b : blocks) {
			swapBuilder.addBlock(new Block(b.y(), b.x()));
		}
		Figure swap = swapBuilder.build();

		var xmax = blocks.stream().map(b -> b.x()).max(Comparator.naturalOrder()).get();
		var ymax = blocks.stream().map(b -> b.y()).max(Comparator.naturalOrder()).get();
		Set<Block> swapblocks = swap.getBlocks();
		var sxmax = swapblocks.stream().map(b -> b.x()).max(Comparator.naturalOrder()).get();
		var symax = swapblocks.stream().map(b -> b.y()).max(Comparator.naturalOrder()).get();

		for (Block b : blocks) {
			origYReversBuilder.addBlock(new Block(b.x(), ymax - b.y()));
			origXReverBuilder.addBlock(new Block(xmax - b.x(), b.y()));
			origXYReversBuilder.addBlock(new Block(xmax - b.x(), ymax - b.y()));
		}

		for (Block b : swapblocks) {
			swapXReversBuilder.addBlock(new Block(b.x(), symax - b.y()));
			swapYReversBuilder.addBlock(new Block(sxmax - b.x(), b.y()));
			swapXYReversBuilder.addBlock(new Block(sxmax - b.x(), symax - b.y()));
		}

		return Collections.unmodifiableSet(Stream.of(this, swap, origYReversBuilder.build(), origXReverBuilder.build(), origXYReversBuilder.build(), swapXReversBuilder.build(),
				swapYReversBuilder.build(), swapXYReversBuilder.build()).collect(Collectors.toSet()));
	}
	
	public Set<Figure> getAllPositions(int xsize, int ysize){
		Set<Figure> result = new HashSet<Figure>();
		for (int i = 0; i < xsize; i++) {
			for (int j = 0; j < ysize; j++) {
				Set<Block> shiftBlocks = new HashSet<Block>();
				for (Block b : getBlocks()) {
					Block shiftBlock = new Block(b.x()+i, b.y()+j);
					shiftBlocks.add(shiftBlock);
				}
				// validate
				Figure f = new Figure(shiftBlocks);
				Set<Block> fblocks = f.getBlocks();
				var fxmax = fblocks.stream().map(b -> b.x()).max(Comparator.naturalOrder()).get();
				var fymax = fblocks.stream().map(b -> b.y()).max(Comparator.naturalOrder()).get();
				if (fxmax<xsize && fymax < ysize) {
					result.add(f);
				}
			}
		}
		return result;
	}
	
	public Set<Figure> getAllRotationsWithAllPositions(int xsize, int ysize){
		return this.getAllRotations().stream().flatMap(f -> f.getAllPositions(xsize, ysize).stream()).collect(Collectors.toSet());
	}

	public static class Builder {
		private Set<Block> blocks = new HashSet<>();

		public Builder addBlock(int xBlockAdresses, int yBlockAdresses) {
			this.blocks.add(new Block(xBlockAdresses, yBlockAdresses));
			return this;
		}
		
		public Builder addBlock(Block block) {
			this.blocks.add(block);
			return this;
		}
		
		public Figure build() {
			Figure f = new Figure(blocks);
			return f;
		}
	}

	public void drow(char drowChar) {
		drowBlocks(getBlocks(), drowChar);
	}
	
	public void drow() {
		char drowOption = DROW_OPTIONS[new Random().nextInt(DROW_OPTIONS.length)];
		drow(drowOption);
	}
	
	public static void drowBlocks(Collection<Block> blocks, char drowChar) {
		var xmax = blocks.stream().map(b -> b.x()).max(Comparator.naturalOrder()).get();
		var ymax = blocks.stream().map(b -> b.y()).max(Comparator.naturalOrder()).get();
		for (int i = 0; i < ymax + 1; i++) {
			for (int j = 0; j < xmax + 1; j++) {
				System.out.print(blocks.contains(new Block(j, i)) ? drowChar : '-');
			}
			System.out.println();
		}
	}
	
	public static void drowFigures(Collection<Figure> figures) {
		Map<Block, Figure> blocks = figures.stream()
				.flatMap(
						f -> f.getBlocks().stream().map(v -> new AbstractMap.SimpleImmutableEntry<Block, Figure>(v, f)))
				.collect(Collectors.toMap(v -> v.getKey(), v -> v.getValue()));
		var xmax = blocks.keySet().stream().map(b -> b.x()).max(Comparator.naturalOrder()).get();
		var ymax = blocks.keySet().stream().map(b -> b.y()).max(Comparator.naturalOrder()).get();
		for (int i = 0; i < ymax + 1; i++) {
			for (int j = 0; j < xmax + 1; j++) {
				Block drowBlock = new Block(j, i);
				Figure drowFigure = blocks.get(drowBlock);
				
				char drowOption = drowFigure!=null ? DROW_OPTIONS[new Random(drowFigure.hashCode()).nextInt(DROW_OPTIONS.length)] : '-';
				
				System.out.print(blocks.keySet().contains(new Block(j, i)) ? drowOption : '-');
			}
			System.out.println();
		}
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(blocks);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Figure other = (Figure) obj;
		return Arrays.equals(blocks, other.blocks);
	}

	@Override
	public String toString() {
		return "Figure [blocks=" + Arrays.toString(blocks) + "]";
	}
	
}
