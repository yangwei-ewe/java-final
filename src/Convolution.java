import org.opencv.core.Mat;
import static org.opencv.imgcodecs.Imgcodecs.*;

public class Convolution {
    public static void main(String[] args) {
        long start = System.nanoTime();
        System.load("/usr/local/share/java/opencv4/libopencv_java481.so");
        double[][] convolutionMap = {
                { 1 / 9f, 1 / 9f, 1 / 9f },
                { 1 / 9f, 1 / 9f, 1 / 9f },
                { 1 / 9f, 1 / 9f, 1 / 9f } };

        var origImg = imread("src/IMG_4791.PNG", IMREAD_UNCHANGED);
        if (origImg.empty()) {
            System.err.println("err: img failed.");
        }

        Mat resimg = new Mat(origImg.rows() / 3, origImg.cols() / 3, origImg.type());
        System.out.println(origImg.type() + "\n" + resimg.depth());

        for (int i = 0; i < origImg.rows() / 3; i++) {
            for (int j = 0; j < origImg.cols() / 3; j++) {
                double B = 0, G = 0, R = 0, A = 0;
                for (int m = 0; m < 3; m++) {
                    for (int n = 0; n < 3; n++) {
                        var bgra = origImg.get(i * 3 + m, j * 3 + n);
                        B += bgra[0] * convolutionMap[m][n];
                        G += bgra[1] * convolutionMap[m][n];
                        R += bgra[2] * convolutionMap[m][n];
                        A += bgra[3] * convolutionMap[m][n];
                    }
                }
                resimg.put(i, j, new double[] { B, G, R, A });
            }
        }
        imwrite("src/IMG_4791_SMALL.PNG", resimg);
        long end = System.nanoTime();
        System.out.println("exe time: " + (end - start) * 1e-6 + "ms");// about 313.3115ms | 1786.809ms on raspi
        return;
    }
}
