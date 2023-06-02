package app.utilidades;

import java.util.Date;

public  class Constantes {
	public static Integer NUM_MAX_DATATABLE=10;

	public static int gestionActual() {
		  Date dt=new Date();
	        int year=dt.getYear();
	        System.out.println("Year for date object is : "+year);
	        int current_Year=year+1900;
	        System.out.println("Current year is : "+current_Year);
	        return year;
	}
		
}
