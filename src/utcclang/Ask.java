package utcclang;
/**
 * @author  Misael Viveros Castro
 *
 */


import JaCoP.constraints.Constraint;
import utccEnums.EjecutionProc;

public class Ask extends AskGen<Constraint> {

	public Ask(Constraint cons, Procesos p) {
		super(cons, p);
	}

	@Override
	public  void run() {
		synchronized (Planner.lockAsk.get()) {
			while(!Store.validateCons(this.getC()) && !Planner.endWaitAsk.get())
				try {
					//System.out.println(" Ask Wait--- "+ this.getIdProc()+ " Proce Name "+ this.toString());
					Planner.lockAsk.get().wait();
					}catch (Exception e) {System.out.println("Error"+ e);}
				finally {
					RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
					synchronized (Planner.syncroAskValid.get()) {
                        Planner.syncroAskValid.get().notify(); };
				}
		}
		if (Store.validateCons(this.getC())){
				System.out.println(" Ask --Constraint validado Ejecutando Proceso ---");
				RegistryPro.ChangeState(this.getProcess().getIdProc(), EjecutionProc._RUNNING);
				Planner.addServiceExec(this.getProcess());
				
        }  else {
            //System.out.println(" Ask -- No validado Terminando Ejecucion ---");
        }
				synchronized (Planner.syncroAskValid.get()) {RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
                    Planner.syncroAskValid.get().notifyAll(); };


        try {
            this.finalize();

        } catch (Throwable e) {
            e.printStackTrace();
        }
	}

}
