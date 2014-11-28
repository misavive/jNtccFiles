package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;

/**
 * Clase que Implementa la Instruccion local de ntcc Language
 * @author Misael Viveros Castro
 * @param <S>    --> La implementacion del store
 * @param <I>     --> Tipo variables enteras a utilizar por el motor de constarint
 * 27/05/2011
 */


public abstract class LocalGen<S,I> extends Procesos {
		private I varname;
		private Procesos proc;
		
    /**
     *
     * @param var
     */
	public LocalGen(I var, Procesos proc) {
		this.setVar(var);
		this.setProc(proc);
		this.setName(NameProce._LOCAL, EjecutionProc._CREATE);
	}

	/**
	 * @param varname the varname to set
	 */
	private void setVar(I varname) {
		this.varname = varname;
	}

	/**
	 * @return the varname
	 */
	public I getVar() {
		return varname;
	}

	/**
	 * @return the proc
	 */
	public Procesos getProc() {
		return proc;
	}

	/**
	 * @param proc the proc to set
	 */
	public void setProc(Procesos proc) {
		this.proc = proc;
	}

	/**
	 * Metodo que implementa la alfa comversion de la variable
     * Nota la alfa conversion de la variable se realiza en el momento de local crear la variable
     * le cambia rl nombre para que sea una variable que solo conoce local
	 */
    public abstract void creaVarLocal();

	@Override
	public abstract void run();
 

}
