package utcclang;

/**
 * Clase que implementa la ejcución sincronizada de procesos.
 * Opera como parametro de ChoicePar.
 *
 * @author  Misael Viveros Castro
 *  28/04/2014
 *
 */

import utccEnums.EjecutionProc;
import utccEnums.LocksC;
import utccEnums.NameProce;


public class Parc extends Procesos {
	private Procesos proc ;

    public Parc(Procesos  process) {

        this.proc = process ;
        this.setName(NameProce._PAR,EjecutionProc._CREATE);
    }


    @Override
	public void run() {
    	    System.out.println(" Iniciando ---> "+ this.getIdProc()+ " Proce Name "+ this.toString());
            synchronized (this){
                System.out.println(" -------------Parc Notyficando --- " + this.getIdProc() + " Proce " + this.toString() + " Notyficando al Choice " + LocksC.getIdLock(this.getIdlock()).getNameClass());

                // enviando senal

                LocksC.getIdLock(this.getIdlock()).getCountDownLock().countDown();
                System.out.println("========== >>>>> Senal enviada al CountDown " + LocksC.getIdLock(this.getIdlock()).getCountDownLock().getCount() );
            }
            synchronized (LocksC.getStarObject(this.getIdlock())) {
                while(!LocksC.getStar(this.getIdlock())){
                    try {
                        System.out.println(" -------------Parc Esperando Autorizacion de Ejecucion--- "+ this.getIdProc()+ " Proce Name "+ this.toString());
                        LocksC.getStarObject(this.getIdlock()).wait();
                    }catch (Exception e) {System.out.println("Error"+ e);}

                }
            }

            synchronized (LocksC.getObject(this.getIdlock())) {

                    if (!LocksC.getYeslock(this.getIdlock()) ) {
                        LocksC.changeLock(this.getIdlock());
                        System.out.println(" Parc -- Ejecutado x Proceso ---" + this.toString() + " Del Choice -->" + LocksC.getIdLock(this.getIdlock()).getNameClass()); //+ lockname.getNameClass()
                        RegistryPro.ChangeState(this.proc.getIdProc(), EjecutionProc._RUNNING);
                        Planner.addServiceExec(this.proc);

                    }else {
                        System.out.println(" Parc -- No Ejecutado x Proceso ---"+ this.toString()  +" Del Choice -->" + LocksC.getIdLock(this.getIdlock()).getNameClass());  //+ lockname.getNameClass()
                    }
            }

    	 synchronized (this) {

	            try {
	            	System.out.println(" (Par)..  Ejecutando el Proceso No.---> " +" IdProc" + this.proc.getIdProc()+ " Proce Name "+ this.proc.toString());
	            	RegistryPro.ChangeState(this.proc.getIdProc(), EjecutionProc._RUNNING);
	            	Planner.addServiceExec(this.proc);
	            } catch (Exception e) { System.out.print("Error"+e);
	            }
	            finally {
	            	RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
	            	synchronized (Planner.syncroValid.get()) {
                        Planner.syncroValid.get().notify(); };
	            }


    	 }
	        
	        synchronized (Planner.syncroValid.get()) {RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
                Planner.syncroValid.get().notifyAll(); };
	        try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
    }

}


