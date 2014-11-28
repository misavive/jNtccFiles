package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;
import java.util.Random;

/**
 * @author  Misael Viveros Castro
 *27/05/2011
 */





public class Star extends BangGen {
		private static int ntime=1;
        private static int unitInit =1;

    /**
     * Star acotado con ejecucion eventualmente (randomicamente) entre 1 y el numero de unidades de a ejecutarse (m)
     *
     */
    public Star(Procesos p) {
       int finalnum = Planner.getTimeUnit(),inicio = 1;
        Random rnd = new Random();
        this.setUnitInit(rnd.nextInt(finalnum-inicio+1)+inicio);// rnd.nextInt(HASTA-DESDE+1)+DESDE
        this.setProce(p);
        this.setName(NameProce._BANG, EjecutionProc._CREATE,this);
    }

    /**
     * Star acotado con ejecucion eventualmente entre n y m    ---> inevitablemente se ejecutara en algun instante de tiempo entre n y m
     * @param p  --> Proceso a ejecutar
     * @param unidadIni --> a partir de la unidad m
     * @param ftime --> final de la unidad
     */
     public Star(Procesos p, final int unidadIni, final int ftime) {
         this.setProce(p);
         int finalnum = ftime,inicio = unidadIni;
         Random rnd = new Random();
         this.setUnitInit(rnd.nextInt(finalnum-inicio+1)+inicio);  // eventualmente == inevitablemente se ejecutara en algun instante de tiempo
         this.setName(NameProce._BANG, EjecutionProc._CREATE,this);
     }



	/* (non-Javadoc)
	 * @see proyectUtcc.Procesos#run()
	 */
	@Override
	public void run() {
		
		synchronized (this) {
				try {

                    if (Planner.getTimeUnit() == this.getUnitInit()) {
                            System.out.println(" Iniciando Star ---> "+ this.getIdProc()+ " Proce Name "+ this.toString() + " Unidad Ejecucion" +this.getUnitInit());
                            RegistryPro.ChangeState(this.getProce().getIdProc(), EjecutionProc._RUNNING);
                            Planner.addServiceExec(this.getProce());
                            System.out.println(" Fin Star ---> "+ this.toString() + " Unidad Ejecucion " +this.getUnitInit());
                        }else {
                           Residual.addresiduaLast(this);
                           System.out.println(" Star Esperando  ---> " + this.toString() + "-" + this.getUnitInit());
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
		Star.ntime = ntime;
	}

    public static int getUnitInit() {
        return unitInit;
    }

    public static void setUnitInit(int unitInit) {
        Star.unitInit = unitInit;
    }

}
