/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Hreport.Hreport;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author TheJenos
 */
public class Run {

    private String txt;
    private String txt2;
    private Date gg = new Date();

    public Run(String txt, String txt2) {
        this.txt = txt;
        this.txt2 = txt2;
    }

    public static void main(String[] args) throws Exception {
        Hreport hr = new Hreport("test.html");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("title", "GG");
        ArrayList al = new ArrayList();
        al.add(new Run("GG", "GG"));
        al.add(new Run("GG2", "GG2"));
        hr.compile(map, al);
//        hr.openFile();
//        hr.printFile();
        System.out.println(hr);
    }

    public Date getGg() {
        return gg;
    }
    
    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public String getTxt2() {
        return txt2;
    }

    public void setTxt2(String txt2) {
        this.txt2 = txt2;
    }
}
