package dev.davelpz.jray.animation;

import dev.davelpz.jray.camera.Camera;
import dev.davelpz.jray.canvas.Canvas;
import dev.davelpz.jray.world.World;

import java.io.File;

public class Animator {
    private final Camera camera;
    private final WorldGenerator worldGenerator;
    private final int frames;
    private final String outputDir;
    private final String outputPrefix;

    public Animator(Camera camera, WorldGenerator worldGenerator, int frames, String outputDir, String outputPrefix) {
        this.camera = camera;
        this.worldGenerator = worldGenerator;
        this.frames = frames;
        this.outputDir = outputDir;
        this.outputPrefix = outputPrefix;
    }

    public void animate() {
        for (int i = 0; i < frames; i++) {
            System.out.println("Rendering frame " + (i+1) + " of " + frames);
            World world = worldGenerator.generateWorld(i);
            Canvas canvas = camera.render(world);
            String frameNumber = String.format("%04d", i);
            canvas.saveToFile(outputDir + File.separator + outputPrefix + frameNumber + ".png");
        }
    }
}
