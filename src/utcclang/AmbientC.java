/**
 * @author  Misael Viveros Castro
 *  Clase que agrega los constraint al Ambiente.
 */

package utcclang;

import JaCoP.constraints.Constraint;
import JaCoP.core.IntVar;

public class AmbientC extends AmbientCGen<JaCoP.constraints.Constraint, JaCoP.core.IntVar> {



    @Override
    public void addAmbientCon(String parvarn1, Integer parvarn2, Integer unit, Constraint c) {
        IntVar x = new IntVar();x.id=parvarn1;
        super.setVarname1(parvarn1); super.setVarConst(parvarn2); super.setUnitTime(unit);
        super.setParamvarx(x);
        super.setConstraint(c);


    }


    @Override
	public void addAmbientCon(String parvarn1, String parvarn2, Integer unit, Constraint c) {
        IntVar x = new IntVar();IntVar y = new IntVar();x.id=parvarn1;y.id=parvarn2;
        super.setVarname1(parvarn1); super.setVarname2(parvarn2);super.setUnitTime(unit);
        super.setParamvarx(x);super.setParamvary(y);
        super.setConstraint(c);
	}



    @Override
    public void addAmbientCon(String parvarn1, String parvarn2, String parvarn3, Integer unit, Constraint c) {
        IntVar x = new IntVar();IntVar y = new IntVar();IntVar z = new IntVar(); x.id=parvarn1; y.id=parvarn2; z.id=parvarn3;
        super.setVarname1(parvarn1); super.setVarname2(parvarn2);super.setVarname3(parvarn3);super.setUnitTime(unit);
        super.setParamvarx(x);super.setParamvary(y); super.setParamvarz(z);
        super.setConstraint(c);

    }

    @Override
    public void addAmbientCon(String parvarn1, Integer parvarn2, String parvarn3, Integer unit, Constraint c) {
        IntVar x = new IntVar();IntVar y = new IntVar(); x.id=parvarn1; y.id=parvarn3;
        super.setVarname1(parvarn1); super.setVarname2(parvarn3); super.setVarConst(parvarn2);super.setUnitTime(unit);
        super.setParamvarx(x);super.setParamvary(y);
        super.setConstraint(c);
    }


}
