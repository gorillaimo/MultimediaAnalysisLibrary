/**
 * 
 */
package me.gorillabo.multimediaanalysis;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gorillaimo
 *
 */
public class ImageDetector {
	
	private String lbpCascadePath;
	
	public void setLbpCascadePath(String lbpCascadePath) {
		this.lbpCascadePath = lbpCascadePath;
	}
	
	public String getLbpCascadePath() {
		return lbpCascadePath;
	}
	
	public ImageDetector(String lbpCascadePath) {
		this.lbpCascadePath = lbpCascadePath;
	}
	
	public List<Mat> ditectFromImage(Mat src, int margin) {
		return ditectFromImage(src, margin, lbpCascadePath);
	}
	
	public static List<Mat> ditectFromImage(Mat src, int margin, String lbpCascadePath) {
        Mat dst = src.clone();
        List<Mat> result = new ArrayList<>();

        // 正面顔検出
        CascadeClassifier objDetector = new CascadeClassifier(lbpCascadePath);
        MatOfRect matOfRect = new MatOfRect();
        objDetector.detectMultiScale(dst, matOfRect);
        if(matOfRect.toArray().length <= 0) {
            result.add(src);
            return result;
        }

        System.out.println("Face detection：" + matOfRect.toArray().length);

        // 正面顔抜き出し
        for(Rect rect : matOfRect.toArray()) {
            int x1 = (rect.x - margin) < 0 ? 0 : (rect.x - margin);
            int y1 = (rect.y - margin) < 0 ? 0 : (rect.y - margin);
            int x2 = (rect.x + rect.width + margin) > src.width() ? src.width() : (rect.x + rect.width + margin);
            int y2 = (rect.y + rect.height + margin) > src.height() ? src.height() : (rect.y + rect.height + margin);
            Rect withdrawRect = new Rect(new Point(x1, y1), new Point(x2, y2));
            Mat withdrawMat = new Mat(dst, withdrawRect);
            result.add(withdrawMat);
        }
        return result;
	}
}
