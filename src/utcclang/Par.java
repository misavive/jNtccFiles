package utcclang;

/**
 * @author  Misael Viveros Castro
 *26/05/2011
 */

import utccEnums.EjecutionProc;
import utccEnums.NameProce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Par extends Procesos {
	private List<Procesos> proc = Collections.synchronizedList(new ArrayList<Procesos>()) ;

    public Par(Procesos... process) {
        for (Procesos p : process) {
            proc.add(p);
        }
        this.setName(NameProce._PAR,EjecutionProc._CREATE);
    }


    @Override
	public void run() {
    	 //System.out.println(" Iniciando ---> "+ this.getIdProc()+ " Proce Name "+ this.toString());
    	 int i = 1;
    	 synchronized (this) {
	        for (Procesos p : this.proc) {
	            try {
	            	//System.out.println(" (Par)..  Ejecutando el Proceso No.---> "+ i +" IdProc" + p.getIdProc()+ " Proce Name "+ p.toString());
	            	RegistryPro.ChangeState(p.getIdProc(), EjecutionProc._RUNNING);
	            	Planner.addServiceExec(p);
	            } catch (Exception e) { System.out.print("Error"+e);
	            }
	            finally {
	            	RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
	            	synchronized (Planner.syncroValid.get()) {
                        Planner.syncroValid.get().notify(); };
	            }
	            i++;
	        }
    	 }
            Collections.shuffle(this.proc);
	        synchronized (Planner.syncroValid.get()) {RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
                Planner.syncroValid.get().notifyAll(); };
	        try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
    }

}


