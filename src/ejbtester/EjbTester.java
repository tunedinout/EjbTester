/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbtester;
import com.tutorialspoint.stateless.LibrarySessionBeanRemote;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 *
 * @author narottamkrjha
 */
public class EjbTester {
    BufferedReader brConsoleReader=null;
    Properties props;
    InitialContext ctx;
    {
        
        //properties object's intial context is set in this static block
        //static block always executes before the constructor
        props = new Properties();
        try {
            props.load(new FileInputStream("jboss-ejb-client.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();;
        }
        
        try {
            //set props as the initial context
            ctx = new InitialContext(props);
        } catch (NamingException ex) {
           ex.printStackTrace();
        }
        brConsoleReader = new BufferedReader(new InputStreamReader(System.in));
    }
    
    private void showGUI(){
        System.out.println("***************************");
        System.out.println("Welcome to the book store");
        System.out.println("***************************");
        System.out.println("Options : ---- \n1.Add Book   \n2.Exit \nEnter Choice: ");
        
    }
    private void testStatelessEjb(){
        try{
            int choice = 1;
            
            LibrarySessionBeanRemote libraryBean;
            //how did we know using this key we will get the bean
            String appName = "";
            String moduleName="EjbComponent";
            String distinctName="";
            String beanName = "LibrarySessionBean";
            String interfaces = LibrarySessionBeanRemote.class.getName();
            String name = "ejb:" + appName + "/" + moduleName + "/" +  distinctName    + "/" + beanName + "!" + interfaces;
            libraryBean = (LibrarySessionBeanRemote)ctx.lookup(name);
            
            while(choice!=2){
                String bookName ;
                showGUI();
                String strChoice = brConsoleReader.readLine();
                choice = Integer.parseInt(strChoice);
                if(choice==1){
                    System.out.println("Enter book Name : ");
                    bookName = brConsoleReader.readLine();
                    libraryBean.addBook(bookName);
                  
                }else if(choice==2){
                    break;
                }
                
                List<String> bookList = libraryBean.getBooks();
                System.out.println(" Books entered so far ----------     ");
                for(int i=0;i<bookList.size();i++){
                    System.out.println((i+1) + " : "+bookList.get(i));
                }
                
                
                LibrarySessionBeanRemote libraryBean1 = (LibrarySessionBeanRemote)ctx.lookup(name);
                bookList = libraryBean1.getBooks();
                System.out.println("***using second lookup to get the library stateless objects*************");
                System.out.println(" Books entered so far ----------     ");
                for(int i=0;i<bookList.size();i++){
                    System.out.println((i+1)+ " : " + bookList.get(i));
                }
                
            }
            
        }catch(Exception ex){
            ex.printStackTrace();

        }finally{
            try{
                if(brConsoleReader!=null)
                brConsoleReader.close();
            }catch(IOException ex){
                ex.printStackTrace();;
            }
            
            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EjbTester ejbTester = new EjbTester();
        ejbTester.testStatelessEjb();
        
//        String URLName = "http://localhost:8080";
//        boolean isUp = false;
//        try{
//          
//            HttpURLConnection.setFollowRedirects(false);
//            HttpURLConnection con = (HttpURLConnection)new URL(URLName).openConnection();
//            con.setRequestMethod("GET");
//            isUp = (con.getResponseCode()==HttpURLConnection.HTTP_OK);
//            con.disconnect();
//            
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }
//        if(isUp == true)
//        System.out.println("valid connection");
    }
    
}
