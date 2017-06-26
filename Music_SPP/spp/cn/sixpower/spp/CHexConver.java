package cn.sixpower.spp;

import cn.mb.http.VDLog;

public class CHexConver {
    private static final String mHexStr = "0123456789ABCDEF";
    private static final char[] mChars = mHexStr.toCharArray();

    public static String byte2HexStr(byte[] paramArrayOfByte, int paramInt) {
        StringBuilder localStringBuilder = new StringBuilder("");
        for (int i = 0; i < paramInt; i++) {
            String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
            if (str.length() == 1)
                str = "0" + str;

            localStringBuilder.append(str);
            localStringBuilder.append(" ");
        }
        return localStringBuilder.toString().toUpperCase().trim();
    }

    public static String int2HexStr(int b) {
        String str = Integer.toHexString(0xFF & b);
        if (str.length() == 1)
            str = "0" + str;
        str = str + " ";

        return str.toUpperCase();
    }

    public static boolean checkHexStr(String paramString) {
        String str = paramString.toString().trim().replace(" ", "")
                .toUpperCase();
        int j = str.length();
        int i;
        if ((j <= 1) || (j % 2 != 0)) {
            i = 0;
        } else {
            i = 0;
            while (i < j) {
                if (mHexStr.contains(str.substring(i, i + 1))) {
                    i++;
                    continue;
                }
                i = 0;
                break;
            }
            i = 1;
        }
        return i == 0 ? false : true;
    }

    public static byte[] hexStr2Bytes(String paramString) throws NumberFormatException {
        VDLog.i("CHexConver", ">>>>hexStr2Bytes>>>>>:" + paramString);
        String str = paramString.trim().replace(" ", "").toUpperCase();
        int k = str.length() / 2;
        byte[] arrayOfByte = new byte[k];
        for (int j = 0; j < k; j++) {
            int i = 1 + j * 2;
            int m = i + 1;
            // try {

            arrayOfByte[j] = (byte) (0xFF & Integer.decode(
                    "0x" + str.substring(j * 2, i) + str.substring(i, m))
                    .intValue());
            // } catch (NumberFormatException e) {
            // // TODO: handle exception
            //
            // }
        }
        return arrayOfByte;
    }

    public static String hexStr2Str(String paramString) {
        char[] arrayOfChar = paramString.toCharArray();
        byte[] arrayOfByte = new byte[paramString.length() / 2];
        for (int i = 0; i < arrayOfByte.length; i++)
            arrayOfByte[i] = (byte) (0xFF & 16
                    * mHexStr.indexOf(arrayOfChar[(i * 2)])
                    + mHexStr.indexOf(arrayOfChar[(1 + i * 2)]));
        return new String(arrayOfByte);
    }

    public static String str2HexStr(String paramString) {
        StringBuilder localStringBuilder = new StringBuilder("");
        byte[] arrayOfByte = paramString.getBytes();
        for (int i = 0; i < arrayOfByte.length; i++) {
            int j = (0xF0 & arrayOfByte[i]) >> 4;
            localStringBuilder.append(mChars[j]);
            j = 0xF & arrayOfByte[i];
            localStringBuilder.append(mChars[j]);
            localStringBuilder.append(' ');
        }
        return localStringBuilder.toString().trim();
    }

    public static String strToUnicode(String paramString) throws Exception {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int j = 0; j < paramString.length(); j++) {
            int i = paramString.charAt(j);
            String str = Integer.toHexString(i);
            if (i <= 128)
                localStringBuilder.append("\\u00" + str);
            else
                localStringBuilder.append("\\u" + str);
        }
        return localStringBuilder.toString();
    }

    public static String unicodeToString(String paramString) {
        int i = paramString.length() / 6;
        StringBuilder localStringBuilder = new StringBuilder();
        for (int j = 0; j < i; j++) {
            String str2 = paramString.substring(j * 6, 6 * (j + 1));
            String str1 = str2.substring(2, 4) + "00";
            str2 = str2.substring(4);
            localStringBuilder.append(new String(Character.toChars(Integer
                    .valueOf(str1, 16).intValue()
                    + Integer.valueOf(str2, 16).intValue())));
        }
        return localStringBuilder.toString();
    }
}