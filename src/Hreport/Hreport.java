/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Hreport;

import Error.HreportException;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 *
 * @author TheJenos
 */
public class Hreport {

    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    String In_txt;
    String Out_txt;
    File file;
    File tmpfile;

    public Hreport(String file) throws HreportException {
        this(new File(file));
    }

    public Hreport(File file) throws HreportException {
        this.file = file;
        try {
            this.In_txt = readFile(file);
            this.Out_txt = this.In_txt;
            this.tmpfile = File.createTempFile(file.getName().split("\\.")[0], ".html");
        } catch (Exception e) {
            throw new HreportException(e);
        }
    }

    public String regexFilter(String s) {
        return Pattern.quote(s);
    }

    private String LooperText(List cr, String format) throws HreportException {
        String finaltxt = "";
        try {
            for (Object object : cr) {
                Class c = object.getClass();
                String s = format;
                Matcher m = Pattern.compile("\\[\\[([^]]+)\\]\\]").matcher(s);
                while (m.find()) {
                    String[] sub = m.group(1).split("\\.");
                    Object retrunO = null;
                    Object pojo = object;
                    c = object.getClass();
                    for (int i = 0; i < sub.length; i++) {
                        String string = sub[i];
                        if (string.startsWith("#")) {
                            String paras[] = string.substring(1).split(",");
                            if (paras.length > 1) {
                                Class[] classes = new Class[paras.length - 1];
                                Object[] objs = new Object[paras.length - 1];
                                for (int j = 1; j < paras.length; j++) {
                                    String para = paras[j];
                                    DataPill dp = getRealObject(para);
                                    classes[j - 1] = dp.getDatatype();
                                    objs[j - 1] = dp.getObject();
                                }
                                retrunO = c.getMethod(paras[0], classes).invoke(pojo, objs);
                            } else {
                                retrunO = c.getMethod(string.substring(1), null).invoke(pojo, null);
                            }
                        } else {
                            retrunO = c.getMethod("get" + string, null).invoke(pojo, null);
                        }
                        if (retrunO != null) {
                            c = retrunO.getClass();
                        }
                        pojo = retrunO;
                    }
                    s = s.replaceAll("\\[\\[" + m.group(1) + "\\]\\]", retrunO.toString());
                }
                Matcher mss = Pattern.compile("TIME\\{([^}]+)\\}").matcher(s);
                while (mss.find()) {
                    String dateString[] = mss.group(1).split("->");
                    String dats = dateString[1];
                    Date d = null;
                    try {
                        d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").parse(dats);
                    } catch (Exception e) {
                        d = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy").parse(dats);
                    }
                    String newdate = new SimpleDateFormat(dateString[0]).format(d);
                    s = s.replaceAll("TIME\\{" + regexFilter(mss.group(1)) + "\\}", newdate);
                }
                finaltxt += s + "\n";
            }
        } catch (Exception e) {
            throw new HreportException(e);
        }
        return finaltxt;
    }

    private DataPill getRealObject(String s) throws Exception {
        String data[] = s.split(":");
        String ss = data[0].trim();
        if (data.length > 1) {
            if (ss.equalsIgnoreCase("String")) {
                return new DataPill(String.class, data[1]);
            } else if (ss.equalsIgnoreCase("int")) {
                return new DataPill(int.class, Integer.parseInt(data[1]));
            } else if (ss.equalsIgnoreCase("long")) {
                return new DataPill(long.class, Long.parseLong(data[1]));
            } else if (ss.equalsIgnoreCase("char")) {
                return new DataPill(char.class, data[1].charAt(0));
            } else if (ss.equalsIgnoreCase("byte")) {
                return new DataPill(byte.class, Byte.parseByte(data[1]));
            } else if (ss.equalsIgnoreCase("float")) {
                return new DataPill(float.class, Float.parseFloat(data[1]));
            } else if (ss.equalsIgnoreCase("double")) {
                return new DataPill(double.class, Double.parseDouble(data[1]));
            } else if (ss.equalsIgnoreCase("boolean")) {
                return new DataPill(boolean.class, Boolean.parseBoolean(data[1]));
            } else {
                return new DataPill(s.getClass(), data[1]);
            }
        } else {
            return new DataPill(String.class, data[1]);
        }
    }
    
    public void compile(HashMap<String, String> paras, List cr) throws HreportException {
        Matcher mss = Pattern.compile("PARA\\{([^}]+)\\}").matcher(this.In_txt);
        while (mss.find()) {
            this.Out_txt = this.Out_txt.replaceAll("PARA\\{" + regexFilter(mss.group(1)) + "\\}", paras.getOrDefault(mss.group(1), ""));
        }
        Matcher ms = Pattern.compile("<loop>([^\\u0000]+)</loop>").matcher(this.In_txt);
        while (ms.find()) {
            String fnl_txt = LooperText(cr, ms.group(1).trim());
            this.Out_txt = this.Out_txt.replaceAll("<loop>" + regexFilter(ms.group(1)) + "</loop>", fnl_txt);
        }
        this.Out_txt = javascriptEngin(Out_txt);
    }

    @Override
    public String toString() {
        return this.Out_txt;
    }

    public void openFile() throws HreportException {
        try {
            writeFile(this.Out_txt, tmpfile);
            Desktop.getDesktop().open(tmpfile);
        } catch (Exception e) {
            throw new HreportException(e);
        }
    }

    public void printFile() throws HreportException {
        try {
            writeFile(this.Out_txt + "\n<script>window.print()</script>", tmpfile);
            Desktop.getDesktop().open(tmpfile);
        } catch (Exception e) {
            throw new HreportException(e);
        }
    }

    private String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    private void writeFile(String text, File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    public String javascriptEngin(String s) throws HreportException {
        String codes[] = s.split("JS");
        for (int i = 0; i < codes.length; i++) {
            String code = codes[i];
            Matcher ms = Pattern.compile("\\{([^\\u0000]+)\\}").matcher(code);
            while (ms.find()) {
                s = s.replaceAll("JS\\{" + regexFilter(ms.group(1)) + "\\}", eval(ms.group(1)).toString());
            }
        }
        return s;
    }

    public Object eval(String input) throws HreportException {
        try {
            return engine.eval("function run(){" + input + "}run();");
        } catch (Exception e) {
            e.printStackTrace();
            throw new HreportException(e);
        }
    }

}
