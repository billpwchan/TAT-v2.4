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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

    /**
     *
     */
    public MacroDB() {

    }

    /**
     *
     * @param macro
     */
    public void getAllFromMacro(Script macro) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        try {
            session.update(macro);
        } catch (Exception e) {
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

    public void deleteMacro(Script macro) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        session.delete(macro);
        Set<Macro> deleteMacros = new HashSet<>(macro.getMacrosForScriptIdScript()); //Set of IDs.
        for (Macro deleteMacro : deleteMacros){
            deletePSMGivenMacroId(deleteMacro);
            deleteMacroGivenMacroId(deleteMacro.getIdmacro());
//            removePSMGivenId(deleteMacro.getIdmacro());
//            session.delete(deleteMacro);
        }
        session.beginTransaction().commit();
        session.close();
    }
    
    private void deleteMacroGivenMacroId(int macroId){
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("delete from Macro mc where mc.idmacro=:macro_id");
        qry.setInteger("macro_id", macroId);
        qry.executeUpdate();
        session.beginTransaction().commit();
        session.close();
    }
    
    public void deletePSMGivenMacroId(Macro macro){
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("delete from ParamScriptMacro psm where psm.macro=:macro_id");
        qry.setParameter("macro_id", macro);
        qry.executeUpdate();
        session.beginTransaction().commit();
        session.close();
    }

    /**
     *
     * @return
     */
    public ArrayList<Script> getMacros() {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("from Script sc where sc.isMacro=1");
        List l = qry.list();
        ArrayList<Script> macros = (ArrayList) l;
        session.close();
        return macros;
    }

    //Assumption of this function: A record is deleted on a reversed order.
    /**
     *
     * @param macroID
     */
    public void removePSMGivenId(int macroID) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("delete from ParamScriptMacro psm where psm.paramScriptMacrocol=:macro_id");
        qry.setInteger("macro_id", macroID); //Remove based on PSM ID.
        qry.executeUpdate();
        session.beginTransaction().commit();
        session.close();

    }

    /**
     *
     * @param macroID
     */
    public void makeDuplicateParamScriptMacro(int macroID) {
        SessionFactory factory = sessionFactorySingleton.getInstance();
        Session session = factory.openSession();
        Query qry = session.createQuery("from ParamScriptMacro psm where psm.macro=:macro_id");
        qry.setInteger("macro_id", macroID);
        List l = qry.list();
        ArrayList<ParamScriptMacro> macros = (ArrayList) l;

        System.out.println(macros.size());

        for (ParamScriptMacro PSM : macros) {
            System.out.println("Entered");
            qry = session.createQuery("select count(*) from ParamScriptMacro as PSM");
            long count = (long) qry.uniqueResult();
            count++;
            System.out.println("Current database records: " + count);
            qry = session.createSQLQuery("INSERT INTO param_script_macro (param_script_macrocol, macro_idmacro, param_script_macro_param_script_macrocol, script_has_parameters_idscript_has_parameters, to_display, value_path, value, param_order) values (:inParamScriptMacrocol, :inMacro, :inParamScriptMacro, :inScriptHasParameters, :inToDisplay, :inValuePath, :inValue, :inParameterOrder)");
//            qry.setParameter("inParamScriptMacrocol", 0);       //Should generae a new unique id by default.
            qry.setParameter("inMacro", PSM.getMacro().getIdmacro());
            System.out.println("Curent IdMacro: " + PSM.getMacro().getIdmacro());
            qry.setParameter("inParamScriptMacro", PSM.getParamScriptMacro());
            qry.setParameter("inScriptHasParameters", PSM.getScriptHasParameters());
            qry.setParameter("inToDisplay", PSM.getToDisplay());
            qry.setParameter("inValuePath", PSM.getValuePath());
            qry.setParameter("inValue", PSM.getValue());
            qry.setParameter("inParameterOrder", PSM.getParamOrder());
            System.out.println("currentParameterorder: " + PSM.getParamOrder());
            qry.executeUpdate();
            session.beginTransaction().commit();

        }
        session.close();

    }
}
