import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main {

	static JFrame jframe;

	static JPanel drawPanel;

	static JPanel contentPane;

	static ParticleSystem particleSystem;

	static boolean respawnWhenRightClick, isMouseDown0, isMouseDown1, isShiftDown, isRedDown, isGreenDown, isBlueDown,
			fullscreen, isWDown, isADown, isSDown, isDDown, freeze;

	static boolean lines = true;

	static Point lastMouse = null;

	static float rainbowSpeed = 1;

	static ColorTheme theme = ColorTheme.STATIC;

	public static void main(String[] args) {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						drawPanel.repaint();
						Thread.sleep(1l);
					} catch (Exception e) {
					}
				}
			}
		}).start();
		particleSystem = new ParticleSystem(100);
		generateWindow(false);
	}

	public static void generateWindow(boolean fullscreen) {
		jframe = new JFrame();
		Main.fullscreen = fullscreen;
		jframe.setTitle("Particles");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (fullscreen) {
			jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
			jframe.setUndecorated(true);
		} else {
			jframe.setBounds((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - 427),
					(int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - 240), 854, 480);
		}
		contentPane = new JPanel();
		contentPane.setFocusable(true);
		contentPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 122) {
					jframe.setVisible(false);
					jframe.dispose();
					generateWindow(!fullscreen);
				} else if (e.getKeyCode() == 16) {
					isShiftDown = true;
				} else if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W') {
					isWDown = true;
				} else if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
					isADown = true;
				} else if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
					isSDown = true;
				} else if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
					isDDown = true;
				} else if (e.getKeyChar() == 'f' || e.getKeyChar() == 'F') {
					freeze = !freeze;
				} else if (e.getKeyChar() == 'r' || e.getKeyChar() == 'R') {
					isRedDown = true;
					theme = ColorTheme.RED;
				} else if (e.getKeyChar() == 'g' || e.getKeyChar() == 'G') {
					isGreenDown = true;
					theme = ColorTheme.GREEN;
				} else if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
					isBlueDown = true;
					theme = ColorTheme.BLUE;
				} else if (e.getKeyChar() == 'e' || e.getKeyChar() == 'E') {
					if (theme == ColorTheme.RAINBOW) {
						theme = ColorTheme.STATIC;
					}
				} else if (e.getKeyChar() == 'q' || e.getKeyChar() == 'Q') {
					respawnWhenRightClick = !respawnWhenRightClick;
				} else if (e.getKeyChar() == 'l' || e.getKeyChar() == 'L') {
					lines = !lines;
				} else if (e.getKeyChar() == '+') {
					if (rainbowSpeed == -1) {
						rainbowSpeed = 1;
					} else {
						rainbowSpeed++;
					}
				} else if (e.getKeyChar() == '-') {
					if (rainbowSpeed != 1) {
						rainbowSpeed--;
					}
				}
				if (isRedDown && isGreenDown && isBlueDown) {
					theme = ColorTheme.RAINBOW;
				}
				e.consume();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 16) {
					isShiftDown = false;
				} else if (e.getKeyChar() == 'w' || e.getKeyChar() == 'W') {
					isWDown = false;
				} else if (e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
					isADown = false;
				} else if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
					isSDown = false;
				} else if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
					isDDown = false;
				} else if (e.getKeyChar() == 'r' || e.getKeyChar() == 'R') {
					isRedDown = false;
				} else if (e.getKeyChar() == 'g' || e.getKeyChar() == 'G') {
					isGreenDown = false;
				} else if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
					isBlueDown = false;
				}
				e.consume();
			}
		});
		contentPane.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				particleSystem.addAmount(-e.getWheelRotation() * (isShiftDown ? 10 : 1));
			}
		});
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == 1)
					isMouseDown0 = true;
				else if (e.getButton() == 3)
					isMouseDown1 = true;
				e.consume();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == 1)
					isMouseDown0 = false;
				else if (e.getButton() == 3)
					isMouseDown1 = false;
				e.consume();
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		jframe.setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		drawPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Point mouse = getMousePosition();
				if (mouse != null) {
					particleSystem.render(g, this, mouse.x, mouse.y);
					lastMouse = mouse;
				} else {
					if (lastMouse != null) {
						particleSystem.render(g, this, lastMouse.x, lastMouse.y);
					} else {
						particleSystem.render(g, this, 0, 0);
					}
				}
			}
		};
		drawPanel.setBackground(Color.GRAY);
		contentPane.add(drawPanel, BorderLayout.CENTER);
		jframe.setVisible(true);
	}

}
