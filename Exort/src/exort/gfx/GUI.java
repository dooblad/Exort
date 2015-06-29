package exort.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.lwjgl.input.*;

import com.doobs.modern.util.Color;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.matrix.*;

import exort.*;
import exort.util.*;
import exort.util.loaders.*;

public class GUI {
	private static final int PADDING = 5;
	private static final float[] GUI_COL = { 0f, 0f, 0f, 0.5f };
	private static final int VISIBLE_MESSAGES = 5;

	private Main main;

	private List<Message> messages;
	public Animation chatFade;
	private int messageOffset;

	public Animation exitDuel;
	public Animation pauseHover;
	private boolean exiting;
	private Rectangle exitBounds;

	public GUI(Main main) {
		this.main = main;
		this.messages = new ArrayList<Message>();
		this.chatFade = new Animation(120);
		this.messageOffset = 0;
		this.pauseHover = new Animation(8);
		this.exitDuel = new Animation(15);
		this.exiting = false;
		this.exitBounds = new Rectangle((main.getWidth() - 250) / 2, (main.getHeight() - 150) / 2, 250, 150);
	}

	public void tick(boolean paused, int delta) {
		this.chatFade.tickUp(delta);

		if (this.main.input.isKeyPressed(Keyboard.KEY_UP) && (this.messageOffset < (this.messages.size() - VISIBLE_MESSAGES))) {
			this.chatFade.empty();
			this.messageOffset++;
		} else if (this.main.input.isKeyPressed(Keyboard.KEY_DOWN) && (this.messageOffset != 0)) {
			this.chatFade.empty();
			this.messageOffset--;
		}

		if (paused && this.exitBounds.intersects(Mouse.getX(), Mouse.getY(), 1, 1)) {
			this.pauseHover.tickUp(delta);

			if (this.main.input.isMouseButtonPressed(0)) {
				this.exiting = true;
				this.chatFade.fill();
			}
		} else {
			this.pauseHover.tickDown(delta);
		}

		if (this.exiting) {
			this.pauseHover.fill();
			this.exitDuel.tickUp(delta);
		}
	}

	public void render(String text, boolean paused, boolean typing) {
		Matrices.switchToOrtho();
		Matrices.loadIdentity();

		Shaders.use("font");
		Matrices.sendMVPMatrix(Shaders.current);
		Fonts.centuryGothic.setSize(15);
		Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f);

		Shaders.use("gui");
		Matrices.sendMVPMatrix(Shaders.current);

		glEnable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);

		if (typing) {
			this.chatFade.empty();

			// Draw background
			Shaders.use("gui");
			glActiveTexture(GL_TEXTURE0);
			Textures.get("white").bind();
			Dimension d = Fonts.centuryGothic.getPhraseDimensions(text);
			renderChatBackground(d, 10, 10, 1f);
			Shaders.use("font");
			Fonts.centuryGothic.draw(text, 10, 10);
		}

		if (!this.chatFade.isFull()) {
			float alpha = Math.min(1f, 1f - ((this.chatFade.getPercentage() - 0.8f) * 5f));
			Dimension d;
			int y = 10 + Fonts.centuryGothic.getPhraseHeight(text) + (PADDING * 2);
			for (int i = 0; i < VISIBLE_MESSAGES; i++) {
				if (i < this.messages.size()) {
					Message message = this.messages.get(this.messages.size() - i - this.messageOffset - 1);
					d = Fonts.centuryGothic.getPhraseDimensions(message.getText());

					// Draw chat background
					Shaders.use("gui");
					glActiveTexture(GL_TEXTURE0);
					Textures.get("white").bind();
					renderChatBackground(d, 10, y, alpha);

					Shaders.use("font");
					message.draw(10, y);

					y += d.height + (PADDING * 2);
				}
			}
		}

		if (paused) {
			Shaders.use("gui");
			glActiveTexture(GL_TEXTURE0);
			Textures.get("white").bind();
			Color.set(Shaders.current, GUI_COL[0], GUI_COL[1], GUI_COL[2], GUI_COL[3] + (this.exitDuel.getSmoothedPercentage() * (1 - GUI_COL[3])));

			// Percentages
			float exit = this.exitDuel.getSmoothedPercentage();
			float inverseExit = 1 - this.exitDuel.getSmoothedPercentage();
			float hover = this.pauseHover.getSmoothedPercentage();

			new SimpleBatch(GL_TRIANGLES, 3, new float[] { (this.exitBounds.x - (hover * 100f)) * inverseExit, // 0
					this.exitBounds.y * inverseExit, 0f,

					(this.main.getWidth() * exit) + ((this.exitBounds.x + this.exitBounds.width) * inverseExit), // 1
					this.exitBounds.y * inverseExit, 0f,

					(this.main.getWidth() * exit) + ((this.exitBounds.x + this.exitBounds.width + (hover * 100f)) // 2
							* inverseExit), (this.main.getHeight() * exit) + ((this.exitBounds.y + this.exitBounds.height) * inverseExit), 0f,

					(this.exitBounds.x - (hover * 100f)) * inverseExit, // 0
					this.exitBounds.y * inverseExit, 0f,

					(this.main.getWidth() * exit) + ((this.exitBounds.x + this.exitBounds.width + (hover * 100f)) // 2
							* inverseExit), (this.main.getHeight() * exit) + ((this.exitBounds.y + this.exitBounds.height) * inverseExit), 0f,

					this.exitBounds.x * inverseExit, // 3
					(this.main.getHeight() * exit) + ((this.exitBounds.y + this.exitBounds.height) * inverseExit), 0f }, null, null, null, null)
					.draw(Shaders.current.getAttributeLocations());

			Shaders.use("font");
			Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f);
			Fonts.centuryGothic.setSize(40 + (this.pauseHover.getSmoothedPercentage() * 10));
			Fonts.centuryGothic.drawCentered("EXIT", 0, 0);
		}

		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
	}

	private static void renderChatBackground(Dimension d, int x, int y, float alpha) {
		Color.set(Shaders.current, GUI_COL[0], GUI_COL[1], GUI_COL[2], GUI_COL[3] * alpha);

		new SimpleBatch(GL_TRIANGLES, 3, new float[] { x - PADDING, y - PADDING, 0f, d.width + x + PADDING, y - PADDING, 0f, d.width + x + PADDING,
				d.height + y + PADDING, 0f,

				x - PADDING, y - PADDING, 0f, d.width + x + PADDING, d.height + y + PADDING, 0f, x - PADDING, d.height + y + PADDING, 0f }, null, null,
				new float[] { 0f, 0f, 1f, 0f, 1f, 1f,

				0f, 0f, 1f, 1f, 0f, 1f }, null).draw(Shaders.current.getAttributeLocations());
	}

	public void recalculatePositions() {
		this.exitBounds = new Rectangle((this.main.getWidth() - 250) / 2, (this.main.getHeight() - 150) / 2, 250, 150);
	}

	public void addMessage(Message message) {
		this.messages.add(message);
		this.chatFade.empty();
		this.messageOffset = 0;
	}

	public void addMessage(String message) {
		this.addMessage(new Message(message));
	}
}
