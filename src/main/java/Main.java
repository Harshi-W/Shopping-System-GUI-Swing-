
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager(new ArrayList<>());
    public static void main(String[] args) {
        shoppingManager.loadFromFile();
        Scanner input = new Scanner(System.in);
        boolean flag = false;
        while (!flag) {
            System.out.println("______Console Menu______");
            System.out.println("1)Add a new product");
            System.out.println("2)Delete a product");
            System.out.println("3)Print the list of the product");
            System.out.println("4)Save in a file");
            System.out.println("5)Open  GUI");
            System.out.println("0)Quit");
            System.out.println("Enter a option: ");
            int optionNum;
            try {
                optionNum = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                continue;
            }
            switch (optionNum) {
                case 1 -> shoppingManager.addNewProduct();
                case 2 -> shoppingManager.deleteProduct();
                case 3 -> shoppingManager.printList();
                case 4 -> shoppingManager.saveFile();
                case 5 -> {
                    shoppingManager.saveFile();
                    UserValidator val = new UserValidator();
                    val.showGUI();
                }
                case 0 -> flag = true;
                default -> System.out.println("Incorrect option.");
            }
        }
    }
}
