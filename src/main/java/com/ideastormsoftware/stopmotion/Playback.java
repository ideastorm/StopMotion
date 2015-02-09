package com.ideastormsoftware.stopmotion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * @author Phillip
 */
public class Playback extends Thread implements ImageSource {

    private final File folder;
    private BufferedImage currentImage;
    private int lastImage = -1;

    public Playback(File folder) {
        this.folder = folder;
        currentImage = new BufferedImage(2, 2, BufferedImage.TYPE_3BYTE_BGR);
    }

    @Override
    public BufferedImage getCurrentImage() {
        return currentImage;
    }

    private void delay(long nanos) {
        long millis = nanos / 1_000_000;
        int nanoDelay = (int) (nanos % 1_000_000);
        try {
            Thread.sleep(millis, nanoDelay);
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public void run() {
        while (!interrupted()) {
            loop();
        }
    }

    private void loop() {
        long nanoTime = System.nanoTime();
        lastImage++;
        String filename = String.format("%s%s%06d.png", folder.getAbsolutePath(), File.separator, lastImage);
        File sourceFile = new File(filename);
        if (!sourceFile.isFile()) {
            lastImage = -1;
        } else {
            try {
                currentImage = ImageIO.read(sourceFile);
            } catch (IOException ex) {
            }
        }
        long target = 1_000_000_000 / 15;
        long remaining = target - System.nanoTime() + nanoTime;
        if (remaining > 0)
            delay(remaining);
    }

}
