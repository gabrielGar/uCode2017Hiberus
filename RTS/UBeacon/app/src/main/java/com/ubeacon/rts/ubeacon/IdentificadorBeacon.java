/* AUTOR: RTS
 * FICHERO:  IdentificadorBeacon.java
 * DESCRIPCION: Decodifica informacion a partir del UUID del beacon recibido:
 * Numero de Hotel, tipo de seguridad necesaria para tratar el beacon, 
 * tipo de beacon, identificador de beacon.  
 */


package com.ubeacon.rts.ubeacon;

import java.lang.String;

/**
 * Using first three bytes of the beacon UUID to identify the Hotel, the type of the UUID: information or interactive: and the security level: a registered user or a guest.
 */
public class IdentificadorBeacon {
	public static final int HOTEL_USER = 0;
	public static final int HOTEL_GUEST = 1;

	public static final int BEACON_INFORMATIVE = 0;
	public static final int BEACON_INTERACTIVE = 1;
	/* Identificador del hotel */
	public String hotel;
	
	/* Flag de seguridad */ 
	public int security_type = 0;
	
	/* Flag de tipo de beacon */
	public int type = 0;
	
	/* Identificador de beacon */
	public String bID;
	
	/* UUID Beacon*/
	public String beacon;



	/**
	 * Extrac the information asociated with the beacon UUID.
	 * @param beac
     */
	public IdentificadorBeacon(String beac){		
		beacon=beac;
		hotel=beac.substring(0,5);
		security_type=decoSegType();
		type=decoTipo();
		bID=beac.substring(6,36);
	}

	/**
	 * Obtain the security level of the UUID
	 * @return
     */
	private int decoSegType(){
		char letra;
		int i;
		
		letra = beacon.charAt(5);
		i = char2Hex(letra);
		
		return (i>>2);
	}

	/**
	 * Decode the type of the uuid
	 * @return
     */
	private int decoTipo(){
		char letra;
		int i;
		
		letra = beacon.charAt(5);
		i = char2Hex(letra);
		
		
		return (i & 3);
	}
	

	private int char2Hex(char ch){
		int chi = (int)ch;
		int valor = 0;
		
		if(ch<=57 & ch>=48){
			valor = chi-48;
		}else if(ch<=102 & ch>=97){
			valor = ch - 87;
		}
				
		return valor;
	}

}
