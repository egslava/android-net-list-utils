package ru.poloniumarts.netutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;

public class ImageUtils
{
	public static final File saveBitmapOnStorage(Context context, String filename, Bitmap bmp) {
		if (externalStorageAvailable()) {
			File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
		try {
				FileOutputStream outStream = new FileOutputStream(image);
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
				outStream.flush();
				outStream.close();
				return image;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public static final String savePictureToStorage(Context context, byte[] imageData, String filename) {
		if (externalStorageAvailable()) {
			File image = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename);
			try {
				FileOutputStream outStream = new FileOutputStream(image);
				outStream.write(imageData);
				outStream.flush();
				outStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				filename = null;
			} catch (IOException e) {
				e.printStackTrace();
				filename = null;
			}
		}
		return filename;
	}

	public static boolean externalStorageAvailable() {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return mExternalStorageAvailable && mExternalStorageWriteable;
	}
	
	public static String getRealPathFromURI(Context context, Uri contentUri) {

		// can post image
		String[] filePathColumn = {MediaColumns.DATA};

		Cursor cursor = context.getContentResolver().query(contentUri, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();

		return picturePath;
	}
	
	public static Bitmap loadBitmap(String path, int memorySize, int rotation) {

		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(new File(path));
			 
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			Bitmap bmp = BitmapFactory.decodeStream(inputStream, null, options);

			int scale = 1;
			while (options.outWidth * options.outHeight / (scale * scale) > memorySize) {
				scale++;
			}

			inputStream = new FileInputStream(new File(path));
			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				bmp = BitmapFactory.decodeStream(inputStream, null, o);

				// resize to desired dimensions
				int height = bmp.getHeight();
				int width = bmp.getWidth();

				double y = Math.sqrt(memorySize
						/ (((double) width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, (int) x,
						(int) y, true);
				bmp.recycle();
				bmp = scaledBitmap;

				System.gc();
			} else {
				bmp = BitmapFactory.decodeStream(inputStream);
			}
			inputStream.close();
			// rotaton bitmap
			if (rotation != 0)
			{
				Matrix matrix = new Matrix();
				matrix.preRotate(rotation);
				bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			}
			return bmp;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}
}
