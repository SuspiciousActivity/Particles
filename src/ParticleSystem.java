import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class ParticleSystem {
	private int amount;
	private final ArrayList<Particle> particles;

	public ParticleSystem(final int amount) {
		this.amount = amount;
		particles = new ArrayList<>();
	}

	public void render(final Graphics g, final JPanel pan, final int mouseX, final int mouseY) {
		if (particles.size() < amount && !Main.INSTANCE.isMouseDown1) {
			newParticle();
		} else if (particles.size() > amount) {
			particles.remove(0);
		}
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, pan.getWidth(), pan.getHeight());

		ThemeManager.generateRainbow();
		Color color = ThemeManager.getColor();
		Color lineColor = ThemeManager.getColorLine();

		final ArrayList<Particle> remove = new ArrayList<>();
		particles.stream().map((p) -> {
			if (p.isReset()) {
				remove.add(p);
			}
			return p;
		}).forEachOrdered((p) -> {
			final int range = 50;
			if (Main.lines && Math.abs(mouseX - p.getX()) < range && Math.abs(mouseY - p.getY()) < range) {
				p.render(color, lineColor, g, particles, mouseX, mouseY);
			} else {
				p.render(color, lineColor, g, null, mouseX, mouseY);
			}
		});

		remove.forEach((p) -> {
			particles.remove(p);
		});
	}

	public void addAmount(int amount) {
		int tmp = this.amount + amount;
		if (tmp >= 0) {
			this.amount = tmp;
		}
	}

	private void newParticle() {
		final Random r = new Random();
		final int id = r.nextInt(1000);
		for (final Particle p : particles) {
			if (id == p.getID()) {
				return;
			}
		}

		particles.add(new Particle(r.nextInt(Main.INSTANCE.getWidth()), r.nextInt(Main.INSTANCE.getHeight()), id));
	}
}
