package util;

/**
 * 异或加密，解密.
 * */
public class Encrypt {
    private static final String key = "key";

    public static String setEncrypt(String str){
        int strLen = str.length();//要加密的字符串长度
        int[] strNum = new int[strLen];//字符串的每一位和key异或后变成整型，存放在这里
        String temp = "";//后面在字符串拼接时作为临时字符串使用
        StringBuilder result = new StringBuilder();//最终加密好的字符串放这里

        for(int i=0,j=0;i<strLen;i++,j++){
            if(j==key.length()){
                j=0;
            }
            strNum[i] = str.charAt(i)^key.charAt(j);
        }

        for(int i=0;i<strLen;i++){
            if(strNum[i]<10){
                temp = "00"+strNum[i];
            }else {
                if(strNum[i]<100){
                    temp = "0"+strNum[i];
                }
            }
            result.append(temp);
        }

        return result.toString();
    }

    public static String getEncrypt(String str){
        char[] strChar = new char[str.length()/3];
        StringBuilder result = new StringBuilder();

        for(int i=0,j=0;i<str.length()/3;i++,j++){
            if(j == key.length()){
                j = 0;
            }
            int n = Integer.parseInt(str.substring(i*3,i*3+3));
            strChar[i] = (char)((char)n^key.charAt(j));
            result.append(strChar[i]);
        }

        return result.toString();
    }

}
