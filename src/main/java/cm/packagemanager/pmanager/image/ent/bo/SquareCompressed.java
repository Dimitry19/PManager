package cm.packagemanager.pmanager.image.ent.bo;

public final class SquareCompressed implements ImageFormatBO {

    private final int size;

    public SquareCompressed(int size) {
        this.size = size;
    }

    @Override
    public int width() {
        return size;
    }

    @Override
    public int height() {
        return size;
    }

    @Override
    public float compression() {
        return 0.50f;
    }
}