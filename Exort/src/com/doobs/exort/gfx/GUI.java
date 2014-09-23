package com.doobs.exort.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.lwjgl.input.*;

import com.doobs.exort.*;
import com.doobs.exort.util.*;
import com.doobs.exort.util.loaders.*;
import com.doobs.modern.util.Color;
import com.doobs.modern.util.batch.*;
import com.doobs.modern.util.matrix.*;

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
	private Rectangle exitBounds;

	public GUI() {
		messages = new ArrayList<Message>();
		chatFade = new Animation(120);
		messageOffset = 0;
		pauseHover = new Animation(8);
		exitDuel = new Animation(15);
		exiting = false;
		exitBounds = new Rectangle((Main.getWidth() - 250) / 2, (Main.getHeight() - 150) / 2, 250, 150);
	}

	public void tick(boolean paused, int delta) {
		chatFade.tickUp(delta);

		if (Main.input.isKeyPressed(Keyboard.KEY_UP) && messageOffset < messages.size() - VISIBLE_MESSAGES) {
			chatFade.empty();
			messageOffset++;
		} else if (Main.input.isKeyPressed(Keyboard.KEY_DOWN) && messageOffset != 0) {
			chatFade.empty();
			messageOffset--;
		}

		if (paused && exitBounds.intersects(Mouse.getX(), Mouse.getY(), 1, 1)) {
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
			chatFade.empty();

			// Draw background
			Shaders.use("gui");
			glActiveTexture(GL_TEXTURE0);
			Textures.get("white").bind();
			Dimension d = Fonts.centuryGothic.getPhraseDimensions(text);
			renderChatBackground(d, 10, 10, 1f);
			Shaders.use("font");
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
					Shaders.use("gui");
					glActiveTexture(GL_TEXTURE0);
					Textures.get("white").bind();
					renderChatBackground(d, 10, y, alpha);

					Shaders.use("font");
					message.draw(10, y);

					y += d.height + PADDING * 2;
				}
			}
		}

		if (paused) {
			Shaders.use("gui");
			glActiveTexture(GL_TEXTURE0);
			Textures.get("white").bind();
			Color.set(Shaders.current, GUI_COL[0], GUI_COL[1], GUI_COL[2], GUI_COL[3] + exitDuel.getSmoothedPercentage() * (1 - GUI_COL[3]));
			
			// Percentages
			float exit = exitDuel.getSmoothedPercentage();
			float inverseExit = 1 - exitDuel.getSmoothedPercentage();
			float hover = pauseHover.getSmoothedPercentage();
			
			new SimpleBatch(GL_TRIANGLES, 3, new float[] {
					(exitBounds.x - hover * 100f) * inverseExit, // 0
					exitBounds.y * inverseExit, 0f,
			
					Main.getWidth() * exit + (exitBounds.x + exitBounds.width) * inverseExit, // 1
					exitBounds.y * inverseExit, 0f,
			
					Main.getWidth() * exit + (exitBounds.x + exitBounds.width + hover * 100f) // 2
							* inverseExit,
					(Main.getHeight() * exit) + (exitBounds.y + exitBounds.height) * inverseExit, 0f,
			
					(exitBounds.x - hover * 100f) * inverseExit, // 0
					exitBounds.y * inverseExit, 0f,
					
					Main.getWidth() * exit + (exitBounds.x + exitBounds.width + hover * 100f) // 2
						* inverseExit,
					(Main.getHeight() * exit) + (exitBounds.y + exitBounds.height) * inverseExit, 0f,
	
					exitBounds.x * inverseExit, // 3
					(Main.getHeight() * exit) + (exitBounds.y + exitBounds.height) * inverseExit, 0f
			}, null, null, null, null).draw(Shaders.current.getAttributeLocations());
			
			Shaders.use("font");
			Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f);
			Fonts.centuryGothic.setSize(40 + pauseHover.getSmoothedPercentage() * 10);
			Fonts.centuryGothic.drawCentered("EXIT", 0, 0);
		}

		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
	}

	private static void renderChatBackground(Dimension d, int x, int y, float alpha) {
		Color.set(Shaders.current, GUI_COL[0], GUI_COL[1], GUI_COL[2], GUI_COL[3] * alpha);
		
		new SimpleBatch(GL_TRIANGLES, 3, new float[] {
				x - PADDING, y - PADDING, 0f, 
				d.width + x + PADDING, y - PADDING, 0f, 
				d.width + x + PADDING, d.height + y + PADDING, 0f,
				
				x - PADDING, y - PADDING, 0f,
				d.width + x + PADDING, d.height + y + PADDING, 0f,
				x - PADDING, d.height + y + PADDING, 0f
		}, null, null, new float[] {
				0f, 0f,
				1f, 0f,
				1f, 1f,
				
				0f, 0f,
				1f, 1f,
				0f, 1f
		}, null).draw(Shaders.current.getAttributeLocations());
	}

	public void recalculatePositions() {
		exitBounds = new Rectangle((Main.getWidth() - 250) / 2, (Main.getHeight() - 150) / 2, 250, 150);
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
