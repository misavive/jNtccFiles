package utcclang;
/**
 * @author  Misael Viveros Castro
 *
 */

import JaCoP.constraints.Constraint;
import utccEnums.EjecutionProc;
import utccEnums.LocksC;


public class AskChoice extends AskGen<Constraint> {

	    
	  public AskChoice(Constraint cons, Procesos p) {
		super(cons, p);
	  }

	@Override
	public  void run() {
		synchronized (this){
					//System.out.println(" -------------AskChoice Notyficando --- " + this.getIdProc() + " Proce " + this.toString() + " Notyficando al Choice " + LocksC.getIdLock(this.getIdlock()).getNameClass());

                   // enviando senal

               LocksC.getIdLock(this.getIdlock()).getCountDownLock().countDown();
               //System.out.println("========== >>>>> Senal enviada al CountDown " + LocksC.getIdLock(this.getIdlock()).getCountDownLock().getCount() );
		}
		synchronized (LocksC.getStarObject(this.getIdlock())) {
				while(!LocksC.getStar(this.getIdlock())){
					try {
						//System.out.println(" -------------AskChoice Esperando Autorizacion de Ejecucion--- "+ this.getIdProc()+ " Proce Name "+ this.toString());
						LocksC.getStarObject(this.getIdlock()).wait();
					}catch (Exception e) {System.out.println("Error"+ e);}
					
				}
		}
        //System.out.println(" -------------AskChoice Esperando validar constraint ");
		synchronized (Planner.lockAsk.get()) {
				while(!Store.validateCons(this.getC()) && !Planner.endWaitAsk.get()) {
                    try {
                       // System.out.println(" AskChoice Wait--- " + this.getIdProc() + " Proce Name " + this.toString());
                        Planner.lockAsk.get().wait();
                    } catch (Exception e) {
                        System.out.println("Error" + e);
                    } finally {
                        RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
                        synchronized (Planner.syncroAskValid.get()) { Planner.syncroAskValid.get().notifyAll(); } ; }
                }
				
				
			
		}
		synchronized (LocksC.getObject(this.getIdlock())) {
					//LocksC.LockStruc lockname = LocksC.getIdLock(this.getIdlock());
               if (Store.getStore().validateCons(this.getC())) {
					if (!LocksC.getYeslock(this.getIdlock()) ) {
						LocksC.changeLock(this.getIdlock());
						System.out.println(" AskChoice --Constraint validado Ejecutando x Proceso ---" + this.toString() + " Del Choice -->" + LocksC.getIdLock(this.getIdlock()).getNameClass()); //+ lockname.getNameClass()
						RegistryPro.ChangeState(this.getProcess().getIdProc(), EjecutionProc._RUNNING);
						Planner.addServiceExec(this.getProcess());
									
					}else {
						//System.out.println(" AskChoice --Constraint No Ejecutado x Proceso ---"+ this.toString()  +" Del Choice -->" + LocksC.getIdLock(this.getIdlock()).getNameClass());  //+ lockname.getNameClass()
					}
               } else {
                       // System.out.println(" AskChoice -- Liberado de las Guardas (No Ejecutado)");
               }
		}

				// RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
		//	}// synchro Planner
		//}// synchro Locks
		
		
		synchronized (Planner.syncroAskValid.get()) {RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
            Planner.syncroAskValid.get().notifyAll(); };
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	

}
