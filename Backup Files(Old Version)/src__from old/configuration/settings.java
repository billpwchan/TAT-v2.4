/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.util.prefs.Preferences;

/**
 *
 * @author T0155041
 */
public class settings {

    //private String configPath = "settings.properties";

    public static String scriptsPaht;
    
    private Preferences prefs;

    public void readSettings() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        scriptsPaht=prefs.get("scriptPath", "");
//        Properties properties = new Properties();
//        try {
//
//
//            FileInputStream in = new FileInputStream(configPath);
//            properties.load(in);
//            in.close();
//        } catch (IOException e) {
//            System.out.println("Unable to load config file.");
//        }
//
//        //let's do the magic
//        scriptsPaht = properties.getProperty("scriptPath");
    }

    public void saveSettings() {
        prefs.put("scriptPath", scriptsPaht);
    }

}
