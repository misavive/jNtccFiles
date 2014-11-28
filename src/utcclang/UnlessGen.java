package utcclang;
import utccEnums.*;

/**
 *@author misael
 *27/05/2011
 */

public abstract class UnlessGen<C> extends Procesos {
	 private C c; 
	 private Procesos proc;
	
	/**
	 * 
	 */
	public UnlessGen(C cons, Procesos p ) {
		this.setC(cons); this.setProcess(p);
		this.setName(NameProce._UNLESS, EjecutionProc._CREATE);
	}

	/**
	 * @param c the c to set
	 */
	private void setC(C c) {
		this.c = c;
	}

	/**
	 * @return the c
	 */
	public C getC() {
		return c;
	}

	/**
	 * @param proc the proc to set
	 */
	private void setProcess(Procesos proc) {
		this.proc = proc;
	}

	/**
	 * @return the proc
	 */
	public Procesos getProcess() {
		return proc;
	}
	
	/* (non-Javadoc)
	 * @see proyectUtcc.Procesos#run()
	 */
	@Override
	public abstract void run();

	

}
