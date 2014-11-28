package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;

/**
 * @author  Misael Viveros Castro
 *27/05/2011
 */





public class BangP extends BangGen {
		private static int ntime=1;
	/**
	 * 
	 */
	public BangP(Procesos p, final int ntime) {
		this.setProce(p);
		this.setNtime(ntime);
		this.setName(NameProce._BANG, EjecutionProc._CREATE);
	}


	/* (non-Javadoc)
	 * @see proyectUtcc.Procesos#run()
	 */
	@Override
	public void run() {
		
		synchronized (this) {
				try {
					if (this.getNtime() > 0) {
						//System.out.println(" Iniciando ---> "+ this.getIdProc()+ " Proce Name "+ this.toString() + " Unidad Ejecucion" +this.getNtime());
						BangP bproce = new BangP(this.getProce(),this.getNtime()-1);
				        Residual.addresiduaLast(bproce);
				        RegistryPro.ChangeState(this.getProce().getIdProc(), EjecutionProc._RUNNING);
				        Planner.addServiceExec(this.getProce());
				        
					}else {
						//System.out.println(" Fin de las unidades de Bang ---> "+ this.toString() + " Unidad Ejecucion " +this.getNtime());
					}
							
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

	/**
	 * @return the ntime
	 */
	public int getNtime() {
		return ntime;
	}

	/**
	 * @param ntime the ntime to set
	 */
	public void setNtime(int ntime) {
		BangP.ntime = ntime;
	}

}
