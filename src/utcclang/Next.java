package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;

/**
 *@author  Misael Viveros Castro
 *26/05/2011
 */


public class Next extends Procesos {
			private Procesos p;
	
	public Next(Procesos pro) {
		this.setP(pro);
		this.setName(NameProce._NEXT,EjecutionProc._CREATE);
	}
	

	/**
	 * @param p the p to set
	 */
	private void setP(Procesos p) {
		this.p = p;
	}

	/**
	 * @return the p
	 */
	public Procesos getP() {
		return p;
	}
	
	/* (non-Javadoc)
	 * @see proyectUtcc.Procesos#run()
	 */
	@Override
	public void run() {
		synchronized (this) {
			try {
				//System.out.println(" Iniciando ---> "+ "Proce Name "+ this.toString() + " idProce " + this.getIdProc());
				RegistryPro.ChangeState(this.getP().getIdProc(), EjecutionProc._CREATE);
				Residual.addresiduaLast(this.getP());
			} finally {
		        RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
		        synchronized (Planner.syncroValid.get()) {
                    Planner.syncroValid.get().notify(); };
			}
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
