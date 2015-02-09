package com.ideastormsoftware.stopmotion;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;
import org.opencv.core.Size;

/**
 *
 * @author Phillip
 */
class Preview extends JPanel {

    private ImageSource source;
    private final Timer timer;
    private BufferedImage lastImage;

    public Preview(ImageSource source) throws HeadlessException {
        setBackground(Color.black);
        setSize(320, 240);
        this.source = source;
        this.timer = new Timer(30, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                repaint(1);
            }
        });
        timer.start();
    }

    public void setSource(ImageSource source) {
        this.source = source;
    }

    public void setLastImage(BufferedImage image) {
        this.lastImage = image;
    }

    @Override
    public void paint(Graphics grphcs) {
        int w = getWidth();
        int h = getHeight();
        if (w < 1 || h < 1) {
            return;
        }
        BufferedImage img = source.getCurrentImage();
        if (img != null) {
            Size targetSize = ImageUtils.aspectScaledSize(img.getWidth(), img.getHeight(), w, h);
            Graphics2D g2 = (Graphics2D) grphcs;
            int offsetWidth = (int) (w - targetSize.width) / 2;
            int offsetHeight = (int) (h - targetSize.height) / 2;
            g2.drawImage(img, offsetWidth, offsetHeight, (int) targetSize.width, (int) targetSize.height, this);
            if (lastImage != null) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2.drawImage(lastImage, offsetWidth, offsetHeight, (int) targetSize.width, (int) targetSize.height, this);
            }
        }
    }
}
