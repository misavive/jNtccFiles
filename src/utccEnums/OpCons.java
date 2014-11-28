package utccEnums;

/**
 * Operadores de Constraint
 * Clase que facilita la implementacion de operadores de Constraint.
 * @author  Misael Viveros Castro
 *
 */

import JaCoP.constraints.PrimitiveConstraint;
import JaCoP.constraints.XeqC;
import JaCoP.constraints.XeqY;
import JaCoP.core.IntVar;

public class OpCons extends OpconstGen<PrimitiveConstraint> {

	@Override
	public PrimitiveConstraint andC(PrimitiveConstraint... cons) {
		for (@SuppressWarnings("unused") PrimitiveConstraint c : cons) {
			
		}
		return null;
	}

	@Override
	public PrimitiveConstraint orC(PrimitiveConstraint... Const) {
		return null;
	}

    /**
     * aSingVarC permite establecerle un valor (Constante) a una variable XeqC
     * @param var
     * @param numC
     */

    public static PrimitiveConstraint aSingVarC(IntVar var, int numC) {
                return new XeqC(var,numC);
    }

    /**
     * aSingVarC permite establecerle el valor una variable IntVar a otra Variable XeqY
     * @param var1  --> JaCop - IntVar
     * @param var2  --> JaCop - IntVar
     * @return new PrimitiveConstraint  --> JaCop -
     */
    public static PrimitiveConstraint aSingVarC(IntVar var1, IntVar var2) {
        return new XeqY(var1,var2);
    }


}
