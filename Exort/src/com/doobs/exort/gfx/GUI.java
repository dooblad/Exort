package com.doobs.exort.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.lwjgl.input.*;

import com.doobs.exort.*;
import com.doobs.exort.util.*;

import res.shaders.*;
import res.textures.*;
import res.textures.fonts.*;

public class GUI {
	private static final int PADDING = 5;
	private static final float[] GUI_COL = { 0f, 0f, 0f, 0.5f };
	private static final int VISIBLE_MESSAGES = 5;
	
	private List<Message> messages;
	public Animation chatFade;
	private int messageOffset;

	public Animation exitDuel;
	public Animation pauseHover;
	private boolean exiting;
	private Rectangle exit;

	public GUI() {
		messages = new ArrayList<Message>();
		chatFade = new Animation(120);
		messageOffset = 0;
		pauseHover = new Animation(8);
		exitDuel = new Animation(15);
		exiting = false;
		exit = new Rectangle((Main.width - 250) / 2, (Main.height - 150) / 2, 250, 150);
	}

	public void tick(boolean paused, int delta) {
		chatFade.tickUp(delta);

		if(Main.input.isKeyPressed(Keyboard.KEY_UP) && messageOffset < messages.size() - VISIBLE_MESSAGES) {
			chatFade.empty();
			messageOffset++;
		}
		else if(Main.input.isKeyPressed(Keyboard.KEY_DOWN) && messageOffset != 0) {
			chatFade.empty();
			messageOffset--;
		}
		
		if (paused && exit.intersects(Mouse.getX(), Mouse.getY(), 1, 1)) {
			pauseHover.tickUp(delta);

			if (Main.input.isMouseButtonPressed(0)) {
				exiting = true;
				chatFade.fill();
			}
		} else
			pauseHover.tickDown(delta);

		if (exiting) {
			pauseHover.fill();
			exitDuel.tickUp(delta);
		}
	}

	public void render(String text, boolean paused, boolean typing) {
		Fonts.centuryGothic.setSize(10);
		Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f);

		glEnable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);

		if (typing) {
			chatFade.empty();

			// Draw background
			Shaders.gui.use();
			glActiveTexture(GL_TEXTURE0);
			Textures.getTexture("white").bind();
			Dimension d = Fonts.centuryGothic.getPhraseDimensions(text);
			renderChatBackground(text, d, 10, 10, 1f);
			Shaders.font.use();
			Fonts.centuryGothic.draw(text, 10, 10);
		}

		if (!chatFade.isFull()) {
			float alpha = Math.min(1f, 1f - (chatFade.getPercentage() - 0.8f) * 5f);
			Dimension d;
			int y = 10 + Fonts.centuryGothic.getPhraseHeight(text) + PADDING * 2;
			for (int i = 0; i < VISIBLE_MESSAGES; i++) {
				if (i < messages.size()) {
					Message message = messages.get(messages.size() - i - messageOffset - 1);
					d = Fonts.centuryGothic.getPhraseDimensions(message.getText());

					// Draw chat background
					Shaders.gui.use();
					glActiveTexture(GL_TEXTURE0);
					Textures.getTexture("white").bind();
					renderChatBackground(message.getText(), d, 10, y, alpha);

					Shaders.font.use();
					message.draw(10, y);

					y += d.height + PADDING * 2;
				}
			}
		}

		if (paused) {
			Shaders.gui.use();
			glActiveTexture(GL_TEXTURE0);
			Textures.getTexture("white").bind();
			glColor4f(GUI_COL[0], GUI_COL[1], GUI_COL[2], GUI_COL[3] + exitDuel.getSmoothedPercentage() * (1 - GUI_COL[3]));
			glBegin(GL_QUADS);
			glVertex2f((exit.x - pauseHover.getSmoothedPercentage() * 100f) * (1 - exitDuel.getSmoothedPercentage()),
					exit.y * (1 - exitDuel.getSmoothedPercentage()));
			glVertex2f(Main.width * exitDuel.getSmoothedPercentage() + (exit.x + exit.width) * (1 - exitDuel.getSmoothedPercentage()),
					exit.y * (1 - exitDuel.getSmoothedPercentage()));
			glVertex2f(
					Main.width * exitDuel.getSmoothedPercentage() + (exit.x + exit.width + pauseHover.getSmoothedPercentage() * 100f)
							* (1 - exitDuel.getSmoothedPercentage()),
					(Main.height * exitDuel.getSmoothedPercentage()) + (exit.y + exit.height) * (1 - exitDuel.getSmoothedPercentage()));
			glVertex2f(exit.x * (1 - exitDuel.getSmoothedPercentage()), (Main.height * exitDuel.getSmoothedPercentage()) + (exit.y + exit.height) * (1 - exitDuel.getSmoothedPercentage()));
			glEnd();
			Shaders.font.use();
			Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f);
			Fonts.centuryGothic.setSize(40 + pauseHover.getSmoothedPercentage() * 10);
			Fonts.centuryGothic.drawCentered("EXIT", 0, 0);
		}

		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
	}

	private static void renderChatBackground(String text, Dimension d, int x, int y, float alpha) {
		glColor4f(GUI_COL[0], GUI_COL[1], GUI_COL[2], GUI_COL[3] * alpha);
		glBegin(GL_QUADS);
		glVertex2f(x - PADDING, y - PADDING);
		glVertex2f(d.width + x + PADDING, y - PADDING);
		glVertex2f(d.width + x + PADDING, d.height + y + PADDING);
		glVertex2f(x - PADDING, d.height + y + PADDING);
		glEnd();
	}

	public void recalculatePositions() {
		exit = new Rectangle((Main.width - 250) / 2, (Main.height - 150) / 2, 250, 150);
	}
	
	public void addMessage(Message message) {
		messages.add(message);
		chatFade.empty();
		messageOffset = 0;
	}
	
	public void addMessage(String message) {
		addMessage(new Message(message));
	}
}
