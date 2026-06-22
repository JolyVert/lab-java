package service;


import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageService {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void shutdown() {
        executor.shutdown();
    }

    public Image rotate(Image srcImage, boolean clockwise) {
        int w = (int) srcImage.getWidth();
        int h = (int) srcImage.getHeight();
        int[] srcPixels = new int[w * h];
        srcImage.getPixelReader().getPixels(0, 0, w, h, PixelFormat.getIntArgbInstance(), srcPixels, 0, w);
        int[] destPixels = new int[w * h];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int nx = clockwise ? (h - 1 - y) : y;
                int ny = clockwise ? x : (w - 1 - x);
                destPixels[ny * h + nx] = srcPixels[y * w + x];
            }
        }

        WritableImage rotated = new WritableImage(h, w);
        rotated.getPixelWriter().setPixels(0, 0, h, w, PixelFormat.getIntArgbInstance(), destPixels, 0, h);
        return rotated;
    }

    public Image scale(Image srcImage, int targetW, int targetH) {
        int w1 = (int) srcImage.getWidth();
        int h1 = (int) srcImage.getHeight();
        int[] srcPixels = new int[w1 * h1];
        srcImage.getPixelReader().getPixels(0, 0, w1, h1, PixelFormat.getIntArgbInstance(), srcPixels, 0, w1);
        int[] destPixels = new int[targetW * targetH];

        for (int y = 0; y < targetH; y++) {
            for (int x = 0; x < targetW; x++) {
                int srcX = Math.min(x * w1 / targetW, w1 - 1);
                int srcY = Math.min(y * h1 / targetH, h1 - 1);
                destPixels[y * targetW + x] = srcPixels[srcY * w1 + srcX];
            }
        }

        WritableImage scaled = new WritableImage(targetW, targetH);
        scaled.getPixelWriter().setPixels(0, 0, targetW, targetH, PixelFormat.getIntArgbInstance(), destPixels, 0, targetW);
        return scaled;
    }

    public Image applyParallelOperation(Image srcImage, String opType, Object param) throws Exception {
        int width = (int) srcImage.getWidth();
        int height = (int) srcImage.getHeight();

        int[] srcPixels = new int[width * height];
        int[] destPixels = new int[width * height];
        srcImage.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), srcPixels, 0, width);

        int chunks = 4;
        int rowsPerChunk = height / chunks;
        List<Callable<Void>> tasks = new ArrayList<>();

        for (int i = 0; i < chunks; i++) {
            final int startY = i * rowsPerChunk;
            final int endY = (i == chunks - 1) ? height : (i + 1) * rowsPerChunk;

            tasks.add(() -> {
                for (int y = startY; y < endY; y++) {
                    for (int x = 0; x < width; x++) {
                        int idx = y * width + x;

                        if ("Negatyw".equals(opType)) {
                            int argb = srcPixels[idx];
                            int a = (argb >> 24) & 0xff;
                            int r = 255 - ((argb >> 16) & 0xff);
                            int g = 255 - ((argb >> 8) & 0xff);
                            int b = 255 - (argb & 0xff);
                            destPixels[idx] = (a << 24) | (r << 16) | (g << 8) | b;
                        }
                        else if ("Progowanie".equals(opType)) {
                            int threshold = (int) param;
                            int argb = srcPixels[idx];
                            int a = (argb >> 24) & 0xff;
                            int r = (argb >> 16) & 0xff;
                            int g = (argb >> 8) & 0xff;
                            int b = argb & 0xff;
                            int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                            int val = (gray >= threshold) ? 255 : 0;
                            destPixels[idx] = (a << 24) | (val << 16) | (val << 8) | val;
                        }
                        else if ("Konturowanie".equals(opType)) {
                            if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                                destPixels[idx] = 0xFF000000;
                                continue;
                            }
                            int[][] val = new int[3][3];
                            for (int dy = -1; dy <= 1; dy++) {
                                for (int dx = -1; dx <= 1; dx++) {
                                    int p = srcPixels[(y + dy) * width + (x + dx)];
                                    int r = (p >> 16) & 0xff;
                                    int g = (p >> 8) & 0xff;
                                    int b = p & 0xff;
                                    val[dy + 1][dx + 1] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                                }
                            }
                            int gx = -val[0][0] + val[0][2] - 2 * val[1][0] + 2 * val[1][2] - val[2][0] + val[2][2];
                            int gy = -val[0][0] - 2 * val[0][1] - val[0][2] + val[2][0] + 2 * val[2][1] + val[2][2];
                            int mag = (int) Math.sqrt(gx * gx + gy * gy);
                            if (mag > 255) mag = 255;
                            destPixels[idx] = 0xFF000000 | (mag << 16) | (mag << 8) | mag;
                        }
                    }
                }
                return null;
            });
        }

        executor.invokeAll(tasks);

        WritableImage outImg = new WritableImage(width, height);
        outImg.getPixelWriter().setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), destPixels, 0, width);
        return outImg;
    }
}
