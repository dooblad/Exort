package com.doobs.exort.gfx;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import com.doobs.exort.*;

import res.shaders.*;
import res.textures.*;

public class GUI {
	
	public static void tick() {
		
	}
	
	public static void render() {
		glColor4f(1f, 1f, 1f, 1f);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, Textures.abilityHUD.getID());
		glUniform1i(glGetUniformLocation(Shaders.gui.getID(), "texture"), 0);
		
		glEnable(GL_BLEND);
		
		glOrtho(0, Main.width, 0, Main.height, 1, -1);
		
		float width = Textures.abilityHUD.getWidth();
		float height = Textures.abilityHUD.getHeight();
		
		float x = (Main.width - width) / 2;
		
		glBegin(GL_QUADS);
		glTexCoord2f(0f, 0f);
		glVertex2f(x, 0f);
		
		glTexCoord2f(1f, 0f);
		glVertex2f(x + width, 0f);
		
		glTexCoord2f(1f, 1f);
		glVertex2f(x + width, height);
		
		glTexCoord2f(0f, 1f);
		glVertex2f(x, height);
		glEnd();
		
		glDisable(GL_BLEND);
		
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}	
