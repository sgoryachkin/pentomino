package org.sego.pentasolution.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.sego.pentasolution.service.SolutionService;

public class FigureTest {
	
	@Test
	public void block() {
		Block b = new Block(63, 63);
		assertEquals(63, b.x());
		assertEquals(63, b.y());
		assertEquals(64*63+63, b.row());
	}

	@Test
	public void flist() {
		List<Figure> figures = List.of(Figure.F, Figure.L, Figure.N, Figure.T, Figure.U, Figure.V, Figure.X, Figure.Z,
				Figure.Y, Figure.P, Figure.W, Figure.I);
		System.out.println("All Figures:");
		for (Figure f : figures) {
			System.out.println(f.toString());
			f.drow();
		}
	}
	
	@Test
	public void clac6() {
		List<Figure> figures = List.of(Figure.L, Figure.V, Figure.N, Figure.U, Figure.X,Figure.I);
		List<List<Figure>> solution = SolutionService.clac(6, 5, figures);
		solution.stream().forEach(f -> {Figure.drowFigures(f); System.out.println();});
		assertEquals(8, solution.size());
		
	}

	@Test
	public void clac8() {
		List<Figure> figures = List.of(Figure.L, Figure.Y, Figure.V, Figure.N, Figure.Z,Figure.P, Figure.W, Figure.X); // 8 - 33
		List<List<Figure>> solution = SolutionService.clac(8, 5, figures);
		solution.stream().forEach(f -> {Figure.drowFigures(f); System.out.println();});
		assertEquals(16, solution.size());
	}
	
	@Test
	public void clac9() {
		List<Figure> figures = List.of(Figure.L, Figure.P, Figure.U, Figure.F, Figure.Y,Figure.W, Figure.V, Figure.Z, Figure.N); // 9 - A
		SolutionService.clac(9, 5, figures).stream().forEach(f -> {Figure.drowFigures(f); System.out.println();});
	}
	
	@Test
	public void clac10() {
		List<Figure> figures = List.of(Figure.L, Figure.P, Figure.U, Figure.F, Figure.Y,Figure.W, Figure.V, Figure.Z, Figure.N, Figure.I); 
		SolutionService.clac(10, 5, figures).stream().forEach(f -> {Figure.drowFigures(f); System.out.println();});
	}
	


	@Test
	public void rotate() {
		System.out.println(Figure.X.toString());
		Figure.X.drow();
		System.out.println("Variants:");
		for (Figure f : Figure.X.getAllRotations()) {
			f.drow();
		}

		System.out.println(Figure.L.toString());
		Figure.L.drow();
		System.out.println("Variants:");
		for (Figure f : Figure.L.getAllRotations()) {
			f.drow();
		}

	}
	
	@Test
	public void arrayPerf() {
		int[] array = new int[] {1,2,3,4};
		Set<Integer> set = Set.of(1,2,3,4);
		int arrayR = 0;
		int setR = 0;
		
		long arrayStrt = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
//			for (Integer integer : array) {
//				arrayR+=integer;
//			}
			for (int integer = 0; integer<array.length ; integer++) {
				arrayR+=array[integer];
			}
		}
		
		long setStart = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			for (Integer integer : set) {
				setR+=integer;
			}
		}
		
		long end = System.currentTimeMillis();
		assertEquals(arrayR, setR);
		
		System.out.println("Array: " + (setStart - arrayStrt));
		System.out.println("Set  : " + (end - setStart));
		
		
		
		
		
		
		
	}

	@Test
	public void desk() {
		System.out.println(Figure.L.toString());
		Figure.L.drow();
		System.out.println("Variants:");
		for (Figure f : Figure.L.getAllPositions(10, 6)) {
			f.drow();
		}
	}


}
