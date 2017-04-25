import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//图片合成工具类，用于将n个头像合并成一个头像
public class ImageCombine {


    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\Macbook\\Desktop\\img\\");//图片文件目录
        List<String> path = new LinkedList<String>();
        for(File f : file.listFiles()){ //为了效果最好，图片数量最好是平方数
            path.add(f.getAbsolutePath());
        }
        combineImages(path);
    }
    public static void combineImages(List<String> paths)
            throws IOException {
        List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();

        int imgNumber = paths.size(); // 头像数量,为了效果最好，图片数量最好是平方数

        int PicHeightAndWidth = 1600; // 画板的宽高

        int gap = 5;//头像之间缝隙的宽度

        int numPerEdge = (int)Math.sqrt(imgNumber);//每条边头像的数量

        int heightOrWidth = (PicHeightAndWidth - gap * (numPerEdge + 1)) / numPerEdge;// 单个头像的长和宽

        for (int i = 0; i < paths.size(); i++) {
            bufferedImages.add(ImageCombine.resize(paths.get(i), heightOrWidth, heightOrWidth, true));
        }

        BufferedImage outImage = new BufferedImage(PicHeightAndWidth, PicHeightAndWidth,
                BufferedImage.TYPE_INT_RGB);

        Graphics g = outImage.getGraphics();// 生成画布

        Graphics2D g2d = (Graphics2D) g;

        g2d.setBackground(new Color(255,255,255)); // 设置背景色

        g2d.clearRect(0, 0, PicHeightAndWidth, PicHeightAndWidth);// 通过使用当前绘图表面的背景色进行填充来清除指定的矩形。

        //逐一绘制头像
        for(int i = 0; i < numPerEdge; i++){
            for(int j = 0; j < numPerEdge; j++){
                g2d.drawImage(bufferedImages.get(i * 10 + j),gap * (j+1) + heightOrWidth * j, gap * (i+1) +heightOrWidth * i, null);
            }
        }

        String outPath = "C:\\Users\\Macbook\\Desktop\\combine.jpg";

        String format = "JPG";

        ImageIO.write(outImage, format, new File(outPath));
    }

    /**
     * 图片缩放
     * @param filePath 图片路径
     * @param height 高度
     * @param width 宽度
     * @param bb 比例不对时是否需要补白
     */
    public static BufferedImage resize(String filePath, int height, int width,
                                        boolean bb) {
        try {
            double ratio = 0; // 缩放比例
            File f = new File(filePath);
            BufferedImage bi = ImageIO.read(f);
            Image itemp = bi.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            // 计算比例
            if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
                if (bi.getHeight() > bi.getWidth()) {
                    ratio = (new Integer(height)).doubleValue()
                            / bi.getHeight();
                } else {
                    ratio = (new Integer(width)).doubleValue() / bi.getWidth();
                }
                AffineTransformOp op = new AffineTransformOp(
                        AffineTransform.getScaleInstance(ratio, ratio), null);
                itemp = op.filter(bi, null);
            }
            if (bb) {
                BufferedImage image = new BufferedImage(width, height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D g = image.createGraphics();
                g.setColor(Color.white);
                g.fillRect(0, 0, width, height);
                if (width == itemp.getWidth(null))
                    g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                else
                    g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
                            itemp.getWidth(null), itemp.getHeight(null),
                            Color.white, null);
                g.dispose();
                itemp = image;
            }
            return (BufferedImage) itemp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}