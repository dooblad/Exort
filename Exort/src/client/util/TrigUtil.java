package client.util;

/**
 * Utility class for trigonometric operations.
 */
public class TrigUtil {
	/**
	 * Returns the angle (in radians) of the line from the origin to ("x", 0, "z"), with
	 * respect to the y-axis.
	 */
	public static float calculateAngle(double x, double z) {
		float result = (float) Math.atan(z / x);
		if (x < 0) {
			result += Math.PI;
		}
		return result;
	}
}
