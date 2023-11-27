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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.sego.pentasolution.model.Figure;
import org.sego.pentasolution.service.CombinationContext.CommonContext;
import org.sego.pentasolution.util.Num;
import org.sego.pentasolution.util.NumLong;

public class SolutionService {

	private static final Logger LOG = Logger.getLogger(SolutionService.class.toString());

	private static final int threadCount = 16;

	public static List<List<Figure>> clac(int x, int y, List<Figure> figures)
			throws InterruptedException, ExecutionException {

		long calcStartTime = System.currentTimeMillis();

		CommonContext ccc = new CommonContext();

		ccc.figuresAll = List
				.copyOf(figures
						.stream().map(
								f -> List
										.copyOf(f.getAllRotationsWithAllPositions(x, y).stream()
												.sorted(Collections.reverseOrder(
														Comparator.comparingInt(v -> v.getRowBlocks()[0])))
												.toList())
										.toArray(new Figure[0]))
						.sorted(Collections.reverseOrder(Comparator.comparingInt(v -> v.length))).toList())
				.toArray(new Figure[0][0]);
		LOG.info("Board: x=" + x + ", y=" + y);
		LOG.info("All Figures:");
		LOG.info(() -> {
			StringBuilder sb = new StringBuilder();
			for (Figure[] f : ccc.figuresAll) {
				sb.append(f.length).append(", ");
			}
			return sb.toString();
		});

		ccc.mc = numFactory().setMutable(Arrays.stream(ccc.figuresAll).map(l -> BigInteger.valueOf(l.length)).reduce(BigInteger.ONE,
				(v1, v2) -> v1.multiply(v2)));
		System.out.println(ccc.mc);
		ccc.ncs = ncs(ccc.mc, ccc.figuresAll); // Shifts for all (cached)

		final int taskCount = ccc.figuresAll[ccc.figuresAll.length - 1].length; //Task count assotiated with count of figures 
		List<CombinationContext> listcc = new ArrayList<CombinationContext>(taskCount);
	
		for (int i = 0; i < taskCount; i++) {
			CombinationContext cc = new CombinationContext(ccc);
			cc.goodSubCombinationBlocksBitSet = new BitSet();
			cc.goodSubCombinationCode = new int[cc.ccc.figuresAll.length]; // Current idx combination storage

			cc.jump = numFactory().setMutable(1);
			cc.startj = i == 0 ? numFactory().setMutable(0) 
					: listcc.get(i - 1).endj.add(1);
			cc.endj = (i == (taskCount - 1)) ? cc.ccc.mc
					: cc.ccc.mc.divide(taskCount).multiply(i + 1);

			listcc.add(i, cc);
			System.out.println(cc.startj + " - " + cc.endj);
		}

		//ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		ExecutorService executor = Executors.newCachedThreadPool();
		//ExecutorService executor = Executors.newSingleThreadExecutor();
		List<Callable<List<List<Figure>>>> callables = listcc.stream()
				.map(c -> (Callable<List<List<Figure>>>) () -> SolutionService.checkCombinationsInterval(c)).toList();
		long prepareTime = System.currentTimeMillis();
		List<List<Figure>> solutions = new ArrayList<>();
		List<Future<List<List<Figure>>>> result = executor.invokeAll(callables);
		for (Future<List<List<Figure>>> future : result) {
			solutions.addAll(future.get());
		}

		LOG.info("Full time: " + (double) (System.currentTimeMillis() - calcStartTime) / 1000);
		LOG.info(" - Prepare time: " + (double) (prepareTime - calcStartTime) / 1000);
		LOG.info(" - Search time: " + (double) (System.currentTimeMillis() - prepareTime) / 1000);

		return solutions;

	}

	private static List<List<Figure>> checkCombinationsInterval(CombinationContext cc) {
		for (Num j = cc.startj; j.compareTo(cc.endj) < 0; j.addMutable(cc.jump)) { // Combination number
			checkCombination(j, cc);
		}
		return cc.solutions;
	}

	/**
	 * 
	 * https://ru.stackoverflow.com/questions/235030/%d0%9a%d0%b0%d0%ba-%d1%81%d0%be%d1%81%d1%82%d0%b0%d0%b2%d0%b8%d1%82%d1%8c-%d0%b2%d0%be%d0%b7%d0%bc%d0%be%d0%b6%d0%bd%d1%8b%d0%b5-%d0%ba%d0%be%d0%bc%d0%b1%d0%b8%d0%bd%d0%b0%d1%86%d0%b8%d0%b8-%d0%bc%d0%b5%d0%b6%d0%b4%d1%83-%d1%8d%d0%bb%d0%b5%d0%bc%d0%b5%d0%bd%d1%82%d0%b0%d0%bc%d0%b8-%d0%bc%d0%bd%d0%be%d0%b6%d0%b5%d1%81%d1%82%d0%b2
	 * 
	 * @param j
	 * @param cc
	 */
	private static void checkCombination(Num j, CombinationContext cc) {
		boolean isShift = false;
		for (int variantIndex = cc.ccc.figuresAll.length - 1; variantIndex >= 0; variantIndex--) { // Бежим по
																									// множествам

			Figure[] figuresAllvariantIndex = cc.ccc.figuresAll[variantIndex];
			
			int idx = j.divide(cc.ccc.ncs[variantIndex]).mod(figuresAllvariantIndex.length); // Current shift

			int[] idxBlocks = figuresAllvariantIndex[idx].getRowBlocks();

			if (!absentAll(cc.goodSubCombinationBlocksBitSet, idxBlocks)) {
				cc.jump = cc.ccc.ncs[variantIndex];
				isShift = (variantIndex == cc.ccc.figuresAll.length - 2);
				break;
			}
			cc.jump = numOneFactory();

			addAll(cc.goodSubCombinationBlocksBitSet, idxBlocks);

			cc.goodSubCombinationCode[cc.ccc.figuresAll.length - (variantIndex + 1)] = idx;

			if (variantIndex == 0) {
				List<Figure> listSolution = new ArrayList<Figure>(cc.ccc.figuresAll.length);
				for (int variantIndexSol = cc.ccc.figuresAll.length - 1; variantIndexSol >= 0; variantIndexSol--) {
					listSolution
							.add(cc.ccc.figuresAll[variantIndexSol][cc.goodSubCombinationCode[cc.ccc.figuresAll.length
									- (variantIndexSol + 1)]]);
				}
				cc.solutions.add(Collections.unmodifiableList(listSolution));
				LOG.info("Solution " + cc.solutions.size() + " was found on combination number: " + j);
				Figure.drowFigures(listSolution); System.out.println();
			}

		}
		cc.goodSubCombinationBlocksBitSet.clear();
//		System.out.println();
		if (isShift) {
			System.out.println(new BigDecimal(j.getBigInteger()).divide(new BigDecimal(cc.ccc.mc.getBigInteger()), 2, RoundingMode.FLOOR)
					.multiply(BigDecimal.valueOf(100).round(new MathContext(1, RoundingMode.HALF_EVEN))) + "% Interval " + cc.startj + " - " + cc.endj);
		}
	}

	private static Num[] ncs(Num mc, Figure[][] figuresAll) {
		Num[] ncs = new Num[figuresAll.length];
		for (int variantIndex = figuresAll.length - 1; variantIndex >= 0; variantIndex--) { // Бежим по множествам
			Figure[] figuresAllvariantIndex = figuresAll[variantIndex];
			ncs[variantIndex] = variantIndex == figuresAll.length - 1 ? mc.divide(figuresAllvariantIndex.length) // Current
					: ncs[variantIndex + 1].divide(figuresAllvariantIndex.length); // Current
		}
		return ncs;
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
	
	// factory
	
	private static Num numFactory() {
		return new NumLong();
	}
	
	private static Num ONE = new NumLong(1); 
	
	private static Num numOneFactory() {
		return ONE;
	}

}
