package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.LocksC;

/**
 * Clase que implementa la instrucion de Choice de Ntcc
 * @author  Misael Viveros Castro
 *
 */
public class ChoicePar extends ChoiceGen<Par> {


	/**
	 *
	 */
	public ChoicePar(Par... asks) {
		this.setAsk(asks);
		this.locknum = LocksC.getlockStruc(this.toString());
	}
	
	public static ChoicePar getChoice() {
		return new ChoicePar();
	}

	/* (non-Javadoc)
	 * @see Procesos#run()
	 */
	
	@Override
	public  void run() {
			synchronized (this) {
					System.out.println(" Iniciando ---> "+ this.getIdProc()+ " Proce Name "+ this.toString());
					LocksC.SetCountlock(this.locknum, this.getAsk().length);
			        for (Procesos ask : this.getAsk()) {
			            try {
			            	ask.setIdlock(this.locknum);
			            	System.out.println(" (Choice Paralelo)..  Ejecutando el Proceso No.---> "+ ask.getIdProc()+ " Proce Name "+ ask.toString());
			            	RegistryPro.ChangeState(ask.getIdProc(), EjecutionProc._RUNNING);
			            	Planner.addServiceExec(ask);
			            } catch (Exception e) { System.out.print("Error"+e); }
			            
			        }
			        System.out.println(" (Choice Paralelo).. Locknum (numero del Lock) --------->" + this.locknum + " Size List Procesos " + this.getAsk().length );
			        System.out.println("(Esperando Terminacion de ejecucion Procesos" + LocksC.getCountlock(this.locknum).getCount());

                    try {
                        LocksC.getIdLock(this.locknum).getCountDownLock().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

	  		 
	        
				      	System.out.println(" (Choice Paralelo)...                :) Notyficando Los Procesos Sincronizados Para su ejecucion ---->" );
				        LocksC.StarteLock(this.locknum); // indica que los procesos sincronizados pueden ejecutarse
			  	  }     
				    synchronized (LocksC.getStarObject(this.locknum)) {LocksC.getStarObject(this.locknum).notifyAll(); };
                    // verificar esta instruccion //System.out.print("Validacion --- > Estado de las guardas de AskChoice " + LocksC.getStar(this.locknum));
			        synchronized (Planner.syncroValid.get()) {RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
                        Planner.syncroValid.get().notifyAll(); };
			        
			        try {
						this.finalize();
					} catch (Throwable e) {
						e.printStackTrace();
					}
		//}
	}
	

}
