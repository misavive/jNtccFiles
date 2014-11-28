package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;
import utccEnums.Operation;

/**
 * Change permite cambiar el valor de una variable en la siguiente unidad de tiempo
 * @author Misael Viveros Castro 18/05/2012
 * 
 */
public abstract class ChangeGen<I> extends Procesos {
    private I varX; private I varY;
    public int value;
    public Operation op; public int oc = 0; // opcion a aplicar


    public I getVarX() {
        return varX;
    }


    public I getVarY() {
        return varY;
    }

	/**
	 *
	 */
	ChangeGen(I varnameX, I varnameY) {
        this.varX = varnameX ; this.varY = varnameY; this.oc = 1;
        this.setName(NameProce._CHANGE, EjecutionProc._CREATE);
    }

    ChangeGen(I varnameX, int value) {
        this.varX = varnameX;  this.value = value; this.oc = 2;
        this.setName(NameProce._CHANGE, EjecutionProc._CREATE);
    }

	ChangeGen(I varnameX, Operation ope, final int value) {
        this.varX = varnameX; this.op = ope; this.value = value; this.oc = 3;
        this.setName(NameProce._CHANGE, EjecutionProc._CREATE);
    }

	@Override
	public abstract void run();

}
