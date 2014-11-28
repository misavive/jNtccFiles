package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;

/**
 * @author  Misael Viveros Castro
 *27/05/2011
 */





public class BangPn extends BangGen {
		private static int ntime=1;
        private static int unitInit =1;
        private static int op=0;
    /**
     * Bang acotado con ejecucion n veces
     * @param p  --> Proceso a ejecutar
     * @param ntime  --> numero de veces a ejecutarse
     */
	public BangPn(Procesos p, final int ntime) {
		this.setProce(p);
		this.setNtime(ntime);
		this.setName(NameProce._BANG, EjecutionProc._CREATE,this);
        this.op =1;
	}

    /**
     * Bang acotado con ejecucion desde la unidad m hasta n veces
     * @param p  --> Proceso a ejecutar
     * @param unidadIni --> a partir de la unidad m
     * @param ntime --> numero de veces a ejecutarse
     */
     public BangPn(Procesos p, final int unidadIni ,final int ntime) {
         this.setProce(p);
         this.setNtime(ntime);
         this.setUnitInit(unidadIni);
         this.setName(NameProce._BANG, EjecutionProc._CREATE,this);
         this.op=2;
     }



	/* (non-Javadoc)
	 * @see proyectUtcc.Procesos#run()
	 */
	@Override
	public void run() {
		
		synchronized (this) {
				try {
                    switch (this.op) {
                       case 1:
                            if (this.getNtime() > 0) {
                                //System.out.println(" Iniciando ---> "+ this.getIdProc()+ " Proce Name "+ this.toString() + " Unidad Ejecucion" +this.getNtime());
                                BangPn bproce = new BangPn(this.getProce(),this.getNtime()-1);
                                Residual.addresiduaLast(bproce);
                                RegistryPro.ChangeState(this.getProce().getIdProc(), EjecutionProc._RUNNING);
                                Planner.addServiceExec(this.getProce());

                            }else {
                                //System.out.println(" Fin de las unidades de BangPn ---> "+ this.toString() + " Unidad Ejecucion " +this.getNtime());
                            }
                            break;
                        case 2:
                            if (Planner.getTimeUnit() >= this.getUnitInit()) {
                                if (this.getNtime() > 0) {
                                    System.out.println(" Iniciando ---> "+ this.getIdProc()+ " Proce Name "+ this.toString() + " Unidad Ejecucion" +this.getNtime());
                                    BangPn bproce = new BangPn(this.getProce(),this.getUnitInit(),this.getNtime()-1);
                                    Residual.addresiduaLast(bproce);
                                    RegistryPro.ChangeState(this.getProce().getIdProc(), EjecutionProc._RUNNING);
                                    Planner.addServiceExec(this.getProce());
                                }else {
                                    System.out.println(" Fin de las unidades de BangPn ---> "+ this.toString() + " Unidad Ejecucion " +this.getNtime());
                                }

                            }else {
                                Residual.addresiduaLast(this);
                               // System.out.println(" BangPn Esperando A ejecutarse  ---> "+ this.toString() + "En La Unidad Ejecucion " + this.getUnitInit());
                            }
                            break;
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
		BangPn.ntime = ntime;
	}

    public static int getUnitInit() {
        return unitInit;
    }

    public static void setUnitInit(int unitInit) {
        BangPn.unitInit = unitInit;
    }

}
