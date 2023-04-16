import java.awt.EventQueue;
import java.awt.event.ActionEvent;		//button funct
import java.awt.event.ActionListener;
import java.awt.Dimension; //for absolute positioning
import java.awt.Color;	// for coloring labels

import java.time.*;	// for dates and the time
import java.time.format.DateTimeFormatter;	// for formating time to convienence
import java.io.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;	//swing var
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class LoginWindow 
{

	private JFrame frame;
	private JTextField IDTextField;
	private JTextField pinTextField;
	private JButton loginButton;
	private String activeUser;

	public LoginWindow() 
	{
		frame = new JFrame("Login Window");
		frame.setLayout(null);
		frame.setLocationRelativeTo(null);	//Login window frame
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel IDLabel = new JLabel("ID:");
		IDLabel.setLocation(40, 25);		//ID Text label
		IDLabel.setSize(100, 25);
		frame.add(IDLabel);

		IDTextField = new JTextField();
		IDTextField.setLocation(140, 25);	//ID Text Field
		IDTextField.setSize(200, 25);
		frame.add(IDTextField);

		JLabel pinLabel = new JLabel("Pin:");
		pinLabel.setLocation(40, 75);		//Pin Text Labek
		pinLabel.setSize(100, 25);
		frame.add(pinLabel);

		pinTextField = new JTextField();
		pinTextField.setLocation(140, 75);	//Pin Text Field
		pinTextField.setSize(200, 25);
		frame.add(pinTextField);

		loginButton = new JButton("Login");
		loginButton.setLocation(185, 115);	// login button
		loginButton.setSize(100, 25);
		loginButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String ID = IDTextField.getText();
				String pin = pinTextField.getText();
				
				if (ID.equals("admin") && pin.equals("1234")) 
				{
					new AdminPanel().setVisible(true);
				}
				else if (isValidLogin(ID,pin)) 
				{
					new UserPanel(activeUser,ID); //
					//frame.setVisible(false);
				}
				else 
				{
				  JOptionPane.showMessageDialog(null, "Invalid ID or pin"); // no matches found
				}
				IDTextField.setText("");
				pinTextField.setText("");
			}
		});
		frame.add(loginButton);
		frame.setVisible(true);
	}
	
	
	private boolean isValidLogin(String ID, String pin)
	{
		try
		{
			File file = new File("Data.csv");
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String ln = "";
			String[] lnArr;
			ln = br.readLine(); // reads the header
			//System.out.println(ID + "  " + pin);
			while((ln = br.readLine())!=null)
			{
				lnArr = ln.split(", ");
				//System.out.println(lnArr[1]+"  "+lnArr[2]); 
				if ((ID.equals(lnArr[1])) && (pin.equals(lnArr[2])))		////checks if the ID and pin have a match
				{
					this.activeUser = lnArr[0];	//assigns the username to the activeUser
					return true;
				}
			}
			br.close();
		}
		catch (IOException e)
		{
			System.out.println("" + e);
		}
		return false;
	}
	
	public void setLoginWindowVisible()
	{
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				//new UserPanel("test","00001"); used to test the userpanel
				new LoginWindow();
				//System.out.println(System.getProperty("user.dir"));
				
			}
		});
	}
}

class AdminPanel extends JFrame 
{

	public AdminPanel() 
	{
		setTitle("Welcome!");
		setSize(400, 300);
		setLocationRelativeTo(null);
		//setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		JLabel welcomeLabel = new JLabel("Welcome to the application!");
		welcomeLabel.setLocation(100, 100);
		welcomeLabel.setSize(200, 25);
		add(welcomeLabel);

		JButton exitButton = new JButton("Exit");										// ADMIN PANEL IS WIP
		exitButton.setLocation(100, 150);
		exitButton.setSize(75, 35);
		exitButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
			}
		});
		add(exitButton);
		getContentPane().setLayout(null); //enforces absolute layout
		setVisible(true);
	}
}


class UserPanel extends JFrame
{
	private JLabel statusLbl = new JLabel("Status: Null"); //placed out here so that it can be accessed for the methods

	public UserPanel(String username, String ID) 
	{
		setTitle("Clock in/out");
		setSize(400, 300);
		setLocationRelativeTo(null);
		//setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		JLabel welcomeLbl = new JLabel("Welcome "+username+"!");
		welcomeLbl.setLocation(50, 5);
		welcomeLbl.setSize(200, 25);
		add(welcomeLbl);
		
		JLabel infoLbl = new JLabel(getUserInfo(ID,"<br>","<html>")); //necessary html formatting for multilined labels
		infoLbl.setLocation(210, 40);
		infoLbl.setSize(150, 150);
		infoLbl.setOpaque(true);
		infoLbl.setBackground(Color.WHITE); // used to differentiate from background bc of formating
		add(infoLbl);
		
		//status label
		statusLbl.setSize(120,45);
		statusLbl.setLocation(30,40);
		add(statusLbl);
		
		JLabel timeLbl = new JLabel();
		timeLbl.setLocation(285,5);
		timeLbl.setSize(100,25);
		Timer timer = new Timer(1000, new ActionListener()
		{
			@Override
            public void actionPerformed(ActionEvent e) {
                LocalTime time = LocalTime.now();				// displays current time to the second
				String fTime = String.format("%02d:%02d:%02d", time.getHour(), time.getMinute(), time.getSecond());
				timeLbl.setText(fTime);
            }
        });
		timer.start();
		add(timeLbl);
		
		JButton exitButton = new JButton("Exit");
		exitButton.setLocation(285, 205);
		exitButton.setSize(75, 35);
		exitButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dispose();								//exit button
			}
		});
		add(exitButton);
		
		JButton inButton = new JButton("Clock   IN");
		inButton.setLocation(30,80);
		inButton.setSize(120,45);
		inButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) //in button
			{
				sendUserInfo(ID,"IN");
				dispose();					//closes bc technically you shouldnt need it after you press
			}
		});
		add(inButton);
		
		JButton outButton = new JButton("Clock OUT");
		outButton.setLocation(30,130);
		outButton.setSize(120,45);
		outButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) //out button
			{
				
				sendUserInfo(ID,"OUT");
				dispose();
			}
		});
		add(outButton);
		
		getContentPane().setLayout(null); //enforces absolute layout
		setVisible(true);
	}
	
	private String getUserInfo(String ID, String delimiter, String starter)//delimiter and starter make the effiecieny of the function have future use
	{
		try
		{
			File f = new File(System.getProperty("user.dir")+"\\Userfiles\\"+ID+".csv");
			FileReader file = new FileReader(f);
			BufferedReader reader = new BufferedReader(file);
			String temp = starter;
			String temp2 = "";
			String line = "";
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				temp += line + delimiter;
				temp2 = line; //used to get the last fruitful line
			}
			reader.close();
			file.close();
			if (temp2.contains("IN"))
			{
				statusLbl.setText("Status: IN");
			}
			else if (temp2.contains("OUT"))
			{
				statusLbl.setText("Status: OUT");  	// Determines the status label
			}
			else
			{
				statusLbl.setText("Status: null");
			}
			return temp;
			
		}
		catch (IOException e)
		{
			System.out.println("" + e);
		}
		return "";
	}
	
	private void sendUserInfo(String ID, String inOrOut)
	{
		try
		{
			FileWriter fw = new FileWriter(System.getProperty("user.dir")+"\\Userfiles\\"+ID+".csv", true); // takes absolute directory and finds the appropriate csv file
			LocalDateTime myDateObj = LocalDateTime.now();    
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM/dd/yyyy, HH:mm");  // Date formating
			String formattedDate = myDateObj.format(myFormatObj);  
			fw.write("\n"+formattedDate+", "+inOrOut); //writing
			fw.close();
			
			
		}
		catch (IOException e)
		{
			System.out.println("" + e);
		}
	}
	
}