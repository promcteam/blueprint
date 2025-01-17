package studio.magemonkey.blueprint.nbt;

import java.io.*;
import java.nio.ByteOrder;


public class EndianSwitchableInputStream extends FilterInputStream implements DataInput {
    private final ByteOrder endianness;

    public EndianSwitchableInputStream(InputStream stream, ByteOrder endianness) {
        super(stream instanceof DataInputStream ? stream : new DataInputStream(stream));
        this.endianness = endianness;
    }

    public ByteOrder getEndianness() {
        return endianness;
    }

    protected DataInputStream getBackingStream() {
        return (DataInputStream) super.in;
    }

    public void readFully(byte[] bytes) throws IOException {
        getBackingStream().readFully(bytes);
    }

    public void readFully(byte[] bytes, int i, int i1) throws IOException {
        getBackingStream().readFully(bytes, i, i1);
    }

    public int skipBytes(int i) throws IOException {
        return getBackingStream().skipBytes(i);
    }

    public boolean readBoolean() throws IOException {
        return getBackingStream().readBoolean();
    }

    public byte readByte() throws IOException {
        return getBackingStream().readByte();
    }

    public int readUnsignedByte() throws IOException {
        return getBackingStream().readUnsignedByte();
    }

    public short readShort() throws IOException {
        short ret = getBackingStream().readShort();
        if (endianness == ByteOrder.LITTLE_ENDIAN) {
            ret = Short.reverseBytes(ret);
        }
        return ret;
    }

    public int readUnsignedShort() throws IOException {
        int ret = getBackingStream().readUnsignedShort();
        if (endianness == ByteOrder.LITTLE_ENDIAN) {
            ret = (char) (Integer.reverseBytes(ret) >> 16);
        }
        return ret;
    }

    public char readChar() throws IOException {
        char ret = getBackingStream().readChar();
        if (endianness == ByteOrder.LITTLE_ENDIAN) {
            ret = Character.reverseBytes(ret);
        }
        return ret;
    }

    public int readInt() throws IOException {
        return endianness == ByteOrder.LITTLE_ENDIAN
                ? Integer.reverseBytes(getBackingStream().readInt())
                : getBackingStream().readInt();
    }

    public long readLong() throws IOException {
        return endianness == ByteOrder.LITTLE_ENDIAN
                ? Long.reverseBytes(getBackingStream().readLong())
                : getBackingStream().readLong();
    }

    public float readFloat() throws IOException {
        int result = readInt();
        if (endianness == ByteOrder.LITTLE_ENDIAN) {
            result = Integer.reverseBytes(result);
        }
        return Float.intBitsToFloat(result);
    }

    public double readDouble() throws IOException {
        long result = readLong();
        if (endianness == ByteOrder.LITTLE_ENDIAN) {
            result = Long.reverseBytes(result);
        }
        return Double.longBitsToDouble(result);
    }

    @SuppressWarnings("deprecation") // This method is deprecated
    public String readLine() throws IOException {
        return getBackingStream().readLine();
    }

    public String readUTF() throws IOException {
        return getBackingStream().readUTF();
    }
}
