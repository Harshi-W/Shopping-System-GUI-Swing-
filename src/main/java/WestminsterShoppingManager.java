import java.io.*;
import java.util.*;

public class WestminsterShoppingManager implements ShoppingManager {
    private static ArrayList<Product> products;
    public WestminsterShoppingManager(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public  String userDetails(String prompt) {
        String data = null;
        Scanner input = new Scanner(System.in);
        System.out.print(prompt);
        data = input.nextLine();
        return data;
    }
    @Override
    public void addNewProduct() {
        boolean quit = false;
        while (!quit) {
            System.out.println("1)Electronic item");
            System.out.println("2)Clothing item");
            Scanner input = new Scanner(System.in);
            System.out.println("Enter the type of the product(press 1 or 2): ");
            int productType;
            try {
                productType = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 2.");
                continue;
            }
            switch (productType) {
                case 1:
                    if (products.size() <= 50) {
                        inputData("Electronics");
                        System.out.println("Do you want to add more products: ");
                        String answer = input.nextLine();
                        if (answer.equals("n")) {
                            quit = true;
                        } else {
                            int sizeOfList = products.size();
                            System.out.println(sizeOfList);//delete this
                        }
                    } else {
                        System.out.println("You can add maximum 50 of products.");
                    }
                    break;
                case 2:
                    if (products.size() <= 50) {
                        inputData("Clothing");
                        System.out.println("Do you want to add more products: ");
                        String answer = input.nextLine();
                        if (answer.equals("n")) {
                            quit = true;
                        } else {
                            int sizeOfList = products.size();
                            System.out.println(sizeOfList);//delete this
                        }
                    } else {
                        System.out.println("You can add maximum 50 of products.");
                    }
                    break;
            }
        }
    }
    @Override
    public void inputData(String className){
        String productID = userDetails("Enter the product ID: ");
        String productName = userDetails("Enter the product name: ");
        int availableItems = inputValidatorItems("Enter number of available items: ");
        double price = inputValidatorPrice("Enter the product price: ");
        if (className.equals("Clothing")){
            String size = userDetails("Enter the product size: ");
            String color = userDetails("Enter the product color: ");
            Clothing e1 = new Clothing(productID, productName, availableItems, price, size, color);
            products.add(e1);
        }else {
            String brand = userDetails("Enter the product brand: ");
            String warranty = userDetails("Enter the product's warranty period: ");
            Electronics e1 = new Electronics(productID, productName, availableItems, price, brand, warranty);
            products.add(e1);
        }
    }
    @Override
    public  double inputValidatorPrice(String prompt){
        Scanner input = new Scanner(System.in);
        double price;
        while (true){
            try {
                System.out.print(prompt);
                price =input.nextDouble();
                break;
            }catch (InputMismatchException e){
                input.nextLine();
                System.out.println("Enter a valid double");
            }
        }
        return price;
    }
    @Override
    public  int inputValidatorItems(String prompt){
        Scanner input = new Scanner(System.in);
        int noOfItems;
        while (true){
            try {
                System.out.print(prompt);
                noOfItems =input.nextInt();
                break;
            }catch (InputMismatchException e){
                input.nextLine();
                System.out.println("Enter a valid Integer");
            }
        }
        return noOfItems;
    }
    @Override
    public void deleteProduct() {
        System.out.println(products);//delete this
        String productIDToDelete = userDetails("Enter product ID to delete: ");
        Iterator<Product> pro = products.iterator();
        boolean result = false;
        while (pro.hasNext()) {
            Product prod = pro.next();
            if (productIDToDelete.equals(prod.getProductID())) {
                pro.remove();
                System.out.println(prod.getProductName()+" which is "+prod.getClass()+" item has deleted from the products list");
                System.out.println("Available products in the system: "+ products.size());
                result =true;
                break;
            }
        }
        if (!result){
            System.out.println("There is no any matching product with product ID "+productIDToDelete+".");
        }
    }
    @Override
    public void printList() {
        if (products.size() > 0){
            Collections.sort(products, Comparator.comparing(Product::getProductID));
            for(int i =0;i<products.size();i++){
                System.out.println(products.get(i));
            }
        }else {
            System.out.println("Product list is empty..");
        }
    }
    @Override
    public void saveFile() {
        File productFile = new File("File.txt");
        try {
            if (productFile.createNewFile()) {
                System.out.println("File created: " + productFile.getName());
            } else {
                System.out.println("File already exists.");
                try {
                    FileWriter writer = new FileWriter(productFile, false);
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Empty.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        try{
            BufferedWriter writeToFile= new BufferedWriter(new FileWriter("File.txt",true));
            writeToFile.write(products.toString());
            writeToFile.close();
        }catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    @Override
    public void loadFromFile() {
        try {
            File listFile = new File("File.txt");
            Scanner scanner = new Scanner(listFile);
            String listContent = scanner.useDelimiter("\\A").next();
            scanner.close();
            listContent = listContent.replaceAll("[\\[\\]]", "");
            //To remove curly braces and split by them.
            String[] allProducts = listContent.split("\\},\\s*\\{");
            for (String item : allProducts) {
                //Split items by commas and remove braces.
                String[] data = item.replaceAll("[{}]", "").split(",");
                String productID = data[0];
                String productName = data[1];
                int noOfAvailableItems = Integer.parseInt(data[2]);
                double price = Double.parseDouble(data[3]);
                if (data[6].equals("Clothing")) {
                    String size = data[4];
                    String color = data[5];
                    products.add(new Clothing(productID, productName, noOfAvailableItems, price, size, color));
                } else {
                    String brand = data[4];
                    String warrantyPeriod = String.valueOf(data[5]);
                    products.add(new Electronics(productID, productName, noOfAvailableItems, price, brand, warrantyPeriod));
                }
            }
            try {
                FileWriter writer = new FileWriter(listFile, false);
                writer.close();
            } catch (IOException e) {
                System.out.println("Empty File...");
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred....");
        }
    }
    @Override
    public void openGUI(boolean newUser,String name){
        GUI gui = new GUI();
        gui.showGUI(products,newUser,name);
    }
    @Override
    public ArrayList<Product> getProducts() {
        return products;
    }
}

interface ShoppingManager{
    String userDetails(String prompt);
    void addNewProduct();
    void inputData(String className);
    double inputValidatorPrice(String prompt);
    int inputValidatorItems(String prompt);
    void deleteProduct();
    void printList();
    void saveFile();
    void openGUI(boolean newUser,String name);
    ArrayList<Product> getProducts();
    void loadFromFile();
}

