package utccEnums;

/**
 * Created with IntelliJ IDEA.
 * User: misaelviveroscastro
 * Date: 9/11/12
 * Time: 11:23
 * Clase implementa la creacion de Variables en el Store
 * @author  Misael Viveros Castro
 *
 */

public abstract class VarsGen<C> {
    public String varname; // type 1
    public int vinf,vsup; //  type 2
    public int[]  values; //  type 3
    public char vtype; // // identifica el tipo de Variable 1-> variable definida solo el nombre  2--> variable con limites (Superior,Inferior) 3--> variables con rango de Arreglos

    /**
     * Constraint de inicializacion de las varibles del store.
     * Actualizado por la clase Change.
     */
    private C vConstraint;
    /**
    * Indica se inicializo el la variable vConstraint.
    * Actualizado por la clase Change.
    */
    public Boolean iniCons = false;

    public C getConstraint() {
        return vConstraint;
    }

    public void setConstraint(C constraint) {
        iniCons = true ;
        vConstraint = constraint;
    }

    public void delConstraint() {
        iniCons = false ;
        vConstraint = null;
    }


}
