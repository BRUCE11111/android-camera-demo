package com.example.demo_camera2;

public class ImageUtils {

    // This value is 2 ^ 18 - 1, and is used to hold the RGB values together before their ranges
    // are normalized to eight bits.
    private  static int MAX_CHANNEL_VALUE = 262143;

    static int checkBoundaries(int x){
        // Clipping RGB values to be inside boundaries [ 0 , MAX_CHANNEL_VALUE ]

        if(x>MAX_CHANNEL_VALUE){
            x=MAX_CHANNEL_VALUE;
        }else if(x<0){
            x=0;
        }

        return x;
    }

    /** Helper function to convert y,u,v integer values to RGB format */
    private
    static int convertYUVToRGB(int y, int u, int v){
        // Adjust and check YUV values

        //Log.i("me",y+" "+u+" "+v);

        int yNew=(y-16)<0?0:(y-16);
        int uNew = u - 128;
        int vNew = v - 128;
        int expandY = 1192 * yNew;
        int r = expandY + 1634 * vNew;
        int g = expandY - 833 * vNew - 400 * uNew;
        int b = expandY + 2066 * uNew;


        r = checkBoundaries(r);
        g = checkBoundaries(g);
        b = checkBoundaries(b);
        return -0x1000000 | (r << 6 & 0xff0000) | (g >> 2 & 0xff00) | (b >> 10 & 0xff);
        //return -0x1000000 or (r shl 6 and 0xff0000) or (g shr 2 and 0xff00) or (b shr 10 and 0xff)
    }



    /** Converts YUV420 format image data (ByteArray) into ARGB8888 format with IntArray as output. */
    static void convertYUV420ToARGB8888(
            byte[] yData,
            byte[] uData,
            byte[] vData,
            int width,
            int height,
            int yRowStride,
            int uvRowStride,
            int uvPixelStride,
            int[] out
    ) {
        int outputIndex = 0;
        //for (j in 0 until height)

        for(int i=yData.length-1;i>=0;i--){

        }

        for(int j=0;j<height;j++){
            int positionY = yRowStride * j;
            int positionUV = uvRowStride * (j >> 1);

            //for (i in 0 until width)
            for(int i=0;i<width;i++)
            {
                int uvOffset = positionUV + (i >> 1) * uvPixelStride;

                // "0xff and" is used to cut off bits from following value that are higher than
                // the low 8 bits
                //Log.i("outputIndex",outputIndex+"");
                out[outputIndex] = convertYUVToRGB(
                        0xff & (int) yData[positionY + i], 0xff & (int) uData[uvOffset],
                        0xff & (int) vData[uvOffset]
                );
                //Log.i("outputindex",out[outputIndex]+"");
                outputIndex += 1;
            }
        }
    }



}
