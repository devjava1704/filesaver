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

	
	public ServerUtils(String ip, int port) {
		this.serverIp=ip;
		this.port=port;
		
		if(ip.equals("") || ip==null || port<1000 || port>9999) {
			log("Server non valido! Indirizzo ip o porta non validi!");
		}
	}
	
	
	public void uploadFile(File file) {
		startUtils();
		
		if(utilsFlag) {
			
			out.print("file");
			out.flush();
			
			out.print(file.getName());
			out.flush();
	
			
			try {
				
	          File myFile = file;
	          byte [] bytearray  = new byte [(int)myFile.length()];
	          FileInputStream fis = new FileInputStream(myFile);
	          BufferedInputStream bis = new BufferedInputStream(fis);
	          bis.read(bytearray,0,bytearray.length);
	         
	          System.out.println("Sending " + file.getName() + "(" + bytearray.length + " bytes)");
	          sOUT.write(bytearray,0,bytearray.length);
	          sOUT.flush();
	          
			}catch(Exception e) {
				log("Errore nel mandare il file "+file.getName()+" al server\nLOG:"+e.getMessage());
			}
			
		}
		
		stopUtils();
	}
	
	public File downloadFile(String name, String pathToSave) {
		startUtils();
		File file= null;
		String tempo="";
		if(utilsFlag) {
			
			out.print("D");
			out.flush();
			
			out.print(name);
			out.flush();
	
			try {
			
				 file = new File(name);
				 if (file.createNewFile()) {
				      System.out.println("File created: " + file.getName());
				 } else {
				       log("File "+name+" already exists.");
				 }
				  
				
		         FileOutputStream outer = new FileOutputStream(pathToSave+name);
		        
				
				 byte[] bytes = new byte[16*1024];

			     int count;
			     while ((count = server.getInputStream().read(bytes)) > 0) {
			         outer.write(bytes, 0, count);
			     }
						
			} catch (Exception e) {
				log("Errore nel ricevere il file "+name+" dal server!\nLOG:"+e.getMessage());
				tempo="Errore nella comunicazione con il server";
			}
			
		}
		
		stopUtils();
		return file;
	}
	
	public String getServerStatus() {
		startUtils();
		String status="";
		
		if(utilsFlag) {

			out.print("S");
			out.flush();
				
			try {
				 String temp="";
				
				 while((temp=in.readLine()) != null){
	
					 status+=temp+"\n";
					 	 
				 }
						
			} catch (Exception e) {
				log("Errore nel ricevere lo status del server!\nLOG:"+e.getMessage());
				status="Errore nella comunicazione con il server";
			}
			
		}
		
		stopUtils();
		
		return status;
	}

	public String getFileList() {
		startUtils();
		String list="";
		if(utilsFlag) {

			out.print("L");
			out.flush();
			
			
			try {
				
				String temp="";
				 while((temp=in.readLine()) != null){
						
					 list+=temp+"\n";
					 	 
				 }
				
				
			} catch (Exception e) {
				log("Errore nel ricevere la lista files del server!\nLOG:"+e.getMessage());
				list="Errore nella comunicazione con il server";
			}
			}
		
		
		stopUtils();
		
		return list;
	}

	public void disconnect() {
		startUtils();
		
		if(utilsFlag) {
			byte[] bytes = "D".getBytes();
			 
			String encodedString = new String(bytes, StandardCharsets.UTF_8);
					
			out.print(encodedString);
			out.flush();
			
			System.out.println("Disconnesione eseguita");
		}
		
		stopUtils();
		
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
			
			
			utilsFlag=true;
		}catch(Exception e) {
			log("Errore nell'inizializzazione dei flussi i/O con il server!\nLOG: "+e.getMessage());
		}
		
		
	}
	
	private void stopUtils() {
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
	
	private void log(String message) {
		System.out.println(LOG_TAG+""+message+"\n");
	}
}


