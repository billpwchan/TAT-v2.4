package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import engine.Result;

import java.util.ArrayList;
import java.util.HashMap;

public class stopIEC104Simulator implements InterfaceScript {
    @Override
    public Result run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        if (IEC104InitConnection.process != null && IEC104InitConnection.process.isAlive()) {
            IEC104InitConnection.process.destroy();
        }
        Thread.sleep(500);
        if (IEC104InitConnection.process.isAlive()) {
            IEC104InitConnection.process.destroyForcibly();
        }
        IEC104InitConnection.process = null;
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public ArrayList<Parameters> parameters() {
        return null;
    }

    @Override
    public Script scriptInfos() {
        return null;
    }
}
