/**
 * @author  Misael Viveros Castro
 * Clase que Implementa el formato de Constraint a recibir del Entorno
 * @param <C>
 * @param <I>
 */
package utcclang;


public abstract class AmbientCGen<C,I> {
	private String varname1 = null;
    private String varname2 = null;
    private String varname3 = null;
    public Integer varConst;
    private Integer unitTime;
    private I paramvarx;
    private I paramvary;
    private I paramvarz;
    private C constraint;



    public abstract void addAmbientCon( String parvarn1, Integer parvarn2, Integer unit, C constr);
	
	public abstract void addAmbientCon(  String parvarn1, String parvarn2, Integer unit, C constr);

    public abstract void addAmbientCon(  String parvarn1, String parvarn2, String parvarn3, Integer unit, C constr);

    public abstract void addAmbientCon(  String parvarn1, Integer parvarn2, String parvarn3, Integer unit, C constr);

    public String getVarname1() {
        return varname1;
    }

    public void setVarname1(String varname1) {
        this.varname1 = varname1;
    }

    public String getVarname2() {
        return varname2;
    }

    public void setVarname2(String varname2) {
        this.varname2 = varname2;
    }

    public String getVarname3() {
        return varname3;
    }

    public void setVarname3(String varname3) {
        this.varname3 = varname3;
    }

    public Integer getVarConst() {
        return varConst;
    }

    public void setVarConst(Integer varConst) {
        this.varConst = varConst;
    }

    public Integer getUnitTime() {
        return unitTime;
    }

    public void setUnitTime(Integer unitTime) {
        this.unitTime = unitTime;
    }

    public I getParamvarx() {
        return (I) paramvarx;
    }

    public void setParamvarx(I paramvarx) {
        this.paramvarx = paramvarx;
    }

    public I getParamvary() {
        return (I) paramvary;
    }

    public void setParamvary(I paramvary) {
        this.paramvary = paramvary;
    }

    public I getParamvarz() {
        return (I) paramvarz;
    }

    public void setParamvarz(I paramvarz) {
        this.paramvarz = paramvarz;
    }

    public C getConstraint() {
        return constraint;
    }

    public void setConstraint(C constraint) {
        this.constraint = constraint;
    }
}
