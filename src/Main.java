import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	public static Main INSTANCE;

	private JPanel contentPane;
	private ParticleSystem particleSystem;

	boolean isMouseDown0, isMouseDown1, isShiftDown, isRedDown, isGreenDown, isBlueDown;

	public static boolean lines = true;

	Point lastMouse = null;

	public static float rainbowSpeed = 1;

	public static ColorTheme theme = ColorTheme.STATIC;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		INSTANCE = this;
		particleSystem = new ParticleSystem(100);
		setTitle("Particles");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 798, 512);
		contentPane = new JPanel();
		contentPane.setFocusable(true);
		contentPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 16) {
					isShiftDown = true;
				} else if (e.getKeyChar() == 'r' || e.getKeyChar() == 'R') {
					isRedDown = true;
					theme = ColorTheme.RED;
				} else if (e.getKeyChar() == 'g' || e.getKeyChar() == 'G') {
					isGreenDown = true;
					theme = ColorTheme.GREEN;
				} else if (e.getKeyChar() == 'b' || e.getKeyChar() == 'B') {
					isBlueDown = true;
					theme = ColorTheme.BLUE;
				} else if (e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
					if (theme == ColorTheme.RAINBOW) {
						theme = ColorTheme.STATIC;
					}
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
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel() {
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
		panel.setBackground(Color.GRAY);
		contentPane.add(panel, BorderLayout.CENTER);
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					panel.repaint();
					try {
						Thread.sleep(1l);
					} catch (InterruptedException e) {
					}
				}
			}
		}).start();
	}

}
