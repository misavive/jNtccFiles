package utcclang;
/**
 * Implementacion de la clase TellGen
 * @author  Misael Viveros Castro
 *
 */


import JaCoP.constraints.Constraint;
import utccEnums.EjecutionProc;
import utccEnums.LocksC;


public class Tell extends TellGen<Store, Constraint> {

	public Tell(Constraint c) {
		super(c);
        this.locknum = LocksC.getlockStruc(this.toString());
	}

	@Override
	public void run() {
		synchronized (this) {
			try {
				//System.out.println("--Ejecutando Tell---"+ this.getClass().getName());
                LocksC.SetCountlock(this.locknum, 1);
		        Store.addConstra(super.getConstr(),this.locknum) ;

            } finally {
				RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
				synchronized (Planner.syncroValid.get()) {
                    Planner.syncroValid.get().notifyAll(); };
			}
		}

        try {
           //System.out.println("--Esperando Respuesta del Motor de Constrain ---"+ this.getClass().getName());
            LocksC.getIdLock(this.locknum).getCountDownLock().await();
       } catch (InterruptedException e) {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
       }
        synchronized (Planner.syncroValid.get()) {RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
            Planner.syncroValid.get().notify(); };
        try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}

}
