package com.trikon.medicine.Utility;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by comsol on 21-Nov-16.
 */
public class AccessDirectory {
    /**
     * returning image / video
     */


    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "PharmacyProject");
        imagesFolder.mkdirs();

        File image = new File(imagesFolder, "img_" + timeStamp + ".png");
        return image;
    }
}
