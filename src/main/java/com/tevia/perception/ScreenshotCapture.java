package com.tevia.perception;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.NativeImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * Captures screenshots for visual perception.
 * This is optional and only used with vision-capable models.
 */
public class ScreenshotCapture {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotCapture.class);

    /**
     * Capture a screenshot and return as base64 encoded string.
     * Returns null if capture fails.
     */
    public String capture(MinecraftClient client) {
        try {
            Framebuffer framebuffer = client.getFramebuffer();

            int width = framebuffer.textureWidth;
            int height = framebuffer.textureHeight;

            // Limit resolution to reduce size (max 512x512)
            int maxSize = 512;
            if (width > maxSize || height > maxSize) {
                float scale = Math.min((float) maxSize / width, (float) maxSize / height);
                width = (int) (width * scale);
                height = (int) (height * scale);
            }

            // Capture the framebuffer
            NativeImage image = takeScreenshot(framebuffer, width, height);

            if (image == null) {
                return null;
            }

            // Convert to base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.writeTo(baos);
            String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());

            image.close();

            LOGGER.debug("Screenshot captured: {}x{}", width, height);

            return base64;

        } catch (Exception e) {
            LOGGER.error("Failed to capture screenshot", e);
            return null;
        }
    }

    /**
     * Take a screenshot from the framebuffer.
     */
    private NativeImage takeScreenshot(Framebuffer framebuffer, int width, int height) {
        try {
            // This is a simplified version - actual implementation would need
            // to properly read from the framebuffer and resize if needed
            return new NativeImage(width, height, false);
        } catch (Exception e) {
            LOGGER.error("Failed to create screenshot image", e);
            return null;
        }
    }
}
