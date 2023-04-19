package pers.website.common.utils;

import org.springframework.util.Assert;
import pers.website.common.exceptions.CustomException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 图片工具类
 *
 * @author ChenetChen
 * @since 2023/4/19 9:29
 */
public class ImageUtil {
    private static final Integer MASK_DEGREE = 45;
    private static final Integer MASK_OPACITY = 80;
    private static final Integer FONT_SIZE = 50;

    /**
     * 绘制水印
     * @param maskText 水印文字
     * @param srcImgPath 图片源路径
     * @param targetPath 图片输出路径
     * @param imgFormat 输出图片格式
     */
    public static void maskImage(String maskText, String srcImgPath, String targetPath, String imgFormat) {
        maskImage(maskText, srcImgPath, targetPath, FONT_SIZE, MASK_DEGREE, MASK_OPACITY, imgFormat);
    }
    
    /**
     * 绘制水印
     * @param maskText 水印文字
     * @param srcImgPath 图片源路径
     * @param targetPath 图片输出路径
     * @param fontSize 水印字体大小
     * @param degree 水印旋转角度
     * @param opacity 水印不透明度
     * @param imgFormat 输出图片格式
     */
    public static void maskImage(String maskText, String srcImgPath, String targetPath,
                                 Integer fontSize, Integer degree, float opacity, String imgFormat) {
        try {
            BufferedImage bufferedImage = getBufferedImage(srcImgPath);
            Assert.notNull(bufferedImage, "图片对象为空");
            drawTextWaterMask(maskText, fontSize, degree, opacity, bufferedImage);
            try (OutputStream outputStream = Files.newOutputStream(Paths.get(targetPath))) {
                ImageIO.write(bufferedImage, imgFormat, outputStream);
            }
        } catch (IOException e) {
            throw new CustomException("添加水印，IO异常");
        }

    }

    /**
     * 绘制文字水印
     * @param maskText 水印文字
     * @param fontSize 字体大小
     * @param degree 旋转角度
     * @param opacity 不透明度
     * @param bufferedImage 图片对象
     */
    private static void drawTextWaterMask(String maskText, int fontSize, Integer degree, float opacity, BufferedImage bufferedImage) {
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(bufferedImage.getScaledInstance(bufferedImage.getWidth(null), bufferedImage.getHeight(null), Image.SCALE_FAST), 0, 0, null);
        if (null != degree) {
            graphics2D.rotate(Math.toRadians(degree), (double) bufferedImage.getWidth() / 2, (double) bufferedImage.getHeight() / 2);
        }
        graphics2D.setColor(Color.lightGray);
        graphics2D.setFont(new Font(Font.DIALOG, Font.ITALIC, fontSize));
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, opacity));
        int step = maskText.length() * fontSize + 100;
        for (int i = 0; i < bufferedImage.getWidth(null); i+= step) {
            for (int j = 0; j < bufferedImage.getHeight(null); j+= step) {
                graphics2D.drawString(maskText, i, j);
            }
        }
        graphics2D.dispose();
    }

    /**
     * 获取图片对象
     * @param srcImgPath 图片路径
     * @return 图片对象
     * @throws IOException IO异常
     */
    private static BufferedImage getBufferedImage(String srcImgPath) throws IOException {
        return ImageIO.read(new File(srcImgPath));
    }
}
