/**SimpleFirewall 연동클래스**/
package l1j.server.server;

import java.io.*;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.*;

public class MiniClient extends Thread{
 /** 내컴퓨터의 로컬호스트**/
 private String serverIP = "111.111.111.111";  // 내 아이피 주소  
 /**심플 파이어월 포트**/
 private final int serverPort = 2009;
 /** 출력 스트림 **/ 
 private PrintWriter printWriter = null;
  boolean listenFlag = true;
 

/**MiniClient 클래스객체가 담긴 인스턴스**/
 private static MiniClient _instance;
 /**파월 차단중인지 검사**/
 

 public static MiniClient getInstance() {
  if (_instance == null) {
   _instance = new MiniClient();
  }
  return _instance;
 }

 
  public MiniClient() {
     this.serverIP = serverIP;
     boolean isInitEnv = this.initEnv();  
  }

  /**쓰레드 런**/
  public void run() {  
    try {
	    while(listenFlag == true) {
    	this.sleep(2000);
		}
    } catch(Exception e) {   
      listenFlag = false;    
      e.printStackTrace();
    }   
  }
  





























 /** 소켓연결 및 출력 스트림 취득**/
 private boolean initEnv() {
  try {
   Socket clientSocket = new Socket(serverIP,serverPort);  
   printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
   System.out.println( "::::: SimpleFirewall 연결 완료! :::::" );

   return true;
  }
  catch(Exception e) {
   e.printStackTrace();   
   if(printWriter != null){
    try{
     printWriter.close();
    }catch(Exception printWriterException){
     printWriterException.printStackTrace();     
    }
   }   
   return false;
  }
 }


/** 차단할 IP 전송 **/
public void MessageToServer(String IP){
  try {
   printWriter.println("L@"+IP);
   printWriter.flush();   
   System.out.println("["+ IP+"]  SimpleFirewall 차단완료!!" );
  } catch (Exception e){   
   e.printStackTrace();
  }finally{   
  }
 }

 }