package app.utilidades;




public class pruebas {
	public static void main(String[] args) {
		String path="\\home\\hola\\jaja";
		System.out.println(path);
		path = path.replaceAll("\\\\", "/");
		System.out.println(path);
		
		ALGORITMO3DES_Ecript_Desencript des=new ALGORITMO3DES_Ecript_Desencript();
		try {
			System.out.println("PASSW:"+des.Desencriptar("BkHRbeZonLU="));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
