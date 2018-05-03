import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Particle {
	private float x;
	private float y;
	private int k;
	private final int id;
	private final int size, halfSize;
	private boolean reset;
	private final Timer timer;
	private final int dir;

	private float realX;
	private float realY;

	public Particle(final int x, final int y, final int id) {
		timer = new Timer();
		this.x = x;
		this.y = y;
		realX = x;
		realY = y;
		this.id = id;
		dir = (int) genRandom(0, 360);
		size = 2;
		halfSize = size / 2;
	}

	public void render(final Color color, final Color lineColor, final Graphics g,
			final ArrayList<Particle> lastparticles, final int mouseX, final int mouseY) {
		if (realX < -1 || realY < -1 || realX > Main.jframe.getWidth() + 1.0
				|| realY > Main.jframe.getHeight() + 1.0) {
			reset = true;
		}

		float xx = (float) (Math.cos((dir + 90.0f) * 3.141592653589793 / 180.0) * k * 0.7);
		float yy = (float) (Math.sin((dir + 90.0f) * 3.141592653589793 / 180.0) * k * 0.7);

		timer.updateMS();
		if (timer.hasTimePassedM(20)) {
			++k;
			timer.updateLastMS();
		}
		if (Main.isMouseDown0 || Main.isMouseDown1) {
			final float distance = (float) Math
					.sqrt(Math.pow(Math.abs(realX - mouseX), 2) + Math.pow(Math.abs(realY - mouseY), 2))
					* (Main.isMouseDown0 ? 1.0f : -1.0f);
			if (realX > mouseX) {
				x -= (realX - mouseX) / distance;
			} else if (realX < mouseX) {
				x += (mouseX - realX) / distance;
			}

			if (realY > mouseY) {
				y -= (realY - mouseY) / distance;
			} else if (realY < mouseY) {
				y += (mouseY - realY) / distance;
			}
		}
		realX = this.x + xx;
		realY = this.y + yy;

		if (lastparticles != null) {
			for (final Particle p : lastparticles) {
				if (p.equals(this)) {
					continue;
				}
				if (getDistanceTo(p) < 100) {
					if (p != null) {
						g.setColor(lineColor);
						g.drawLine((int) (realX + halfSize), (int) (realY + halfSize), (int) (p.getX() + halfSize),
								(int) (p.getY() + halfSize));
					}
				}
			}
		}
		g.setColor(color);
		g.drawOval((int) realX, (int) realY, size, size);
	}

	public boolean isReset() {
		return reset;
	}

	public float getX() {
		return realX;
	}

	public float getY() {
		return realY;
	}

	public int getID() {
		return id;
	}

	private double getDistanceTo(final Particle p) {
		return Math.abs(getX() - p.getX()) + Math.abs(getY() - p.getY());
	}

	private float genRandom(final float min, final float max) {
		return (float) (min + Math.random() * (max - min + 1.0f));
	}
}
