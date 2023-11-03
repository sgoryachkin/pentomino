package org.sego.pentasolution.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

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
	public void desk() {
		System.out.println(Figure.L.toString());
		Figure.L.drow();
		System.out.println("Variants:");
		for (Figure f : Figure.L.getAllPositions(10, 6)) {
			f.drow();
		}
	}


}
