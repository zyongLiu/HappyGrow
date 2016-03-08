package rapid.decoder;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.os.Build;
import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

class ByteArrayBitmapLoader extends BitmapLoader {
	private byte[] data;
	private int offset;
	private int length;
	
	public ByteArrayBitmapLoader(byte[] data, int offset, int length) {
		if (data == null) {
			throw new NullPointerException();
		}
		
		this.data = data;
		this.offset = offset;
		this.length = length;
	}
	
	protected ByteArrayBitmapLoader(ByteArrayBitmapLoader other) {
		super(other);
		data = other.data;
		offset = other.offset;
		length = other.length;
	}

	@Override
	protected Bitmap decode(Options opts) {
		return BitmapFactory.decodeByteArray(data, offset, length, opts);
	}
	
	@Override
	protected InputStream openInputStream() {
		return new ByteArrayInputStream(data, offset, length);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	@Override
	protected BitmapRegionDecoder createBitmapRegionDecoder() {
		try {
			return BitmapRegionDecoder.newInstance(data, offset, length, false);
		} catch (IOException e) {
			return null;
		}
	}

	@NonNull
    @Override
	public BitmapLoader fork() {
		return new ByteArrayBitmapLoader(this);
	}
	
	@Override
	public int hashCode() {
		if (mHashCode == 0) {
            mHashCode = super.hashCode() + 31 * (Arrays.hashCode(data) + 31 * (offset + 31 * length));
        }
        return mHashCode;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!super.equals(o) || !(o instanceof ByteArrayBitmapLoader)) return false;
		
		final ByteArrayBitmapLoader d = (ByteArrayBitmapLoader) o;
		return Arrays.equals(data, d.data) &&
				offset == d.offset &&
				length == d.length;
	}
}
