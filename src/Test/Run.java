/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import Hreport.Hreport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author TheJenos
 */
public class Run {

    private String Name;
    private String Text;
    private Date now = new Date();

    public Run(String Name, String Text) {
        this.Name = Name;
        this.Text = Text;
    }

    public static void main(String[] args) throws Exception {
        Hreport hr = new Hreport("test.html");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("title", "GG");
        ArrayList al = new ArrayList();
        al.add(new Run("Nadun", "test123"));
        al.add(new Run("Thanura", "test123"));
        hr.compile(map, al);
        hr.openFile();
//        hr.printFile();
        System.out.println(hr);
    }

    public Date getNow() {
        return now;
    }
    
    public String getName() {
        return Name;
    }

    public String getText() {
        return Text;
    }

    
}
