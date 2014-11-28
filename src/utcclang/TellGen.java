package utcclang;

/**
 * Clase que Permite Agregar un constraint al store
 * @author  Misael Viveros Castro
 * @version 1.0
 */
import utccEnums.*;

public abstract class TellGen<S,C> extends Procesos {
	private C constr;
	private volatile S storetmp;
	public volatile  int locknum;

    TellGen(C c) {
		this.setConstr(c);
		this.setName(NameProce._TELL,EjecutionProc._CREATE);
	}
	
		/**
	 * @param constr the constr to set
	 */
	private void setConstr(C constr) {
		this.constr = constr;
	}

	/**
	 * @return the constr
	 */
	public C getConstr() {
		return constr;
	}

	/* (non-Javadoc)
	 * @see proyectUtcc.Procesos#run()
	 */
	@Override
	public abstract void run();

	/**
	 * @param storetmp the storetmp to set
	 */
	public void setStore(S storetmp) {
		this.storetmp = storetmp;
	}

	/**
	 * @return the storetmp
	 */
	
	public S getStore() {
		return  storetmp;
	}

}
