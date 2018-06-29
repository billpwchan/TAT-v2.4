/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBcontroller;

import DB.CaseExecutions;
import DB.Iterations;
import DB.Macro;
import DB.ParamScriptMacro;
import DB.Script;
import DB.ScriptHasParameters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author T0155040
 */
public class MacroDB {

    public MacroDB() {

    }

    public void getAllFromMacro(Script macro) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        try{
        session.update(macro);
        }catch(Exception e){
            session.merge(macro);
        }
        Iterator<Macro> itScripts = macro.getMacrosForScriptIdScript().iterator();
        //System.out.println("NUMBER OF SCRIPT INSIDE = " + macro.getMacrosForScriptIdScript().size());
        while (itScripts.hasNext()) {
            Macro mac = itScripts.next();
            Hibernate.initialize(mac.getScriptByScriptIdScript1());
            Iterator<ParamScriptMacro> itMacro = mac.getParamScriptMacros().iterator();
            while (itMacro.hasNext()) {
                ParamScriptMacro paramSc = itMacro.next();
                Hibernate.initialize(paramSc.getScriptHasParameters().getParameters());
            }
        }
        session.close();
    }

    public ArrayList<Script> getMacros() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("from Script sc where sc.isMacro=1");
        List l = qry.list();
        ArrayList<Script> macros = (ArrayList) l;
        session.close();
        return macros;
    }
}
