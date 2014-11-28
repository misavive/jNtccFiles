package utcclang;

/**
 *  Clase que implementa el Ambiente en JaCop Constraint
 * @author  Misael Viveros Castro
 * 
 * 
 */


import JaCoP.constraints.Constraint;
import JaCoP.core.IntVar;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import utccEnums.Vars;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class Ambient extends AmbientGen<Store,Constraint> {

    /**
     * Agregar los Constraint al Entorno.
     *
     */
    @Override
    public void addEnviroment(String clasname, String parvarn1, Integer parvarn2, Integer unit) throws ClassNotFoundException, SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            InstantiationException, IllegalAccessException,
            InvocationTargetException {

        Constraint c = converConstrain(clasname,parvarn1,parvarn2);
        AmbientC varsConst = new AmbientC();    //Campo que implementa la clase AmbienC para transformar los constraint
        varsConst.addAmbientCon(parvarn1, parvarn2,unit,c);  //
        super.getVarsEnviromen().add(varsConst);


    }

	@Override
	public void addEnviroment(String clasname, String parvarn1,String parvarn2, Integer unit) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        AmbientC varsConst = new AmbientC();            //Campo que implementa la clase AmbienC para transformar los constraint
        Constraint c = converConstrain(clasname,parvarn1,parvarn2);
		varsConst.addAmbientCon(parvarn1, parvarn2, unit, c);
		super.getVarsEnviromen().add(varsConst);
	}


    @Override
    public void addEnviroment(String clasname, String parvarn1, String parvarn2, String parvarn3, Integer unit) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        AmbientC varsConst = new AmbientC();          //Campo que implementa la clase AmbienC para transformar los constraint
        Constraint c = converConstrain(clasname,parvarn1,parvarn2,parvarn3);
        varsConst.addAmbientCon(parvarn1, parvarn2,parvarn3, unit, c);
        super.getVarsEnviromen().add(varsConst);
    }

    @Override
    public void addEnviroment(String clasname, String parvarn1, Integer parvarn2, String parvarn3, Integer unit) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        AmbientC varsConst = new AmbientC();  //Campo que implementa la clase AmbienC para transformar los constraint
        Constraint c = converConstrain(clasname,parvarn1,parvarn2,parvarn3);
        varsConst.addAmbientCon(parvarn1, parvarn2,parvarn3, unit, c);
        super.getVarsEnviromen().add(varsConst);
    }


    /**
     * Conversion de los Constraint.
     *
     */

    @Override
	public Constraint converConstrain(String classname, String parvarn1, String parvarn2) throws ClassNotFoundException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		IntVar varConverX = new IntVar(); IntVar varConverY = new IntVar(); varConverX.id = parvarn1;varConverY.id = parvarn2; 
		Class<?> clasCon = Class.forName(classname);
		Constructor<?> constru2 = clasCon.getDeclaredConstructor(new Class<?>[] {JaCoP.core.IntVar.class,JaCoP.core.IntVar.class});
		Constraint c = (Constraint) constru2.newInstance(varConverX,varConverY);
		return c;
	}

    @Override
    public Constraint converConstrain(String classname, String parvarn1, Integer parvarn2) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        IntVar varConverX = new IntVar();  varConverX.id = parvarn1;
        Class<?> clasCon = Class.forName(classname);
        System.out.println(clasCon.toString());
        Constructor<?> constru2 = clasCon.getDeclaredConstructor(new Class<?>[] {JaCoP.core.IntVar.class,int.class});
        Constraint c = (Constraint) constru2.newInstance(varConverX,parvarn2);
        return c;
    }


    /**
     * Metodo para convertir un constraint de Strint to C
     *
     * @param classname
     * @param parvarn1
     * @param parvarn2
     * @param parvarn3
     * @return C - Constraint type C
     */
    @Override
    public Constraint converConstrain(String classname, String parvarn1, String parvarn2, String parvarn3) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        IntVar varConverX = new IntVar(); IntVar varConverY = new IntVar();  IntVar varConverZ = new IntVar(); varConverX.id = parvarn1;varConverY.id = parvarn2; varConverZ.id = parvarn3 ;
        Class<?> clasCon = Class.forName(classname);
        Constructor<?> constru2 = clasCon.getDeclaredConstructor(new Class<?>[] {JaCoP.core.IntVar.class,JaCoP.core.IntVar.class, JaCoP.core.IntVar.class});
        Constraint c = (Constraint) constru2.newInstance(varConverX,varConverY, varConverZ);
        return c;
    }

    @Override
    public Constraint converConstrain(String classname, String parvarn1, Integer parvarn2, String parvarn3) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        IntVar varConverX = new IntVar();IntVar varConverY = new IntVar();  varConverX.id = parvarn1; varConverY.id = parvarn3;
        Class<?> clasCon = Class.forName(classname);
        Constructor<?> constru2 = clasCon.getDeclaredConstructor(new Class<?>[] {JaCoP.core.IntVar.class,int.class,JaCoP.core.IntVar.class});
        Constraint c = (Constraint) constru2.newInstance(varConverX,parvarn2,varConverY);
        return c;
    }



    @Override
	public void writerVarsXml(String filename) {
		XStream fileXml = new XStream(); AmbientC varsConst = new AmbientC();
		fileXml.alias("constraintNtcc", varsConst.getClass());
		try {
			FileOutputStream fs = new FileOutputStream(filename);
			fileXml.toXML(super.getVarsEnviromen(), fs);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
	}

	@Override
	public void readerVarsXml(String filename) {
        XStream fileXml = new XStream(new DomDriver()); AmbientC varsConst = new AmbientC();
        fileXml.alias("constraintNtcc", varsConst.getClass());  // estructura del arreglo que contiene los constraint

        File file = new File(filename);

        if(file.exists()){
            try {
                FileInputStream fs = new FileInputStream(file);
                @SuppressWarnings("unchecked")
                ArrayList<AmbientC> restoreEnvi	= (ArrayList<AmbientC>) fileXml.fromXML(fs);
                super.setVarsEnviromen(restoreEnvi);

                System.out.println("Archivo "+fileXml.toString() + "\n Archivo Cargado OK +++ \n (Mvc -Master System Engineer) \n (Avispa Research Group)" );
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }   else {
            System.out.println("Archivo No Encontrado" + file.getAbsolutePath());
        }



	}


    /**
     *
     */
    @Override
    public void addIniC(String var, Constraint cons) {
        synchronized (AmbientGen.getArrayutcc()) {
            for (Vars varis: AmbientGen.getArrayutcc() ){
                if 	(varis.varname.equals(var)) {
                    varis.setConstraint(cons);
                    break;
                }
            }
        }
    }

    /**
     * Permite agregar constraint de iniciacion a una variable.
     * Los constraint de iniciacion se ejecutan antes de la ejecucion de los procesos
     */
    @Override
    public void addStoreConstIni() {
        if (AmbientGen.getArrayutcc().size()>0) {
            synchronized (AmbientGen.getArrayutcc()) {

                for (Vars varis: AmbientGen.getArrayutcc() ){
                    if (varis.iniCons == true) {
                        Store.addConstra(varis.getConstraint());
                        System.out.println("           -> Constraint Cargado Al Store - Variable - "   );
                    }
                }
            }
        }
    }

    /**
	 * Metodo que permite cargar los Constraint  del ambiente al store
	 * valida que las variables exitan en store antes de cargarlas
	 * @param timeUnit - int -> unidad de tiempo para cargar los constraint
	 */
	@Override
	public void addStore(int timeUnit) {
          if (getVarsEnviromen().size() > 0) {
			for (AmbientC amb: getVarsEnviromen() ){
				if (amb.getUnitTime() == timeUnit){
                    int nvar =  super.nvarDef(amb);
                    switch (nvar) {
                      case 1:
                            if (super.findVars(amb.getVarname1()) ) {
                                Store.addConstra(amb.getConstraint());
                                System.out.println("Constraint Cargado  - " + amb.getConstraint().getClass().toString());
                            }
                          break;
                      case 2:
                          if (super.findVars(amb.getVarname1()) && super.findVars(amb.getVarname2())) {
                              Store.addConstra(amb.getConstraint());
                              System.out.println("Constraint Cargado  - " + amb.getConstraint().getClass().toString());
                          }
                          break;
                      case 3:
                          if (super.findVars(amb.getVarname1()) && super.findVars(amb.getVarname2()) && super.findVars(amb.getVarname3()) ) {
                              Store.addConstra(amb.getConstraint());
                              System.out.println("Constraint Cargado  - " + amb.getConstraint().getClass().toString());
                          }
                          break;
                      default: break;
                    }
				}
			}
          } else {
              System.out.println("Ambient- <AddStore> -No existen  Elementos en el Ambiente " );
          }

	}


    /**
     * Proceso  que actualiza el valor de las variables
     *
     *
     */

    public static void updateVars() {
        IntVar tempo;
        for (Vars vars: getArrayutcc() ){
            tempo = (IntVar) Store.getModelStore().findVariable(vars.varname);
            vars.vinf = tempo.value();
        }

    }

	public static Ambient getAmbient() {
		return new Ambient();
	}

	
	
		
}
