package com.cloud.util;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.io.FilenameUtils.getExtension;

/**
 * Created by micky on 2016. 12. 12..
 */
public class ImageUtil {
    public static final int RATIO = 0;
    public static final int SAME = -1;

    private static ConcurrentHashMap<File, String> thumbnailCache = new ConcurrentHashMap<>();

    public static String createThumbnail(File src, int max) throws IOException {
        if(thumbnailCache.containsKey(src)) return thumbnailCache.get(src);

        Image srcImg = getImageFromFile(src);
        String thumbnail = null;
        if(srcImg.getWidth(null) > srcImg.getHeight(null))
            thumbnail = resize(srcImg, max, RATIO);
        else
            thumbnail = resize(srcImg, RATIO, max);

        thumbnailCache.put(src, thumbnail);

        return thumbnail;
    }

    private static String resize(Image srcImg, int width, int height) throws IOException {
        int srcWidth = srcImg.getWidth(null);
        int srcHeight = srcImg.getHeight(null);

        int destWidth = -1, destHeight = -1;

        if (width == SAME) {
            destWidth = srcWidth;
        } else if (width > 0) {
            destWidth = width;
        }

        if (height == SAME) {
            destHeight = srcHeight;
        } else if (height > 0) {
            destHeight = height;
        }

        if (width == RATIO && height == RATIO) {
            destWidth = srcWidth;
            destHeight = srcHeight;
        } else if (width == RATIO) {
            double ratio = ((double)destHeight) / ((double)srcHeight);
            destWidth = (int)((double)srcWidth * ratio);
        } else if (height == RATIO) {
            double ratio = ((double)destWidth) / ((double)srcWidth);
            destHeight = (int)((double)srcHeight * ratio);
        }

        Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
        int pixels[] = new int[destWidth * destHeight];
        PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new IOException(e.getMessage());
        }
        BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
        destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Base64OutputStream base64os = new Base64OutputStream(baos, true, 0, null);

        ImageIO.write(destImg, "jpg", base64os);

        return baos.toString("UTF-8");
    }

    private static Image getImageFromFile(File src) throws IOException {
        Image srcImg;
        String suffix = src.getName().substring(src.getName().lastIndexOf('.')+1).toLowerCase();
        if (suffix.equals("bmp") || suffix.equals("png") || suffix.equals("gif")) {
            srcImg = ImageIO.read(src);
        } else {
            // BMP가 아닌 경우 ImageIcon을 활용해서 Image 생성
            // 이렇게 하는 이유는 getScaledInstance를 통해 구한 이미지를
            // PixelGrabber.grabPixels로 리사이즈 할때
            // 빠르게 처리하기 위함이다.
            srcImg = new ImageIcon(src.getCanonicalPath()).getImage();
        }
        return srcImg;
    }

    public static boolean isImageFile(File f) {
        String ext = FilenameUtils.getExtension(f.getName());
        //System.out.println(ext);
        final String[] imgExts  = new String[]{"png", "jpg", "jpeg", "tiff", "bmp"};
        for (String imgExt : imgExts) {
            if(imgExt.equals(ext.toLowerCase())) return true;
        }

        return false;
    }
}
