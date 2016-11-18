package pype.mingming.bibiteacher.entity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.tencent.mm.sdk.platformtools.Log;

/**
 * Created by mk on 2016/9/18.
 */
public class MyBackgroud {
    //底片
    public static Bitmap pixelEffectNegative(Bitmap bm){
        int width,height;
        int r,g,b,a;
        width = bm.getWidth();
        height = bm.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[width*height];
        int[] newPx = new int[width*height];
        bm.getPixels(oldPx,0,width,0,0,width,height);

        for (int i = 0;i<oldPx.length;i++){
            //获取像素的色值点
            r = Color.red(oldPx[i]);
            g = Color.green(oldPx[i]);
            b = Color.blue(oldPx[i]);
            a = Color.alpha(oldPx[i]);
            //底片的算法
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            if(r>255){
                r = 255;
            }else if (r<0){
                r = 0;
            }
            if(g>255){
                g = 255;
            }else if (g<0){
                g = 0;
            }
            if(b>255){
                b = 255;
            }else if (b<0){
                b = 0;
            }
            //将那个像素的色值赋值与newPx中
            newPx[i] = Color.argb(a,r,g,b);

        }
        //颜色融合
        bitmap.setPixels(newPx,0,width,0,0,width,height);
        return bitmap;
    }
    //怀旧
    public static Bitmap pixelEffectOldPhoto(Bitmap bm){
        int width,height;
        int r,g,b,a,r1,g1,b1;
        width = bm.getWidth();
        height = bm.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[width*height];
        int[] newPx = new int[width*height];
        bm.getPixels(oldPx,0,width,0,0,width,height);

        for (int i = 0;i<oldPx.length;i++){
            //获取像素的色值点
            r = Color.red(oldPx[i]);
            g = Color.green(oldPx[i]);
            b = Color.blue(oldPx[i]);
            a = Color.alpha(oldPx[i]);
            //怀旧效果的算法
            r1 = (int) (0.393 * r + 0.769 * g + 0.189 * b);
            g1 = (int) (0.349 * r + 0.686 * g + 0.168 * b);
            b1 = (int) (0.272 * r + 0.534 * g + 0.131 * b);

            if(r1>255){
                r1 = 255;
            }
            if(g1>255){
                g1 = 255;
            }
            if(b1>255){
                b1 = 255;
            }
            //将那个像素的色值赋值与newPx中
            newPx[i] = Color.argb(a,r1,g1,b1);

        }
        //颜色融合
        bitmap.setPixels(newPx,0,width,0,0,width,height);
        return bitmap;
    }
    //浮雕
    public static Bitmap pixelEffectRillievo(Bitmap bm){
        int width,height;
        int color=0,colorBefore=0;
        int r,g,b,a,r1,g1,b1;
        width = bm.getWidth();
        height = bm.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);

        int[] oldPx = new int[width*height];
        int[] newPx = new int[width*height];

        bm.getPixels(oldPx,0,width,0,0,width,height);

        for (int i = 1;i<oldPx.length;i++){
            colorBefore = oldPx[i-1];
            //获取像素的色值点
            r = Color.red(colorBefore);
            g = Color.green(colorBefore);
            b = Color.blue(colorBefore);
            a = Color.alpha(colorBefore);

            color = oldPx[i];
            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);

            r = r - r1 + 127;
            g = g - g1 + 127;
            b = b - b1 + 127;

            if(r>255){
                r = 255;
            }
            if(g>255){
                g = 255;
            }
            if(b>255){
                b = 255;
            }
            //将那个像素的色值赋值与newPx中
            newPx[i] = Color.argb(a,r,g,b);

        }
        //颜色融合
        bitmap.setPixels(newPx,0,width,0,0,width,height);
        return bitmap;
    }
    /**
     * 柔化效果(高斯模糊)(优化后比上面快三倍)
     * @param bmp
     * @return
     */
    private Bitmap blurImageAmeliorate(Bitmap bmp)
    {
        long start = System.currentTimeMillis();
        // 高斯矩阵
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 16; // 值越小图片会越亮，越大则越暗

        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++)
        {
            for (int k = 1, len = width - 1; k < len; k++)
            {
                idx = 0;
                for (int m = -1; m <= 1; m++)
                {
                    for (int n = -1; n <= 1; n++)
                    {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.d("may", "used time="+(end - start));
        return bitmap;
    }
}
