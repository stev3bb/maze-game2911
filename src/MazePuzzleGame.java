import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MazePuzzleGame implements Runnable {

	private static Thread mazeGameThread;
	private static Thread guiThread;
	
	private JFrame mainFrame;
	private MenuPanel menuPanel;
	private GameBoardPanel gamePanel;
	private GameEngine gameEngine;
	private JPanel homeGlassPane;
	
	public MazePuzzleGame(GameEngine ge) throws IOException {
		this.gameEngine = ge;
		
		mainFrame = new JFrame("Mario's Maze");
		mainFrame.setSize(300, 700);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuPanel = new MenuPanel(this);
		menuPanel.setPreferredSize(new Dimension(300, 700));

		homeGlassPane = new JPanel();
		homeGlassPane.setPreferredSize(new Dimension(100, 100));
		
		gamePanel = new GameBoardPanel(null, this);
		gamePanel.setPreferredSize(new Dimension(700, 700));
	}
	
	public static void main(String[] args) throws IOException {
		
		mazeGameThread = new Thread(new GameEngine());
		
		guiThread = new Thread(new MazePuzzleGame(new GameEngine()));
		
		mazeGameThread.start();
		guiThread.start();
	}
	
	private void display() {
		if (homeGlassPane != null) {
			mainFrame.setGlassPane(homeGlassPane);
			homeGlassPane.setOpaque(false);
			homeGlassPane.setVisible(true);
		}
		mainFrame.getContentPane().add(menuPanel, BorderLayout.WEST);
		mainFrame.setResizable(false);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}

	public void setVisibility(boolean visible) {
		mainFrame.setVisible(visible);
	}

	public GameEngine getGameEngine() {
		return gameEngine;
	}

	public MenuPanel getMenuPanel() {
		return menuPanel;
	}

	public JFrame getMainFrame() {
		return mainFrame;
	}

	public void suspendGame() {
		gameEngine.suspendGame();
		guiThread.interrupt();
	}
	
	@Override
	public void run() {
		display();
		
		while (true) {
			if (!gameEngine.isInGame() && mainFrame.isVisible()) {
				gamePanel.startSimulationGame();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		
	}
	//public getGamePanel
}
