package com.nickan.framework;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * I just copied the concept of the Animation class created by Mr.Zechner, but I just changed the animation duration to
 * be changeable. Added wrapper to ease up the creation of the animation a bit.
 * @author Nickan
 *
 */
public class Animation {
	public int currentFrame;
	private int totalFrames;

	public float animationDuration;
	float currentStateTime;
	public int frameNumber = 0;
	public float frameDuration;
	boolean finished = false;

	public enum PlayMode { NORMAL, LOOP };
	PlayMode playMode;

	TextureRegion region[];
	
	// New implementation
	public Animation(Texture texture, int srcX, int srcY, int width, int height,
			int columns, int totalFrames, float animationDuration) {
		this.totalFrames = totalFrames;
		this.animationDuration = animationDuration;
		this.frameDuration = animationDuration / totalFrames;
		region = new TextureRegion[totalFrames];
		for (int i = 0; i < region.length; ++i) {
			int frameX = (i % columns) * width;
			int frameY = (i / columns) * height;
			region[i] = new TextureRegion(texture, srcX + frameX, srcY + frameY, width, height);
		}
		this.playMode = PlayMode.NORMAL;
	}

	public Animation(Texture texture, int srcX, int srcY, int width, int height,
			int columns, int totalFrames, float animationDuration, PlayMode playMode) {
		this.totalFrames = totalFrames;
		this.animationDuration = animationDuration;
		this.frameDuration = animationDuration / totalFrames;
		region = new TextureRegion[totalFrames];
		for (int i = 0; i < region.length; ++i) {
			int frameX = (i % columns) * width;
			int frameY = (i / columns) * height;
			region[i] = new TextureRegion(texture, srcX + frameX, srcY + frameY, width, height);
		}
		this.playMode = playMode;
	}

	public Animation(TextureRegion[] regions, int totalFrames, float animationDuration, PlayMode playMode) {
		this.region = regions;
		this.totalFrames = totalFrames;
		this.animationDuration = animationDuration;
		this.frameDuration = animationDuration / totalFrames;
		this.playMode = playMode;
	}

	public Animation(TextureRegion[] regions, int totalFrames, float animationDuration) {
		this.region = regions;
		this.totalFrames = totalFrames;
		this.animationDuration = animationDuration;
		this.frameDuration = animationDuration / totalFrames;
		this.playMode = PlayMode.NORMAL;
	}

	public TextureRegion[] getRegions() {
		return region;
	}

	public void setAnimationDuration(float animationDuration) {
		this.animationDuration = animationDuration;
		this.frameDuration = animationDuration / totalFrames;
	}

	public TextureRegion getKeyFrame(float stateTime) {
		currentStateTime = stateTime % animationDuration;
		switch (playMode) {
		case NORMAL:
			if (stateTime < animationDuration) {
				frameNumber = (int) (currentStateTime / frameDuration);
			} else {
				frameNumber = totalFrames - 1;
			}
			break;
		case LOOP:
			frameNumber = (int) (currentStateTime / frameDuration);

			break;
		default:
			break;
		}

		return region[frameNumber];
	}

	public boolean isFinished(float stateTime) {
		return (region.length - 1 == frameNumber ? true: false);
	}

	public void setPlayMode(PlayMode playMode) {
		this.playMode = playMode;
	}


}

