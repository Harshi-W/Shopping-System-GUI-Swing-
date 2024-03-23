import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserValidator extends JFrame {
    boolean infoFileLoadedForNewUsers = false;
    boolean infoFileLoadedForOldUsers = false;
    private static Map<String, User> userInfo = new HashMap<>();
    private static Map<String, Boolean> discount = new HashMap<>();
    final JPanel mainPanel= new JPanel(new GridLayout(3,1));
    final JPanel panel2 = new JPanel(new FlowLayout());
    private JPanel panel3;
    private File infoFile = new File("userInfoFile.txt");
    private File discountUserList = new File("discountInfo.txt");
    public static File cartDetails = new File("cartDetails.txt");


    public void showValidatorGUI(){
        JPanel panel1 =new JPanel(new FlowLayout());
        JButton newUserButton = new JButton("New User");
        panel1.add(newUserButton,BorderLayout.NORTH);
        mainPanel.add(panel1);
        newUserButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!infoFileLoadedForNewUsers){//for validating user details
                    loadFile(infoFile);
                    System.out.println(userInfo);
                    newUserChecker();
                    infoFileLoadedForNewUsers =true;
                }else {
                    System.out.println(userInfo);
                    newUserChecker();
                }
            }
        });
        JButton registeredUserButton = new JButton("Registered User");
        panel1.add(registeredUserButton,BorderLayout.SOUTH);
        registeredUserButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!infoFileLoadedForOldUsers){
                    loadFile(infoFile);
                    System.out.println(userInfo);
                    login();
                    infoFileLoadedForOldUsers =true;
                    saveToFile(infoFile,userInfo);
                }else {
                    System.out.println(userInfo);
                    login();
                    saveToFile(infoFile,userInfo);
                }
            }
        });
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }
    public  void newUserChecker () {
        panel2.removeAll();
        JLabel username = new JLabel("Username: ");
        panel2.add(username);
        JTextField usernameText = new JTextField(8);
        panel2.add(usernameText);
        JLabel password = new JLabel("Password: ");
        panel2.add(password);
        JPasswordField passwordText = new JPasswordField(8);
        panel2.add(passwordText);
        JButton enterButton = new JButton("enter");
        panel2.add(enterButton);
        enterButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                String passwordString  = new String(passwordText.getPassword().toString());
                if (userInfo.containsKey(usernameText.getText())) {
                    panel3 =new JPanel(new BorderLayout());
                    JLabel warning = new JLabel("Entered username is already in use.\nPlease enter unique username..");
                    panel3.add(warning,BorderLayout.CENTER);
                    mainPanel.add(panel3);
                }else {
                    getContentPane().add(mainPanel);
                    User newUser = new User(usernameText.getText(), passwordString);
                    userInfo.put(usernameText.getText(), newUser);
                    System.out.println(userInfo);
                    login();
                    saveToFile(infoFile,userInfo);
                }
            }
        });
        mainPanel.add(panel2);
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

    public static void saveToFile (File filename,Map hashmap1) {
        try {
            if (filename.createNewFile()) {
                System.out.println("File created: " + filename.getName());
            } else {
                System.out.println("infoFile already exists.");
                try {
                    // Overwrite the file with an empty string
                    FileWriter writeAgain = new FileWriter(filename, false);
                    writeAgain.close();
                } catch (IOException e) {
                    System.out.println("Empty File...");
                }
                System.out.println("info file cleared..");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try {
            BufferedWriter writeList = new BufferedWriter(new FileWriter(filename, true));
            writeList.write(hashmap1.toString());
            System.out.println("data saved to info file..");
            writeList.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public void loadFile(File filename){
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while ( (line = reader.readLine())!= null){
                String[] keyValuePairs = line.replaceAll("[{}]", "").split(",\\s*");
                for (String pair : keyValuePairs) {
                    String[] entry = pair.split("=");
                    if (entry.length == 2) {
                        String key = entry[0].trim();
                        String value = entry[1].trim();
                        if (filename == infoFile) {
                            User user = new User(key, value);
                            userInfo.put(key, user);
                        }else if(filename == cartDetails) {

                            ShoppingCart.discountK.put(key,value);
                        }else{
                            Boolean boolValue = Boolean.valueOf(value);
                            discount.put(key,boolValue);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("info File has not created  yet.");
        } catch (IOException e) {
            System.out.println("An Error occur when loading info file");
        }
        try {
            // Overwrite the file with an empty string
            FileWriter writeAgain = new FileWriter(infoFile, false);
            writeAgain.close();
        } catch (IOException e) {
            System.out.println("Empty File...");
        }
    }
    public void login(){
        panel2.removeAll();
        JLabel username = new JLabel("Username: ");
        panel2.add(username);
        JTextField usernameText = new JTextField(8);
        panel2.add(usernameText);
        JButton enterButton = new JButton("enter");
        panel2.add(enterButton);
        mainPanel.add(panel2);
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
        loadFile(discountUserList);
        loadFile(cartDetails);
        enterButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(userInfo);
                if (userInfo.containsKey(usernameText.getText())) {
                    if (ShoppingCart.discountK.containsKey(usernameText.getText()) && ShoppingCart.discountK.get(usernameText.getText()) != null ) {
                        System.out.println("Welcome back....!");
                        Main.shoppingManager.openGUI(false,usernameText.getText());
                    } else {
                        System.out.println("you have 10% discount on first buy!");
                        Main.shoppingManager.openGUI(true,usernameText.getText());
                        discount.put(usernameText.getText(),true);
                        saveToFile(discountUserList,discount);
                    }
                } else {
                    System.out.println("Invalid username.Please register as new user or reenter username.");
                }
            }
        });
    }
    public void showGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setTitle("User Validator GUI");
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        showValidatorGUI();
    }
}

