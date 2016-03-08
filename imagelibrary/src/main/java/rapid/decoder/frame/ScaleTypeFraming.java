package rapid.decoder.frame;

import android.widget.ImageView;

import rapid.decoder.BitmapDecoder;

public class ScaleTypeFraming extends FramingMethod {
    private ImageView.ScaleType mScaleType;

    public ScaleTypeFraming(ImageView.ScaleType scaleType) {
        mScaleType = scaleType;
    }

    @Override
    public FramedDecoder createFramedDecoder(BitmapDecoder decoder, int frameWidth, int frameHeight) {
        return FramedDecoder.newInstance(decoder, frameWidth, frameHeight, mScaleType);
    }
}
