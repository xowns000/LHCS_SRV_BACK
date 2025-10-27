package kr.co.hkcloud.palette3.core.security.crypto;


public final class PKCS5Padding
{

    public static byte[] pad(byte[] in, int blockSize)
    {
        if(in == null)
            return null;

        int offset = in.length;
        int len = blockSize - (offset % blockSize);

        byte paddingOctet = (byte) (len & 0xff);

        byte[] out = new byte[offset + len];

        System.arraycopy(in, 0, out, 0, in.length);
        for(int i = offset; i < out.length; i++) {
            out[i] = paddingOctet;
        }

        return out;
    }


    public static byte[] unpad(byte[] in, int blockSize)
    {
        if(in == null)
            return null;

        int len = in.length;
        byte lastByte = in[len - 1];
        int padValue = (int) (lastByte & 0xff);

        if((padValue < 0x01) || (padValue > blockSize)) { return null; }

        int offset = len - padValue;

        for(int i = offset; i < len; i++) {
            if(in[i] != padValue)
                return null;
        }

        byte[] out = new byte[offset];

        System.arraycopy(in, 0, out, 0, offset);

        return out;
    }
}
