package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;

/**
 * @author  Misael Viveros Castro
 *27/05/2011
 */





public class Bang extends BangGen {
	/**
	 * 
	 */
	public Bang(Procesos p) {
		this.setProce(p);
		this.setName(NameProce._BANG, EjecutionProc._CREATE,this);
	}


	/* (non-Javadoc)
	 * @see proyectUtcc.Procesos#run()
	 */
	@Override
	public void run() {
		synchronized (this) {
				try {
					//System.out.println(" Iniciando ---> "+ this.getIdProc()+ " Proce Name "+ this.toString());
					//Bang bproce = new Bang(this.getProce());
			        Residual.addresiduaLast(this);
			        RegistryPro.ChangeState(this.getProce().getIdProc(), EjecutionProc._RUNNING);
			        Planner.addServiceExec(this.getProce());
				} finally {
					RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
					synchronized (Planner.syncroValid.get()) {
                        Planner.syncroValid.get().notifyAll(); };
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
