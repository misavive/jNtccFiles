package utcclang;

/**
 * Clase que implementa la instrucion de Ask de Ntcc
 * @author Misael viveros castro
 */

import utccEnums.EjecutionProc;
import utccEnums.NameProce;

public abstract class AskGen<C> extends Procesos {
		private C c;
		private Procesos askproce;
		
		
		
		AskGen (C cons, Procesos p){
			this.setC(cons); this.setProcess(p);
			this.setName(NameProce._ASK,EjecutionProc._CREATE,this);
		}
	
	/**
	 *  
	 * @param c the c to set
	 */
	public void setC(C c) {
		this.c = c;
	}
	/**
	 * 
	 * @return the c
	 */
	public C getC() {
		return c;
	}
	/**
	 * @param proc the process to set
	 */
	public void setProcess(Procesos proc) {
		this.askproce = proc;
	}
	/**
	 * @return the process
	 */
	public Procesos getProcess() {
		return askproce;
	}
	/* (non-Javadoc)
	 * @see proyectUtcc.Procesos#run()
	 */
	@Override
	public abstract void run();

	
}
