package cn.mb.util;
import java.util.List;

import com.nineoldandroids.animation.ValueAnimator;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.view.WindowManager;

public class Cameras {
	private static final double ASPECT_TOLERANCE = 0.15d;

    public static Size getBestPreviewSize(List<Size> sizes, int layoutWidth, int layoutHeight, boolean isPortrait) {
        if (isPortrait) {
            layoutHeight += layoutWidth;
            layoutWidth = layoutHeight - layoutWidth;
            layoutHeight -= layoutWidth;
        }
        double targetRatio = ((double) layoutWidth) / ((double) layoutHeight);
        Size optimalSize = null;
        double optimalArea = 0.0d;
        for (Size candidateSize : sizes) {
            double candidateArea = (double) (candidateSize.width * candidateSize.height);
            if (Math.abs((((double) candidateSize.width) / ((double) candidateSize.height)) - targetRatio) < ASPECT_TOLERANCE && candidateArea > optimalArea) {
                optimalSize = candidateSize;
                optimalArea = candidateArea;
            }
        }
        if (optimalSize == null) {
            optimalArea = 0.0d;
            for (Size candidateSize2 : sizes) {
            double    candidateArea = (double) (candidateSize2.width * candidateSize2.height);
                if (candidateArea > optimalArea) {
                    optimalSize = candidateSize2;
                    optimalArea = candidateArea;
                }
            }
        }
        return optimalSize;
    }

    public static int[] getProportionalDimension(Size size, int targetW, int targetH, boolean isPortrait) {
        double previewRatio;
        int[] adaptedDimension = new int[2];
        if (isPortrait) {
            previewRatio = ((double) size.height) / ((double) size.width);
        } else {
            previewRatio = ((double) size.width) / ((double) size.height);
        }
        if (((double) targetW) / ((double) targetH) > previewRatio) {
            adaptedDimension[0] = targetW;
            adaptedDimension[1] = (int) (((double) adaptedDimension[0]) / previewRatio);
        } else {
            adaptedDimension[1] = targetH;
            adaptedDimension[0] = (int) (((double) adaptedDimension[1]) * previewRatio);
        }
        return adaptedDimension;
    }

    public static void setCameraDisplayOrientation(Context context, Camera camera) {
        int displayOrientation;
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(0, info);
        int degrees = 0;
        switch (((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation()) {
            case 0:// SpinnerCompat.MODE_DIALOG /*0*/:
                degrees = 0;
                break;
            case 1://SpinnerCompat.MODE_DROPDOWN /*1*/:
                degrees = 90;
                break;
            case ValueAnimator.REVERSE /*2*/:
                degrees = 180;
                break;
            case WearableExtender.SIZE_MEDIUM /*3*/:
                degrees = 270;
                break;
        }
        if (info.facing == 1) {
            displayOrientation = (360 - ((info.orientation + degrees) % 360)) % 360;
        } else {
            displayOrientation = ((info.orientation - degrees) + 360) % 360;
        }
        camera.setDisplayOrientation(displayOrientation);
    }

    private Cameras() {
    }

}
