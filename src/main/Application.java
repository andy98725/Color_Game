package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Application extends JPanel implements Runnable {
	// Serial ID
	private static final long serialVersionUID = 1L;
	// Display size
	static final int Dwid = 1024, Dhei = 760, Dborder = 12;
	// Main thread
	private Thread mainLoop = new Thread(this);
	// Grid size
	static int wid, hei;
	// Grid
	int[][] colorGrid;
	// Grid refresh count
	int iterationsLeft, iterationCount;
	// Color count
	static int colorCount = 8;
	// Colors
	static final Color[] colormap = new Color[colorCount];

	protected void defineColors() {
		colormap[0] = Color.RED;
		colormap[1] = Color.GREEN;
		colormap[2] = Color.BLUE;
		colormap[3] = Color.YELLOW;
		colormap[4] = Color.WHITE;
		colormap[5] = Color.MAGENTA;
		colormap[6] = Color.CYAN;
		colormap[7] = Color.GRAY;
	}

	// Initialize
	public Application(int wid, int hei, int colors, int iterations) {
		// Make colors
		defineColors();
		// Set iterations left
		iterationsLeft = iterations;
		iterationCount = iterations;
		// Set dimensions
		Application.wid = wid;
		Application.hei = hei;
		// Set color limit
		colorCount = colors;
		// Calculate drawing dimensions
		calculateDrawingDimensions();
		// Make board
		makeInitialBoard();
		// Create window
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				initWindow();
			}
		});
	}

	// Make container window
	private void initWindow() {
		// Set size
		setPreferredSize(new Dimension(Dwid, Dhei));
		// Improves rendering
		setDoubleBuffered(true);
		// Default bgcolor
		setBackground(Color.BLACK);
		// Make window
		JFrame window = new JFrame();
		// Add app
		window.add(this);
		// Add listener
		window.addKeyListener(refresher);
		// Not resizable
		window.setResizable(false);
		// Window header
		window.setTitle("Colors");
		// Close app on close
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Pack dimensions
		window.pack();
		// Center
		window.setLocationRelativeTo(null);
		// Display
		window.setVisible(true);
	}

	// On window run, start rendering
	@Override
	public void addNotify() {
		super.addNotify();
		mainLoop.start();
	}

	@Override
	public void run() {
		// Loop
		while (iterationsLeft > 0) {
			// Wait a second between iterations
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// This will happen when it gets refreshed
			}
			doIteration();
			iterationsLeft--;
			repaint();
		}

	}

	// Refresh display
	protected void refresh() {
		// Make initial board
		makeInitialBoard();
		// Stop thread
		mainLoop.interrupt();
		// Refresh counter
		iterationsLeft = iterationCount;
		// Start thread
		mainLoop.start();
	}

	// Make initial board
	protected void makeInitialBoard() {
		// Define
		colorGrid = new int[wid][hei];
		// Get random
		Random r = new Random();
		// Fill
		for (int x = 0; x < wid; x++) {
			for (int y = 0; y < hei; y++) {
				colorGrid[x][y] = r.nextInt(colorCount);
			}
		}
	}

	// Do iteration
	protected void doIteration() {
		// New grid
		int[][] newGrid = new int[wid][hei];
		// Loop through squares
		for (int x = 0; x < wid; x++) {
			for (int y = 0; y < hei; y++) {
				// Count each color
				int[] counter = new int[colorCount];
				// Make limits
				int minX = Math.max(x - 1, 0), minY = Math.max(y - 1, 0);
				int maxX = Math.min(x + 2, wid), maxY = Math.min(y + 2, hei);
				// Loop through neighboring cells and count
				for (int i = minX; i < maxX; i++) {
					for (int j = minY; j < maxY; j++) {
						counter[colorGrid[i][j]]++;
					}
				}
				// Now find index with most colors. Handle ties separately.
				boolean tied = true;
				int maxCount = 0, maxIndex = 0;
				for (int i = 0; i < colorCount; i++) {
					if (counter[i] > maxCount) {
						// Set as max
						tied = false;
						maxCount = counter[i];
						maxIndex = i;
					} else if (counter[i] == maxCount) {
						// Set tied flag
						tied = true;
						maxIndex = -1;
					}
				}
				// Now if there was no tie, set new grid to color. If there was, copy from old
				// grid.
				if (tied) {
					newGrid[x][y] = colorGrid[x][y];
				} else {
					newGrid[x][y] = maxIndex;
				}
			}
		}
		// Update
		colorGrid = newGrid;
	}

	// Drawing dimensions
	int drawPixelSize, drawBorderSize, drawPixelWOff, drawPixelHOff;

	// Calculate drawing dimensions
	protected void calculateDrawingDimensions() {
		int scWid = Dwid - 2 * Dborder, scHei = Dhei - 2 * Dborder;
		// Find scale (Pixels per grid scale)
		int scale = Math.min(scWid / wid, scHei / hei);
		// Based off scale, calculate pixel size and offsets/border
		drawPixelSize = scale;
		drawPixelWOff = (scWid - (wid * scale)) / 2 + Dborder;
		drawPixelHOff = (scHei - (hei * scale)) / 2 + Dborder;
		// Add border in around it
		drawBorderSize = drawPixelSize / 16;
	}

	// Override draw
	@Override
	protected void paintComponent(Graphics graphics) {
		// Paint whole thing black
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, Dwid, Dhei);
		// Calculate dimensions shorthand
		int xoff = drawPixelWOff + drawBorderSize;
		int yoff = drawPixelHOff + drawBorderSize;
		int w = drawPixelSize - 2 * drawBorderSize;
		// Draw the board
		for (int x = 0; x < wid; x++) {
			for (int y = 0; y < hei; y++) {
				// Draw pixel
				graphics.setColor(colormap[colorGrid[x][y]]);
				graphics.fillRect(xoff + x * drawPixelSize, yoff + y * drawPixelSize, w, w);
			}
		}
	}

	// Refresher
	KeyListener refresher = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int k = e.getKeyCode();
			if (k == KeyEvent.VK_SPACE) {
				refresh();
			}

		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}
	};
}
