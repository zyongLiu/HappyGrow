package rapid.decoder.builtin;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;

import java.io.InputStream;

public class JpegDecoder {
    private static boolean sInitialized;
    private static boolean sHasLibrary;
    public static void initDecoder() {
        if (!sInitialized) {
            sInitialized = true;
            System.loadLibrary("jpeg-decoder");
            init();
            sHasLibrary = true;
        } else if (!sHasLibrary) {
            throw new UnsatisfiedLinkError();
        }
    }

    private static native void init();
	private static native long createNativeDecoder(InputStream in);
	private static native void destroyNativeDecoder(long decoder);
	private static native boolean nativeBegin(long decoder);
	private static native int nativeGetWidth(long decoder);
	private static native int nativeGetHeight(long decoder);
	private static native Bitmap nativeDecode(long decoder, int left, int top, int right, int bottom, boolean filter,
			Config config, Options opts);
	
	private long decoder;

	public JpegDecoder(InputStream in) {
		decoder = createNativeDecoder(in);
	}
	
	public void close() {
		if (decoder == 0) return;
		
		destroyNativeDecoder(decoder);
		decoder = 0;
	}
	
	public boolean begin() {
		if (decoder == 0) {
			throw new IllegalStateException();
		}
		
		return nativeBegin(decoder);
	}
	
	public int getWidth() {
		if (decoder == 0) {
			throw new IllegalStateException();
		}

		return nativeGetWidth(decoder);
	}
	
	public int getHeight() {
		if (decoder == 0) {
			throw new IllegalStateException();
		}

		return nativeGetHeight(decoder);
	}
	
	public Bitmap decode(Rect bounds, boolean filter, Config config, Options opts) {
		if (decoder == 0) {
			throw new IllegalStateException();
		}

		if (bounds == null) {
			return nativeDecode(decoder, -1, -1, -1, -1, filter, config, opts);
		} else {
			return nativeDecode(decoder,
					bounds.left, bounds.top, bounds.right, bounds.bottom,
					filter, config, opts);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}
}
