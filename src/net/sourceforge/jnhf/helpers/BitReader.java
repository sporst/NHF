package net.sourceforge.jnhf.helpers;

public class BitReader
{
    private int bitCounter_ = 0;
    private int byteCounter_ = 0;
    private byte[] data_ = null;

    public BitReader(final byte[] data, final int offset)
    {
        if (data == null)
        {
        	throw new IllegalArgumentException("Error: Argument data can't be null");
        }

        data_ = data;
        byteCounter_ = offset;
    }

    private int getBits(final byte b, final int bit, final int length)
    {
        if (length <= 0) return 0;

        return b & makeMask(bit, length);
    }

    private int makeMask(final int bit, final int length)
    {
        int mask = 0;

        for (int i = 0; i < length; i++)
        {
            mask |= 1 << (7 - (bit + i));
        }

        return mask;
    }

    public int getByteCounter()
    {
            return byteCounter_;
    }

    public int readBits(final int bits)
    {
        final int len1 = bits > 8 - bitCounter_ ? 8 - bitCounter_ : bits;
        final int len2 = bits - len1;

        int bits1 = getBits(data_[byteCounter_], bitCounter_, len1);
        int bits2 = getBits(data_[byteCounter_ + 1], 0, len2);

        if (len1 <= bits) bits1 >>= (8 - (len1 + bitCounter_));
        if (len2 > 0) bits1 <<= len2;

        bits2 >>= 8 - len2;

        bitCounter_ += bits;

        if (bitCounter_ >= 8)
        {
            byteCounter_++;
            bitCounter_ %= 8;
        }

        return bits1 | bits2;
    }
}
