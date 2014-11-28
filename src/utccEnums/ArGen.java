package utccEnums;

/**
 * Created by misaelviveroscastro on 8/05/14.
 * Clase Argen utilizada para tranferir los contraint en forma de arreglo
 * Es utilizada por la Expresion condicional  And.
 */
public abstract class ArGen<C> {
    C[] cons;

    public C[] getC() {
        return cons;
    }

    public void setC(C[] c) {
        this.cons = c;
    }


    //public static C[] expre(C... cons) {

    //    return cons ;
    //}

}
