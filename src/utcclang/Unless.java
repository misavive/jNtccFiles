package utcclang;

/**
 * Implementacion de Unless en JaCop Constraint System
 * @author  Misael Viveros Castro
 *
 */

import JaCoP.constraints.Constraint;
import utccEnums.EjecutionProc;

public class Unless extends UnlessGen<Constraint> {

	public Unless(Constraint cons, Procesos p) {
		super(cons, p);
	}

	@Override
	public void run() {
		System.out.println("--Unless esperando permiso --- " + this.getClass().toString() + "IdProce " + this.getIdProc());
		synchronized (Planner.lockUnl.get()) {
			while(!Planner.runUnless.get())
				try {
					Planner.lockUnl.get().wait();
					}catch (Exception e) {System.out.println("Error"+ e);}
		}
		
		try {
			System.out.println("--Unless Validando Constraint ---");
			if(!Store.validateCons(this.getC())) {
				System.out.println("** Ejecutando Proceso Unless---");
				RegistryPro.ChangeState(this.getProcess().getIdProc(), EjecutionProc._CREATE);
				Residual.addresiduaLast(this.getProcess());
			}
			
			
			synchronized (Planner.syncroUnlValid.get()) {RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
                Planner.syncroUnlValid.get().notifyAll(); };
            System.out.println("** Finalizando Proceso Unless--- " + this.getClass().toString()  );

			try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} finally {
			RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
			synchronized (Planner.syncroUnlValid.get()) {
                Planner.syncroUnlValid.get().notify(); };
		}
	}

}
