package org.sego.pentasolution.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.sego.pentasolution.model.Block;
import org.sego.pentasolution.model.Figure;

public class SolutionService {

	private static final Logger LOG = Logger.getLogger(SolutionService.class.toString());

	public static List<List<Figure>> clac(int x, int y, List<Figure> figures) {

		long calcStartTime = System.currentTimeMillis();

		Figure[][] figuresAll = List.copyOf(figures.stream()
				.map(f -> List.copyOf(f.getAllRotationsWithAllPositions(x, y).stream()
						.sorted(Collections.reverseOrder(Comparator.comparingInt(v -> v.getRowBlocks()[0]))).toList()).toArray(new Figure[0]))
				.sorted(Collections.reverseOrder(Comparator.comparingInt(v -> v.length))).toList()).toArray(new Figure[0][0]);
		LOG.info("Board: x=" + x + ", y=" + y);
		LOG.info("All Figures:");
		LOG.info(() -> {
			StringBuilder sb = new StringBuilder();
			for (Figure[] f : figuresAll) {
				sb.append(f.length).append(", ");
			}
			return sb.toString();
		});

		List<List<Figure>> solutions = new ArrayList<>();

		final BigInteger mc = Arrays.stream(figuresAll).map(l -> BigInteger.valueOf(l.length)).reduce(BigInteger.ONE,
				(v1, v2) -> v1.multiply(v2));
		System.out.println(mc);
		// https://ru.stackoverflow.com/questions/235030/%d0%9a%d0%b0%d0%ba-%d1%81%d0%be%d1%81%d1%82%d0%b0%d0%b2%d0%b8%d1%82%d1%8c-%d0%b2%d0%be%d0%b7%d0%bc%d0%be%d0%b6%d0%bd%d1%8b%d0%b5-%d0%ba%d0%be%d0%bc%d0%b1%d0%b8%d0%bd%d0%b0%d1%86%d0%b8%d0%b8-%d0%bc%d0%b5%d0%b6%d0%b4%d1%83-%d1%8d%d0%bb%d0%b5%d0%bc%d0%b5%d0%bd%d1%82%d0%b0%d0%bc%d0%b8-%d0%bc%d0%bd%d0%be%d0%b6%d0%b5%d1%81%d1%82%d0%b2
		BitSet goodSubCombinationBlocksBitSet = new BitSet();
		int[] goodSubCombinationCode = new int[figuresAll.length]; // Current idx combination storage
		//BigInteger nc; // Сдвиг разрядов
		BigInteger ncs[] = new BigInteger[figuresAll.length]; // Shifts for all (cached)

		long prepareTime = System.currentTimeMillis();

		for (BigInteger j = BigInteger.ZERO, jump = BigInteger.ONE; j.compareTo(mc) < 0; j = j.add(jump)) { // Номер
																											// текущей																									
			                                                                                                // комбинации
			boolean isShift = false;
			for (int variantIndex = figuresAll.length - 1; variantIndex >= 0; variantIndex--) { // Бежим по множествам
				
				Figure[] figuresAllvariantIndex = figuresAll[variantIndex];
				BigInteger figuresAllvariantIndexlength = BigInteger.valueOf(figuresAllvariantIndex.length);
				
				
				// Cache NCs. //TODO: Move to prepare cache calculation for all j undepended data
				if (ncs[variantIndex] == null) {
					ncs[variantIndex] = variantIndex == figuresAll.length - 1 ? mc.divide(figuresAllvariantIndexlength) // Current
							: ncs[variantIndex + 1].divide(figuresAllvariantIndexlength); // Current

				}
				
				int idx = j.divide(ncs[variantIndex]).mod(figuresAllvariantIndexlength).intValue(); // Current shift
				
				int[] idxBlocks = figuresAllvariantIndex[idx].getRowBlocks();

//				System.out.print("nc=" + nc + " shifted=" + shifted + " > " + idx + " ");

				if (!absentAll(goodSubCombinationBlocksBitSet, idxBlocks)) {
					jump = ncs[variantIndex];
					isShift = (variantIndex == figuresAll.length-2);
					break;
				}
				jump = BigInteger.ONE;

				addAll(goodSubCombinationBlocksBitSet, idxBlocks);

				goodSubCombinationCode[figuresAll.length - (variantIndex + 1)] = idx;

				if (variantIndex == 0) {
					List<Figure> listSolution = new ArrayList<Figure>(figuresAll.length);
					for (int variantIndexSol = figuresAll.length - 1; variantIndexSol >= 0; variantIndexSol--) {
						listSolution.add(figuresAll[variantIndexSol]
								[goodSubCombinationCode[figuresAll.length - (variantIndexSol + 1)]]);
					}
					solutions.add(Collections.unmodifiableList(listSolution));
					LOG.info("Solution " + solutions.size() + " was found on combination number: " + j);
				}

			}
			goodSubCombinationBlocksBitSet.clear();
//			System.out.println();
			if (isShift) {
				System.out.println(new BigDecimal(j).divide(new BigDecimal(mc), 2, RoundingMode.FLOOR)
						.multiply(BigDecimal.valueOf(100).round(new MathContext(1, RoundingMode.HALF_EVEN))) + "%");
			}

		}

		LOG.info("Full time: " + (double) (System.currentTimeMillis() - calcStartTime) / 1000);
		LOG.info(" - Prepare time: " + (double) (prepareTime - calcStartTime) / 1000);
		LOG.info(" - Search time: " + (double) (System.currentTimeMillis() - prepareTime) / 1000);

		return solutions;

	}

	private static boolean absentAll(BitSet bitSet, int[] indexes) {
		for (int i = 0; i < indexes.length; i++) {
			if (bitSet.get(indexes[i])) {
				return false;
			}
		}
		return true;
	}

	private static void addAll(BitSet bitSet, int[] indexes) {
		for (int i = 0; i < indexes.length; i++) {
			bitSet.set(indexes[i]);
		}
	}

}
