package utccEnums;

/**
 * Operadores de Constraint
 * Clase que facilita la implementacion de operadores de Constraint.
 * @author  Misael Viveros Castro
 *
 */
public abstract class OpconstGen<C> {
	private C cons;
	
	public abstract C andC(C... cons);
	
	public abstract C orC(C... Const);

	/**
	 * @return the cons
	 */
	public C getCons() {
		return cons;
	}

	/**
	 * @param cons the cons to set
	 */
	public void setCons(C cons) {
		this.cons = cons;
	}
}
