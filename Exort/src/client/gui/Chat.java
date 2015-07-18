package client.gui;

import static client.gui.GUI.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.lwjgl.input.*;

import client.state.*;
import client.util.*;
import client.util.loaders.*;

import com.doobs.modern.util.Color;
import com.doobs.modern.util.batch.*;

/**
 * GUI component that handles chat behavior and visuals for the DuelState.
 */
public class Chat {
	private static final int PADDING = 5;
	private static final int CHAT_CHAR_LIMIT = 255;
	private static final int VISIBLE_MESSAGES = 5;

	// For sending messages to the Server.
	private DuelState state;

	private InputHandler input;

	private List<Message> messageHistory;

	// Current message being typed.
	private StringBuffer message;

	private boolean typing;

	// For smooth fading of the Chat.
	public Animation chatFade;

	// For scrolling through the Chat.
	private int messageOffset;

	/**
	 * Creates a Chat component using "input" and using "state" as an interface for
	 * sending messages to the client.
	 */
	public Chat(DuelState state, InputHandler input) {
		this.state = state;
		this.input = input;
		this.messageHistory = new ArrayList<Message>();
		this.message = new StringBuffer();
		this.typing = false;
		this.chatFade = new Animation(120);
		this.messageOffset = 0;
	}

	/**
	 * Handles the behavior of this Chat.
	 */
	public void tick(int delta) {
		if (this.typing) {
			// Message scrolling.
			if (this.input.isKeyPressed(Keyboard.KEY_UP) && (this.messageOffset < (this.messageHistory.size() - VISIBLE_MESSAGES))) {
				this.messageOffset++;
			} else if (this.input.isKeyPressed(Keyboard.KEY_DOWN) && (this.messageOffset != 0)) {
				this.messageOffset--;
			}

			this.input.handleTyping(this.message, Fonts.centuryGothic);
			// Keep message under CHAT_CHAR_LIMIT.
			if (this.message.length() > CHAT_CHAR_LIMIT) {
				this.message.deleteCharAt(this.message.length() - 1);
			}

		} else { // Fade when not typing.
			this.chatFade.tickUp(delta);
		}

		// Toggle chat box.
		if (this.input.isKeyPressed(Keyboard.KEY_RETURN)) {
			this.typing = !this.typing;
			this.chatFade.empty();
			if (this.message.length() != 0) {
				this.state.sendMessage(this.message.toString());
				this.message = new StringBuffer();
			}
		}
	}

	/**
	 * Pre: OpenGL is in a state configured for GUI rendering.
	 *
	 * Renders this Chat.
	 */
	public void render() {
		if (this.typing) {
			// Draw background
			Shaders.use("gui");
			glActiveTexture(GL_TEXTURE0);
			Textures.get("white").bind();
			Dimension d = Fonts.centuryGothic.getPhraseDimensions(this.message);
			renderChatBackground(d, 10, 10, 1f);
			Shaders.use("font");
			Fonts.centuryGothic.draw(this.message, 10, 10);
		}

		if (!this.chatFade.isFull()) {
			float alpha = Math.min(1f, 1f - ((this.chatFade.getPercentage() - 0.8f) * 5f));
			Dimension d;
			int y = 10 + Fonts.centuryGothic.getPhraseHeight(this.message) + (PADDING * 2);
			for (int i = 0; i < VISIBLE_MESSAGES; i++) {
				if (i < this.messageHistory.size()) {
					Message message = this.messageHistory.get(this.messageHistory.size() - i - this.messageOffset - 1);
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
	}

	/**
	 * Pre: OpenGL is in a state configured for GUI rendering.
	 *
	 * Renders the background of size "d" at ("x", "y") with opacity of "alpha"
	 * (multiplied by the original alpha value).
	 */
	private static void renderChatBackground(Dimension d, int x, int y, float alpha) {
		Color.set(Shaders.current, GUI_COL[0], GUI_COL[1], GUI_COL[2], GUI_COL[3] * alpha);

		new SimpleBatch(GL_TRIANGLES, 3, new float[] { x - PADDING, y - PADDING, 0f, d.width + x + PADDING, y - PADDING, 0f, d.width + x + PADDING,
				d.height + y + PADDING, 0f,

				x - PADDING, y - PADDING, 0f, d.width + x + PADDING, d.height + y + PADDING, 0f, x - PADDING, d.height + y + PADDING, 0f }, null, null,
				new float[] { 0f, 0f, 1f, 0f, 1f, 1f,

				0f, 0f, 1f, 1f, 0f, 1f }, null).draw(Shaders.current.getAttributeLocations());
	}

	/**
	 * Adds "message" to the Chat history.
	 */
	public void addMessage(Message message) {
		this.messageHistory.add(message);
		this.chatFade.empty();
		this.messageOffset = 0;
	}

	/**
	 * Adds "message" to the Chat history.
	 */
	public void addMessage(String message) {
		this.addMessage(new Message(message));
	}

	/**
	 * Makes this Chat fully visible.
	 */
	public void show() {
		this.chatFade.empty();
	}

	/**
	 * Makes this Chat vanish.
	 */
	public void vanish() {
		this.chatFade.fill();
	}

	/**
	 * Returns true if the Player is typing.
	 */
	public boolean isTyping() {
		return this.typing;
	}
}