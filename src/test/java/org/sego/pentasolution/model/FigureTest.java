package org.sego.pentasolution.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.sego.pentasolution.service.SolutionService;

public class FigureTest {
	
	@Test
	public void block() {
		Block b = new Block(12, 15);
		assertEquals(12, b.x());
		assertEquals(15, b.y());
		
		Block b2 = new Block(7, 0);
		assertEquals(7, b2.x());
		assertEquals(0, b2.y());
		
		Block b3 = new Block(0, 7);
		assertEquals(0, b3.x());
		assertEquals(7, b3.y());
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
	public void clac6() throws InterruptedException, ExecutionException {
		List<Figure> figures = List.of(Figure.L, Figure.V, Figure.N, Figure.U, Figure.X,Figure.I);
		List<List<Figure>> solution = SolutionService.clac(6, 5, figures);
		solution.stream().forEach(f -> {Figure.drowFigures(f); System.out.println();});
		System.out.println(solution.size());
		assertEquals(8, solution.size());
	}

	@Test
	public void clac8() throws InterruptedException, ExecutionException {
		List<Figure> figures = List.of(Figure.L, Figure.Y, Figure.V, Figure.N, Figure.Z,Figure.P, Figure.W, Figure.X); // 8 - 33
		List<List<Figure>> solution = SolutionService.clac(8, 5, figures);
		solution.stream().forEach(f -> {Figure.drowFigures(f); System.out.println();});
		System.out.println(solution.size());
		assertEquals(16, solution.size());
	}
	
	@Test
	public void clac9() throws InterruptedException, ExecutionException {
		List<Figure> figures = List.of(Figure.L, Figure.P, Figure.U, Figure.F, Figure.Y,Figure.W, Figure.V, Figure.Z, Figure.N); // 9 - A
		List<List<Figure>> solution = SolutionService.clac(9, 5, figures);
		solution.stream().forEach(f -> {Figure.drowFigures(f); System.out.println();});
		System.out.println(solution.size());
		assertEquals(204, solution.size());//???
	}
	
	@Test
	public void clac10() throws InterruptedException, ExecutionException {
		List<Figure> figures = List.of(Figure.L, Figure.P, Figure.U, Figure.F, Figure.Y,Figure.W, Figure.V, Figure.Z, Figure.N, Figure.I); 
		List<List<Figure>> solution = SolutionService.clac(10, 5, figures);
		solution.stream().forEach(f -> {Figure.drowFigures(f); System.out.println();});
		System.out.println(solution.size());
		assertEquals(16, solution.size());//???
	}
	
	@Test
	public void clac12() throws InterruptedException, ExecutionException {
		List<Figure> figures = List.of(Figure.L, Figure.P, Figure.U, Figure.F, Figure.Y,Figure.W, Figure.V, Figure.Z, Figure.N, Figure.I, Figure.X, Figure.T); 
		List<List<Figure>> solution = SolutionService.clac(12, 5, figures);
		solution.stream().forEach(f -> {Figure.drowFigures(f); System.out.println();});
		System.out.println(solution.size());
		assertEquals(16, solution.size());//???
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
