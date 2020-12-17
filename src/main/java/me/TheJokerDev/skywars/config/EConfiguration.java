package me.TheJokerDev.skywars.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;







public class EConfiguration
{
    protected Map<String, List<String>> comments = new LinkedHashMap();


    protected boolean newLinePerKey = false;



    public Map<String, List<String>> getComments() { return this.comments; }



    public List<String> getComments(String paramString) { return this.comments.containsKey(paramString) ? new ArrayList((Collection)this.comments.get(paramString)) : new ArrayList(); }



    public void setNewLinePerKey(boolean paramBoolean) { this.newLinePerKey = paramBoolean; }



    public boolean shouldAddNewLinePerKey() { return this.newLinePerKey; }


    public String getPathToComment(List<String> paramList, int paramInt, String paramString) {
        if (paramList != null && paramInt >= 0 && paramInt < paramList.size() - 1 && paramString != null) {
            String str = trimPrefixSpaces(paramString);
            if (str.startsWith("#")) {
                int i = paramInt;
                while (i < paramList.size() - 1) {
                    i++;
                    String str1 = (String)paramList.get(i);
                    String str2 = trimPrefixSpaces(str1);
                    if (!str2.startsWith("#")) {
                        if (str2.contains(":")) {
                            return getPathToKey(paramList, i, str1);
                        }

                        break;
                    }
                }
            }
        }
        return null;
    }

    public String getPathToKey(List<String> paramList, int paramInt, String paramString) {
        if (paramList != null && paramInt >= 0 && paramInt < paramList.size() && paramString != null &&
                !paramString.startsWith("#") && paramString.contains(":")) {
            int i = getPrefixSpaceCount(paramString);
            String str = trimPrefixSpaces(paramString.substring(0, paramString.indexOf(':')));
            if (i > 0) {
                int j = paramInt;
                int k = -1;
                boolean bool = false;

                while (j > 0) {
                    j--;
                    String str1 = (String)paramList.get(j);
                    int m = getPrefixSpaceCount(str1);
                    if (trim(str1).isEmpty())
                        break;
                    if (!trim(str1).startsWith("#") &&
                            m < i &&
                            str1.contains(":")) {
                        if (m > 0 || !bool) {
                            if (m == 0)
                                bool = true;
                            if (k == -1 || m < k) {

                                k = m;
                                str = trimPrefixSpaces(str1.substring(0, str1.indexOf(":"))) + "." + str;
                            }

                            continue;
                        }

                        break;
                    }
                }
            }

            return str;
        }

        return null;
    }

    public int getPrefixSpaceCount(String paramString) {
        byte b = 0;
        if (paramString != null && paramString.contains(" ")) {
            char[] arrayOfChar; int i; byte b1; for (arrayOfChar = paramString.toCharArray(), i = arrayOfChar.length, b1 = 0; b1 < i; ) { char c = arrayOfChar[b1];
                if (c == ' ') {
                    b++;
                    b1++;
                }  }

        }
        return b;
    }


    public String trim(String paramString) { return (paramString != null) ? paramString.trim().replace(System.lineSeparator(), "") : ""; }


    public String trimPrefixSpaces(String paramString) {
        if (paramString != null)
            while (paramString.startsWith(" ")) {
                paramString = paramString.substring(1);
            }
        return paramString;
    }
}
