package me.TheJokerDev.skywars.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;


public class SkyConfiguration
        extends YamlConfiguration
        implements IConfiguration
{
    private EConfiguration econfig;
    private File file;

    public SkyConfiguration(File paramFile) {
        this.econfig = new EConfiguration();
        this.file = paramFile;
        try {
            load(paramFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public EConfiguration getEConfig() { return this.econfig; }




    public File getFile() { return this.file; }



    public void load(File paramFile) throws IOException {
        this.file = paramFile;
        options().header("");
        BufferedReader bufferedReader = null;
        ArrayList arrayList = new ArrayList();
        try {
            bufferedReader = new BufferedReader(new FileReader(paramFile));
            String str;
            for (; (str = bufferedReader.readLine()) != null; arrayList.add(str));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) bufferedReader.close();

        }
        if (arrayList.isEmpty()) {
            Bukkit.getLogger().log(Level.SEVERE, paramFile.getName() + " doesn't have nothing to load");

            return;
        }
        int bool = !this.econfig.trim((String)arrayList.get(0)).isEmpty() ? 1 : 0;

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (byte b = 0; b < arrayList.size(); b++) {
            String str1 = (String)arrayList.get(b);
            String str2 = this.econfig.trimPrefixSpaces(str1);
            if (str2.startsWith("#") && (b > 0)) {
                String str = this.econfig.getPathToComment(arrayList, b, str1);
                if (str != null) {
                    List list = (List)linkedHashMap.get(str);
                    if (list == null) list = new ArrayList();
                    list.add(str2.substring(str2.startsWith("# ") ? 2 : 1));
                    linkedHashMap.put(str, list);
                }
            }
        }
    }


    public void save() {
        try {
            save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void save(File paramFile) throws IOException {

        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(paramFile));
            String str;
            for (; (str = bufferedReader.readLine()) != null; arrayList.add(str));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) bufferedReader.close();

        }
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(paramFile));
            bufferedWriter.write("");
            for (byte b = 0; b < arrayList.size(); b++) {
                String str1 = (String)arrayList.get(b);

                String str2 = null;
                if (!str1.startsWith("#") && str1.contains(":"))
                    str2 = this.econfig.getPathToKey(arrayList, b, str1);
                if (str2 != null && this.econfig.getComments().containsKey(str2)) {
                    int i = this.econfig.getPrefixSpaceCount(str1);
                    String str = "";
                    for (byte b1 = 0; b1 < i; ) { str = str + " "; b1++; }
                    List<String> list = (List)this.econfig.getComments().get(str2);

                    if (list != null) {
                        for (String str3 : list) {
                            bufferedWriter.append(str).append("# ").append(str3);
                            bufferedWriter.newLine();
                        }
                    }
                }

                boolean bool = str1.startsWith("#");

                if (str1.startsWith("-") || str1.startsWith("  -") || str1.startsWith("    -") || str1.startsWith("      -")) {
                    bufferedWriter.append("  " + str1);
                } else {
                    bufferedWriter.append(str1);
                }
                bufferedWriter.newLine();

                if (this.econfig.shouldAddNewLinePerKey() && b < arrayList.size() - 1 && !bool) {
                    String str = (String)arrayList.get(b + 1);
                    if (str != null && !str.startsWith(" ") &&
                            !str.startsWith("'") && !str.startsWith("-")) bufferedWriter.newLine();

                }
            }
        } finally {
            if (bufferedWriter != null) bufferedWriter.close();

        }
    }

    public void set(String paramString, Object paramObject) {
        if (paramObject != null)
        { if (this.econfig.getComments(paramString).size() > 0) {
            this.econfig.getComments().put(paramString, this.econfig.getComments(paramString));
        } else {
            this.econfig.getComments().remove(paramString);
        }  }
        else { this.econfig.getComments().remove(paramString); }

        super.set(paramString, paramObject);
    }

    public void addDefault(String paramString, Object paramObject, String... paramVarArgs) {
        if (paramObject != null && paramVarArgs != null && paramVarArgs.length > 0 && !this.econfig.getComments().containsKey(paramString)) {
            ArrayList arrayList = new ArrayList();
            for (String str : paramVarArgs) {
                if (str != null) { arrayList.add(str); }
                else { arrayList.add(""); }

            }  this.econfig.getComments().put(paramString, arrayList);
        }
        addDefault(paramString, paramObject);
    }


    public void createSection(String paramString, String... paramVarArgs) {
        if (paramString != null && paramVarArgs != null && paramVarArgs.length > 0) {
            ArrayList arrayList = new ArrayList();
            for (String str : paramVarArgs) {
                if (str != null) { arrayList.add(str); }
                else { arrayList.add(""); }

            }  this.econfig.getComments().put(paramString, arrayList);
        }
        createSection(paramString);
    }


    public void setHeader(String... paramVarArgs) {
        String str = "";
        for (String str1 : paramVarArgs) {
            str = str + str1 + "\n";
        }
        options().header(str);
    }


    public void set(String paramString, Object paramObject, String... paramVarArgs) {
        if (paramObject != null) {
            if (paramVarArgs != null) {
                if (paramVarArgs.length > 0) {
                    ArrayList arrayList = new ArrayList();
                    for (String str : paramVarArgs) {
                        if (str != null) { arrayList.add(str); }
                        else { arrayList.add(""); }

                    }  this.econfig.getComments().put(paramString, arrayList);
                } else {
                    this.econfig.getComments().remove(paramString);
                }
            }
        } else {
            this.econfig.getComments().remove(paramString);
        }
        super.set(paramString, paramObject);
    }
}
