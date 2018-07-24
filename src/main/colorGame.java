package main;

import java.util.Scanner;

public class colorGame {
	public static void main(String[] args) {
		// Inputs needed
		int x = -1, y = -1,  col = -1,  iter = -1;
		// Max width
		int maxWid = Application.Dwid - 2 * Application.Dborder;
		// Max height
		int maxHei = Application.Dhei - 2 * Application.Dborder;
		// Max colors
		int maxCol = Application.colorCount;
		// Mac iterations
		int maxIter = 100;
		// Make input scanner
		Scanner reader = new Scanner(System.in); 
		
		// Get width
		System.out.println("Enter a width from size 1 to " + Integer.toString(maxWid)+':');
		while(x <= 1 || x > maxWid) {
			try {
				// Get input
				x = reader.nextInt();
				// Handle negative/zero
				if(x < 1) {
					System.out.println("Please enter a positive value:");
				}
				// Handle too large
				if(x > maxWid) {
					System.out.println("Please enter a value below " + Integer.toString(maxWid)+':');
				}
			}
			catch(Exception e) {
				// Handle non-number
				System.out.println("Please enter a number:");
			}
			// Clear buffer
			reader.nextLine();
		}
		
		// Get height
		System.out.println("Enter a height from size 1 to " + Integer.toString(maxHei)+':');
		while(y < 1 || y > maxHei) {
			try {
				// Get input
				y = reader.nextInt();
				// Handle negative/zero
				if(y < 1) {
					System.out.println("Please enter a positive value:");
				}
				// Handle too large
				if(y > maxHei) {
					System.out.println("Please enter a value below " + Integer.toString(maxHei)+':');
				}
			}
			catch(Exception e) {
				// Handle non-number
				System.out.println("Please enter a number:");
			}
			// Clear buffer
			reader.nextLine();
		}
		
		// Get color count
		System.out.println("Enter color count from 2 to " + Integer.toString(maxCol)+':');
		while(col < 2 || col > maxCol) {
			try {
				// Get input
				col = reader.nextInt();
				// Handle negative/zero
				if(col < 2) {
					System.out.println("Please enter a positive value:");
				}
				// Handle too large
				if(col > maxCol) {
					System.out.println("Please enter a value below " + Integer.toString(maxCol)+':');
				}
			}
			catch(Exception e) {
				// Handle non-number
				System.out.println("Please enter a number:");
			}
			// Clear buffer
			reader.nextLine();
		}
		
		// Get iteration
		System.out.println("Enter a loop count from size 0 to " + Integer.toString(maxIter)+':');
		while(iter < 0 || iter > maxIter) {
			try {
				// Get input
				iter = reader.nextInt();
				// Handle negative/zero
				if(iter < 0) {
					System.out.println("Please enter a positive value:");
				}
				// Handle too large
				if(iter > maxIter) {
					System.out.println("Please enter a value below " + Integer.toString(maxIter)+':');
				}
			}
			catch(Exception e) {
				// Handle non-number
				System.out.println("Please enter a number:");
			}
			// Clear buffer
			reader.nextLine();
		}
		
		// Make application
		reader.close();
		new Application(x, y, col, iter);
		// Max size
//		new Application(1000, 736, 3, 10);
		// Half size
//		new Application(500,368,8, 0);
		// 1/8 size
//		new Application(125,92,2, 4);
		// Smaller
//		new Application(50, 50, 8, 5);
		// Smallest
//		new Application(10, 10, 5, 15);
	}

}
