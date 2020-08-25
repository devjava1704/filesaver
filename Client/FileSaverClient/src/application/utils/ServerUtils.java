package application.utils;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.*;

public class ServerUtils {

	private static final String LOG_TAG="["+ServerUtils.class.getName()+"/LOG]:> ";
	
	private String serverIp="";
	private int port=0;
	private boolean utilsFlag=false;
	
	private Socket server;
	private InputStreamReader sIN;
	private BufferedReader in;
	private OutputStream sOUT;
	private PrintWriter out;
	private DataInputStream din;
	
	public ServerUtils(String ip, int port) {
		this.serverIp=ip;
		this.port=port;
		
		if(ip.equals("") || ip==null || port<1000 || port>9999) {
			log("Server non valido! Indirizzo ip o porta non validi!");
		}else {
			startUtils();
		}
	}
	
	public String getServerStatus() {
		String status="";
		
		byte[] bytes = "S".getBytes();
				 
		String encodedString = new String(bytes, StandardCharsets.UTF_8);
				
		out.print(encodedString);
		out.flush();
			
		try {
			
			
			String temp="";
			
			
			
			 while((temp=in.readLine()) != null){


				 status+=temp+"\n";
				 System.out.println(temp);
			
				 
			 }
					
		} catch (Exception e) {
			log("Errore nel ricevere lo status del server!\nLOG:"+e.getMessage());
			status="Errore nella comunicazione con il server";
		}
		
		return status;
	}

	public String getFileList() {
		String list="";
		if(utilsFlag) {
			byte[] bytes = "L".getBytes();
			 
			String encodedString = new String(bytes, StandardCharsets.UTF_8);
					
			out.print(encodedString);
			out.flush();
			
			try {
				
				 while((temp=in.readLine()) != null){


				 list+=temp+"\n";
				 System.out.println(temp);
			
				 
			 }
				
	
				
			} catch (Exception e) {
				log("Errore nel ricevere la lista files del server\nLOG:"+e.getMessage());
				list="Errore nella comunicazione con il server";
			}
			}
		return list;
	}

	public void disconnect() {
		if(utilsFlag) {
			byte[] bytes = "D".getBytes();
			 
			String encodedString = new String(bytes, StandardCharsets.UTF_8);
					
			out.print(encodedString);
			out.flush();
		}
		
		try {
			out.close();
			in.close();
			sIN.close();
			sOUT.close();
			server.close();
		} catch (IOException e) {
			log("Errore nella chiusura dei flussi i/O\nLOG:"+e.getMessage());
		}
		
	}

	
	
	
	
	private void startUtils() {
		try {
			server= new Socket(serverIp, port);
			log("Connessione con il server "+serverIp+" stabilita");
		}catch(Exception e) {
			log("Errore nella connessione al server.\nLog dell'errore: "+e.getMessage());
		}
		
		try {
			//flussi input
			sIN= new InputStreamReader(server.getInputStream());
			in= new BufferedReader(sIN);
			//flussi output
			sOUT= server.getOutputStream();
			out= new PrintWriter(sOUT);
			
			din=new DataInputStream(server.getInputStream());
			
			utilsFlag=true;
		}catch(Exception e) {
			log("Errore nell'inizializzazione dei flussi i/O con il server!\nLOG: "+e.getMessage());
		}
		
		
	}
	
	
	private void log(String message) {
		System.out.println(LOG_TAG+""+message+"\n");
	}
}


