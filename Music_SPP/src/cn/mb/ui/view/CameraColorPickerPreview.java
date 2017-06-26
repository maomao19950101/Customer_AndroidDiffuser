package cn.mb.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
@SuppressWarnings("deprecation")
public class CameraColorPickerPreview extends TextureView implements SurfaceTextureListener, PreviewCallback {
    protected static final int POINTER_RADIUS = 5;
    private static final String TAG;
    protected Camera mCamera;
    protected OnColorSelectedListener mOnColorSelectedListener;
    protected Size mPreviewSize;
    protected int[] mSelectedColor;

    public interface OnColorSelectedListener {
        void onColorSelected(int i);
    }

    static {
        TAG = CameraColorPickerPreview.class.getCanonicalName();
    }

   
	public CameraColorPickerPreview(Context context, Camera camera) {
        super(context);
        this.mCamera = camera;
        this.mCamera.getParameters().getPreviewFormat();
        setSurfaceTextureListener(this);
        this.mPreviewSize = this.mCamera.getParameters().getPreviewSize();
        this.mSelectedColor = new int[3];
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            this.mCamera.setPreviewTexture(surface);
            this.mCamera.setPreviewCallback(this);
            this.mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    public void onPreviewFrame(byte[] data, Camera camera) {
        if (this.mOnColorSelectedListener != null) {
            int midX = this.mPreviewSize.width / 2;
            int midY = this.mPreviewSize.height / 2;
            this.mSelectedColor[0] = 0;
            this.mSelectedColor[1] = 0;
            this.mSelectedColor[2] = 0;
            for (int i = 0; i <= POINTER_RADIUS; i++) {
                for (int j = 0; j <= POINTER_RADIUS; j++) {
                    addColorFromYUV420(data, this.mSelectedColor, ((i * POINTER_RADIUS) + j) + 1, (midX - 5) + i, (midY - 5) + j, this.mPreviewSize.width, this.mPreviewSize.height);
                }
            }
            this.mOnColorSelectedListener.onColorSelected(Color.rgb(this.mSelectedColor[0], this.mSelectedColor[1], this.mSelectedColor[2]));
        }
    }

    protected void addColorFromYUV420(byte[] data, int[] averageColor, int count, int x, int y, int width, int height) {
        int size = width * height;
        int xby2 = x / 2;
        int yby2 = y / 2;
        float V = ((float) (data[((xby2 * 2) + size) + (yby2 * width)] & MotionEventCompat.ACTION_MASK)) - 128.0f;
        float U = ((float) (data[(((xby2 * 2) + size) + 1) + (yby2 * width)] & MotionEventCompat.ACTION_MASK)) - 128.0f;
        float Yf = (1.164f * ((float) (data[(y * width) + x] & MotionEventCompat.ACTION_MASK))) - 16.0f;
        int red = (int) ((1.596f * V) + Yf);
        int green = (int) ((Yf - (0.813f * V)) - (0.391f * U));
        int blue = (int) ((2.018f * U) + Yf);
        if (red < 0) {
            red = 0;
        } else if (red > MotionEventCompat.ACTION_MASK) {
            red = MotionEventCompat.ACTION_MASK;
        }
        if (green < 0) {
            green = 0;
        } else if (green > MotionEventCompat.ACTION_MASK) {
            green = MotionEventCompat.ACTION_MASK;
        }
        if (blue < 0) {
            blue = 0;
        } else if (blue > MotionEventCompat.ACTION_MASK) {
            blue = MotionEventCompat.ACTION_MASK;
        }
        averageColor[0] = averageColor[0] + ((red - averageColor[0]) / count);
        averageColor[1] = averageColor[1] + ((green - averageColor[1]) / count);
        averageColor[2] = averageColor[2] + ((blue - averageColor[2]) / count);
    }

    public void setOnColorSelectedListener(OnColorSelectedListener onColorSelectedListener) {
        this.mOnColorSelectedListener = onColorSelectedListener;
    }
}
