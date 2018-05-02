import java.awt.Color;

public class ThemeManager {

	private static Color red = new Color(0.9F, 0.0F, 0.0f, 1F);
	private static Color redLine = new Color(0.9F, 0.0F, 0.0F, 0.5F);

	private static Color green = new Color(0.0F, 0.9F, 0.0f, 1F);
	private static Color greenLine = new Color(0.0F, 0.9F, 0.0F, 0.5F);

	private static Color blue = new Color(0.0F, 0.0F, 0.9f, 1F);
	private static Color blueLine = new Color(0.0F, 0.0F, 0.9F, 0.5F);

	private static Color staticColor = new Color(0.9F, 0.9F, 0.9F);

	public static Color getColor() {
		switch (Main.theme) {
		case RED:
			return red;
		case BLUE:
			return blue;
		case GREEN:
			return green;
		case STATIC:
		case RAINBOW:
			return new Color(staticColor.getRed() / 255.0F, staticColor.getGreen() / 255.0F,
					staticColor.getBlue() / 255.0F, 1.0F);
		default:
			return new Color(-1);
		}
	}

	public static Color getColorLine() {
		switch (Main.theme) {
		case RED:
			return redLine;
		case BLUE:
			return blueLine;
		case GREEN:
			return greenLine;
		case STATIC:
		case RAINBOW:
			return new Color(staticColor.getRed() / 255.0F, staticColor.getGreen() / 255.0F,
					staticColor.getBlue() / 255.0F, 0.5F);
		default:
			return new Color(-1);
		}
	}

	public static void generateRainbow() {
		if (Main.theme == ColorTheme.RAINBOW) {
			staticColor = Color.getHSBColor(
					System.nanoTime() / 1.0E10F % (1.0F / Main.rainbowSpeed) * Main.rainbowSpeed, 1.0F, 1.0F);
		}
	}

}