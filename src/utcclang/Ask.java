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

    // En Ask.java
    @Override
    public void run() {
        synchronized (Planner.lockAsk.get()) {
            while (!Store.validateCons(this.getC()) && !Planner.endWaitAsk.get()) {
                try {
                    Planner.lockAsk.get().wait();
                } catch (Exception e) {
                    System.out.println("Error" + e);
                } finally {
                    // Notificamos al verificador que ya revisamos la guarda,
                    // PERO NO ponemos _FINISH todavía porque seguimos vivos en el while
                    synchronized (Planner.syncroAskValid.get()) {
                        Planner.syncroAskValid.get().notifyAll();
                    }
                }
            }
        }
        if (Store.validateCons(this.getC())) {
            System.out.println(" Ask --Constraint validado Ejecutando Proceso ---");
            RegistryPro.ChangeState(this.getProcess().getIdProc(), EjecutionProc._RUNNING);
            Planner.addServiceExec(this.getProcess());
        }
        // Recién aquí, cuando ya encoló al hijo (o cuando endWaitAsk lo liberó), el Ask termina:
        RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
        synchronized (Planner.syncroAskValid.get()) {
            Planner.syncroAskValid.get().notifyAll();
        }
    }

}
