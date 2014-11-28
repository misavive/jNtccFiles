package utcclang;

/**
 * Implementacion de AskGen en JaCop Constraint
 * @author  Misael Viveros Castro
 *
 */


import utccEnums.EjecutionProc;


public class Local extends LocalGen<Store,String> {

	public Local(String var,Procesos proc) {
		super(var,proc);
	}



    @Override
    public void creaVarLocal() {
        Store.getStore().creavars("_"+this.getVar());   //Le agrega un Prefijo _ que indica que  la variable es local.
    }


    @Override
	public void run() {
		synchronized (this) {
				try {
					 System.out.println(" Iniciando ---> "+ this.getIdProc()+ "Proce Name"+ this.toString());
                     this.creaVarLocal();
					 RegistryPro.ChangeState(this.getProc().getIdProc(), EjecutionProc._RUNNING);
					 Planner.addServiceExec(this.getProc());
				
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
