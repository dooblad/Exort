package com.doobs.exort.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.doobs.exort.*;

import res.shaders.*;
import res.textures.*;
import res.textures.fonts.*;

public class GUI {
	public static List<String> messages = new ArrayList<String>();

	private static final int padding = 5;
	private static final float[] guiCol = { 0f, 0f, 0f, 0.5f };

	private static final int VISIBLE_MESSAGES = 5;
	private static final int FADE_TIME = 100;
	private static int fadeTimer;
	private static boolean chatHidden = true;

	public static void tick() {
		if (++fadeTimer > FADE_TIME) {
			fadeTimer = FADE_TIME;
			chatHidden = true;
		}
	}

	public static void render(String text, boolean paused, boolean typing) {
		Fonts.finalFrontier.setSize(5);
		Fonts.finalFrontier.setColor(1f, 1f, 1f, 1f);

		glEnable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);

		if (typing) {
			chatHidden = false;

			// Draw background
			Shaders.gui.use();
			glActiveTexture(GL_TEXTURE0);
			Textures.getTexture("white").bind();
			Dimension d = Fonts.finalFrontier.getPhraseDimensions(text);
			renderChatBackground(text, d, 10, 10);
			Shaders.font.use();
			Fonts.finalFrontier.draw(text, 10, 10);
		}

		if (!chatHidden) {
			Dimension d;
			int y = 10 + Fonts.finalFrontier.getPhraseHeight(text) + padding * 2;
			for (int i = 0; i < VISIBLE_MESSAGES; i++) {
				if (i < messages.size()) {
					String message = messages.get(messages.size() - i - 1);
					d = Fonts.finalFrontier.getPhraseDimensions(message);

					// Draw background
					Shaders.gui.use();
					glActiveTexture(GL_TEXTURE0);
					Textures.getTexture("white").bind();
					renderChatBackground(message, d, 10, y);

					Shaders.font.use();
					Fonts.finalFrontier.draw(message, 10, y);
					
					y += d.height + padding * 2;
				}
			}
		}
		
		if(paused) {
			chatHidden = false;
			Shaders.gui.use();
			glActiveTexture(GL_TEXTURE0);
			Textures.getTexture("white").bind();
			glColor4f(guiCol[0], guiCol[1], guiCol[2], guiCol[3]);
			glBegin(GL_QUADS);
			glVertex2f((Main.width - 450f) / 2, (Main.height - 150f) / 2);
			glVertex2f((Main.width + 250f) / 2, (Main.height - 150f) / 2);
			glVertex2f((Main.width + 450f) / 2, (Main.height + 150f) / 2);
			glVertex2f((Main.width - 250f) / 2, (Main.height + 150f) / 2);
			glEnd();
			Shaders.font.use();
			Fonts.centuryGothic.setColor(1f, 1f, 1f, 1f);
			Fonts.centuryGothic.setSize(14);
			Fonts.centuryGothic.drawCentered("EXIT", 0, 0);
		}

		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
	}

	private static void renderChatBackground(String text, Dimension d, int x, int y) {
		glColor4f(guiCol[0], guiCol[1], guiCol[2], guiCol[3]);
		glBegin(GL_QUADS);
		glVertex2f(x - padding, y - padding);
		glVertex2f(d.width + x + padding, y - padding);
		glVertex2f(d.width + x + padding, d.height + y + padding);
		glVertex2f(x - padding, d.height + y + padding);
		glEnd();
	}

	public static void addMessage(String message) {
		messages.add(message);
		fadeTimer = 0;
		chatHidden = false;
	}
}
