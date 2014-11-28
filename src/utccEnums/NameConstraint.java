package utccEnums;

/**
 * @author  Misael Viveros Castro
 * Enumeracion de nombres de los Contraints, como Constantes.
 * X variable
 * Y Variable
 * C una Constante
 */
public enum NameConstraint {
	XeqC,
    XgtC,
    XgteqC,
    XltC,
    XlteqC,
    XeqY,
    XgtY,
    XgteqY,
    XltY,
	XlteqY,
    XneqC,
    XneqY,
    XmulCeqZ,
    XmulYeqZ,
    XdivYeqZ,
    XmodYeqZ,
    XplusYeqZ,
    And,
    Or
	;

    /**
     *  Metodo que retorna la clase del Constraint
     *  @param name --> NameConstraint
     *  @return String --> Class of Constraint
     */
    public String nameClassC(NameConstraint name) {
              String resultcons = null;
        switch (name)  {

             case XeqC:
                 resultcons = "JaCoP.constraints.XeqC";
                 break;
            case XgtC:
                resultcons =  "JaCoP.constraints.XgtC";
                  break;
            case XgteqC:
                resultcons =  "JaCoP.constraints.XgteqC";
                break;
            case XltC:
                resultcons =  "JaCoP.constraints.XltC";
            break;
            case XneqC:
                resultcons =  "JaCoP.constraints.XneqC";
                break;
            case XlteqC:
                resultcons =  "JaCoP.constraints.XlteqC";
                break;
            case XeqY:
                resultcons =  "JaCoP.constraints.XeqY";
                break;
            case XgtY:
                resultcons =  "JaCoP.constraints.XgtY";
                break;
            case XgteqY:
                resultcons =  "JaCoP.constraints.XgteqY";
                break;
            case XltY:
                resultcons = "JaCoP.constraints.XltY";
                break;
            case XlteqY:
                resultcons =  "JaCoP.constraints.XlteqY";
                break;
            case XneqY:
                resultcons =  "JaCoP.constraints.XneqY";
                break;
            case XmulCeqZ:
                resultcons =  "JaCoP.constraints.XmulCeqZ";
                break;
            case XmulYeqZ:
                resultcons =  "JaCoP.constraints.XmulYeqZ";
                break;
            case XdivYeqZ:
                resultcons =  "JaCoP.constraints.XdivYeqZ";
                break;
            case XmodYeqZ:
                resultcons =  "JaCoP.constraints.XmodYeqZ";
                break;
            case XplusYeqZ:
                resultcons =  "JaCoP.constraints.XplusYeqZ";
                break;
            case And:
                resultcons =  "JaCoP.constraints.And";
                break;
            case Or:
                resultcons =  "JaCoP.constraints.Or";
                break;
        }

        return resultcons;
    }
}

