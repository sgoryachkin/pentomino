package org.sego.pentasolution.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.sego.pentasolution.model.Block;
import org.sego.pentasolution.model.Figure;

public class SolutionService {
	
	private static final Logger LOG = Logger.getLogger(SolutionService.class.toString());
	
	public static void clac(int x, int y, List<Figure> figures) {

		List<List<Figure>> figuresAll = figures.stream().map(f -> new ArrayList<Figure>(f.getAllRotationsWithAllPositions(x, y)))
				.sorted(Collections.reverseOrder(Comparator.comparingInt(v -> v.size())))
				.collect(Collectors.toUnmodifiableList());
		LOG.info("Board: x=" + x + ", y=" + y);
		LOG.info("All Figures:");
		LOG.info(() -> {
			StringBuilder sb = new StringBuilder();
			for (List<Figure> f : figuresAll) {
				sb.append(f.size()).append(", ");
			}
			return sb.toString();
		});
		


		final BigInteger mc = figuresAll.stream().map(l -> BigInteger.valueOf(l.size())).reduce(BigInteger.ONE, (v1, v2) -> v1.multiply(v2));
		System.out.println(mc);
		// https://ru.stackoverflow.com/questions/235030/%d0%9a%d0%b0%d0%ba-%d1%81%d0%be%d1%81%d1%82%d0%b0%d0%b2%d0%b8%d1%82%d1%8c-%d0%b2%d0%be%d0%b7%d0%bc%d0%be%d0%b6%d0%bd%d1%8b%d0%b5-%d0%ba%d0%be%d0%bc%d0%b1%d0%b8%d0%bd%d0%b0%d1%86%d0%b8%d0%b8-%d0%bc%d0%b5%d0%b6%d0%b4%d1%83-%d1%8d%d0%bb%d0%b5%d0%bc%d0%b5%d0%bd%d1%82%d0%b0%d0%bc%d0%b8-%d0%bc%d0%bd%d0%be%d0%b6%d0%b5%d1%81%d1%82%d0%b2
		List<Figure> combination = new ArrayList<>(figuresAll.size()); // Текущая комбинация
		for (BigInteger j = BigInteger.ZERO; j.compareTo(mc) < 0; j = j.add(BigInteger.ONE)) { // Номер текущей комбинации
			BigInteger nc = mc; // Сдвиг разрядов
			boolean isShift = false;
			for (int variantIndex = figuresAll.size() - 1; variantIndex >= 0; variantIndex--) { // Бежим по множествам
				nc = nc.divide(BigInteger.valueOf(figuresAll.get(variantIndex).size())); // Двигаем разряд влево дальше
				BigInteger shifted = floorDiv(j, nc); // Номер комбинации,
				                                        // сдвинутый на i разрядов влево.
				                                        // Аналог для двоичной системы <<i
				int idx = shifted.mod(BigInteger.valueOf(figuresAll.get(variantIndex).size())).intValue(); // Индекс элемента в i-том разряде
//				System.out.print("nc=" + nc + " shifted=" + shifted + " > " + idx + " ");
				combination.add(figuresAll.get(variantIndex).get(idx)); // Заносим в комбинацию элемент по полученному индексу
				
				
				if (!checkCombination(combination, variantIndex==0)) {	
					j = j.add(nc).subtract(BigInteger.ONE);
					isShift = (combination.size() == 2);
					break;
				}
				
			}
			combination.clear();
//			System.out.println();
			if (j.mod(BigInteger.valueOf(10000000)).equals(BigInteger.ZERO) || isShift) {
				//System.out.println(new BigDecimal(j).divide(new BigDecimal(mc) ,2, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100)) + "% " + j + " of " + mc);
				System.out.println(new BigDecimal(j).divide(new BigDecimal(mc), 2, RoundingMode.FLOOR).multiply(BigDecimal.valueOf(100).round(new MathContext(1, RoundingMode.HALF_EVEN))) + "%");
			}

		}


	}
	public static BigInteger floorDiv(final BigInteger x, final BigInteger y) {
	    BigInteger[] qr = x.divideAndRemainder(y);  
	    return qr[0].signum() >= 0 || qr[1].signum() == 0 ? 
	         qr[0] : qr[0].subtract(BigInteger.ONE);
	}
	
	private static boolean checkCombination(List<Figure> combination, boolean full) {
//		Set<Block> blocks = new HashSet<>(combination.get(0).getBlocks());
//		for (int i = 1; i < combination.size(); i++) {
			
//		}
		Set<Block> blocks = new HashSet<>();
		for (Figure f : combination) {
			for (Block b : f.getBlocks()) {
				if (!blocks.add(b)) {
					return false;
				}
			}
		}
		if (full) {
			System.out.println("solution");
			Figure.drowFigures(combination);
			System.out.println();
		}
		/*
		 * System.out.println(combination.size()); if (combination.size()==2) {
		 * 
		 * return false; }
		 */
		return true;
	}

}
