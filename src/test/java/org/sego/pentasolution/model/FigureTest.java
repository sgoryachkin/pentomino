package org.sego.pentasolution.model;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.sego.pentasolution.service.SolutionService;

public class FigureTest {

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
	public void clac8() {
		//List<Figure> figures = List.of(Figure.L, Figure.V, Figure.N, Figure.U, Figure.X,Figure.I);
		//List<Figure> figures = List.of(Figure.L, Figure.V, Figure.N, Figure.U, Figure.X);
		//List<Figure> figures = List.of(Figure.L, Figure.Y, Figure.T, Figure.P, Figure.W);
		List<Figure> figures = List.of(Figure.L, Figure.Y, Figure.V, Figure.N, Figure.Z,Figure.P, Figure.W, Figure.X); // 8 - 33
		SolutionService.clac(8, 5, figures);
	}
	


	@Test
	public void rotate() {
		System.out.println(Figure.X.toString());
		Figure.X.drow();
		System.out.println("Variants:");
		for (Figure f : Figure.X.getAllVariants()) {
			f.drow();
		}

		System.out.println(Figure.L.toString());
		Figure.L.drow();
		System.out.println("Variants:");
		for (Figure f : Figure.L.getAllVariants()) {
			f.drow();
		}

	}

	@Test
	public void desk() {
		System.out.println(Figure.L.toString());
		Figure.L.drow();
		System.out.println("Variants:");
		for (Figure f : Figure.L.getDescVariants(10, 6)) {
			f.drow();
		}
	}


}
