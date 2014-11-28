package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;

/**
 * @author  Misael Viveros Castro
 *
 * @param <A>
 */

public abstract class ChoiceGen<A> extends Procesos {
    /**
     * A lista de los AskChoice a ejecutar.
     */
		private A[] ask;
    /**
     * Locknum idicador del numero de indice que correspode al objecto del bloqueo en el arreglo de Locks
     */
		public  volatile int locknum;
		
	/**
	 * 
	 */
		public ChoiceGen() {
			this.setName(NameProce._CHOICE, EjecutionProc._CREATE);
			//this.locknum = LocksC.geLocksC().getlockStruc(this.getClass().getName());
		}
		

	/* (non-Javadoc)
	 * @see Procesos#run()
	 */
	@Override
	public abstract void run();

	/**
	 * @return the ask
	 */
	public A[] getAsk() {
		return ask;
	}

	/**
	 * @param ask the ask to set
	 */
	public void setAsk(@SuppressWarnings("unchecked") A... ask) {
		this.ask = ask;
	}


}
