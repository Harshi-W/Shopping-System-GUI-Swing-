import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.*;


public class ShoppingCart extends JFrame{
    private ArrayList<Product> productList;
    private tableCartnew tableCart;
    private Product cartProduct;
    private Map<String, Integer> pQuantityMapping;
    private Double discount20=0.0;
    private Double priceTotal=0.0;
    private Double finalTotal=0.0;
    private Double newUserDiscount=0.0;
    private JPanel panelLower;
    private JPanel panel1;
    public static Map<String, Object> discountK = new HashMap<>();

    public ShoppingCart(Product cartProduct) {
        this.cartProduct = cartProduct;
        this.pQuantityMapping = new HashMap<>();
        this.productList = new ArrayList<>();
        this.tableCart = new tableCartnew();
    }
    public void Cart() {
        panel1 = new JPanel(new GridLayout(2, 1));
        JPanel panelUpper = new JPanel(new BorderLayout());
        JPanel panelCheck1 = new JPanel();
        panelUpper.add(panelCheck1,BorderLayout.NORTH);
        JPanel panelCheck2 = new JPanel(new FlowLayout());
        JPanel p1 = new JPanel();
        panelCheck2.add(p1);
        JPanel p2 = new JPanel();
        tableCart = new tableCartnew();
        // Create a JTable with the custom model
        JTable tableC = new JTable(tableCart);
        tableC.setPreferredScrollableViewportSize(new Dimension(450, 120));
        tableC.setFillsViewportHeight(true);
        DefaultTableCellRenderer centerData = new DefaultTableCellRenderer();
        centerData.setHorizontalAlignment(JLabel.CENTER);
        for (int i=0;i<tableC.getColumnCount();i++){
            tableC.getColumnModel().getColumn(i).setCellRenderer(centerData);
        }
        tableC.setRowHeight(60);
        JTableHeader titles = tableC.getTableHeader();
        titles.setPreferredSize(new Dimension(titles.getHeight(), 60 ));
        titles.setFont(new Font("Arial",Font.BOLD,12));
        JScrollPane scrollPane = new JScrollPane(tableC);
        scrollPane.setVisible(true);
        panelCheck2.add(scrollPane);
        panelCheck2.add(p2);
        panelUpper.add(panelCheck2,BorderLayout.CENTER);
        panel1.add(panelUpper);
        panelLower = new JPanel(new BorderLayout());
        getContentPane().add(panel1);
        updateCartLabel();
        revalidate();
        repaint();
    }

    private class tableCartnew extends AbstractTableModel {
        private String[] columnNames = {"Product", "Quantity", "Price"};


        @Override
        public int getRowCount() {
            return productList.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex < productList.size()) {
                Product product = productList.get(rowIndex);
                String uniqueKey = product.getProductID();
                if (productList != null) {
                    if (columnIndex == 0) {
                        if (product instanceof Clothing){
                            Clothing clProduct = (Clothing) product;
                            return "<html><pre style='font-family: Arial;'>"+clProduct.getProductID() + "<br>" + clProduct.getProductName()+"<br>"+ clProduct.getSize() +"," +clProduct.getColor()+"</pre></html>";
                        }else {
                            Electronics elProduct = (Electronics) product;
                            return "<html><pre style='font-family: Arial;'>"+elProduct.getProductID() + "<br>" + elProduct.getProductName()+"<br>"+ elProduct.getBrand()+","+elProduct.getWarrantyPeriod()+"</pre></html>";
                        }
                    } else if (columnIndex == 1) {
                        if (pQuantityMapping.containsKey(uniqueKey)){
                            return pQuantityMapping.get(uniqueKey);
                        }else {
                            return 0;
                        }
                    } else if (columnIndex == 2) {
                        if(pQuantityMapping.containsKey(uniqueKey)){
                            return product.getPrice()*pQuantityMapping.get(uniqueKey);
                        }else{
                            return 0.0;
                        }
                    }
                }
            }
            return null;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }
    }

    public void  add(Product cartProduct, int quantity, boolean newUser,String name){
        String uniqueKey = cartProduct.getProductID();
        if (pQuantityMapping.containsKey(uniqueKey)){
            int currentQuantity = pQuantityMapping.get(uniqueKey);
            pQuantityMapping.put(uniqueKey, quantity+currentQuantity);
        }else {
            pQuantityMapping.put(uniqueKey,quantity);
            productList.add(cartProduct);
        }
        discountK.put(name,productList);
        UserValidator.saveToFile(UserValidator.cartDetails,discountK);
        calculateTotal(newUser,name);


    }

    private void updateCartLabel() {
        JPanel panelLower1 = new JPanel(new FlowLayout());
        JPanel ch1 = new JPanel();
        panelLower1.add(ch1);
        SwingUtilities.invokeLater(()-> {
            if (newUserDiscount != 0.0) {
                panelLower.removeAll();
                JLabel costLabel = new JLabel("<html><pre><font face='Arial' style='font-weight: normal;'>"
                        + "                                                                       Total         " + priceTotal+" $" + "\n"+"\n"
                        + "                           First Purchase Discount(10%)       " + "   -"+newUserDiscount +" $"+ "\n"+"\n"
                        + "   Three Items in Same Category Discount(20%)       " + "   -"+discount20 +" $"+ "</font></pre><br>"+"\n"+"\n"+"\n"
                        + "<pre style='font-family: Arial;'>"+"                                                             <b>Final Total     </b>   " + finalTotal +" $"+ "</pre></html>");
                panelLower1.add(costLabel);
            } else {
                panelLower.removeAll();
                JLabel costLabel2 = new JLabel("<html><pre><font face='Arial' style='font-weight: normal;'>"
                        +  "                                                                      Total        " + priceTotal +" $"+ "\n"+"\n"
                        +  "   Three Items in Same Category Discount(20%)     " + "   -"+discount20 +" $"+ "</pre><br>"+"\n"+"\n"+"\n"
                        +  "<pre style='font-family: Arial;'>"+"                                                            <b>Final Total     </b>   " + finalTotal +" $"+ "</pre></html>");
                panelLower1.add(costLabel2);
            }
            panelLower.add(panelLower1,BorderLayout.CENTER);
            panel1.add(panelLower);
            panel1.revalidate();
            panel1.repaint();
        });
    }

    public void remove(Product product){
        Iterator<Product> iterator = productList.iterator();

        while (iterator.hasNext()) {
            Product productItem = iterator.next();

            if (productItem.getProductID().equals(product.getProductID())) {
                iterator.remove();
                System.out.println("Product removed: " + product);
            }
        }
    }


    public double calculateTotal(boolean newUser,String name){
        double totalPrice = 0.0;
        int cloQuantity = 0;
        int eleQuantity = 0;
        int rowCount = tableCart.getRowCount();
        for (int rowIndex=0;rowIndex<rowCount;rowIndex++) {
            if (tableCart.getValueAt(rowIndex, 2) != null) {
                Double price = (Double) tableCart.getValueAt(rowIndex, 2);
                totalPrice = totalPrice + price;
                Product product = productList.get(rowIndex);
                if (product instanceof Clothing) {
                    cloQuantity = (int) tableCart.getValueAt(rowIndex, 1) + cloQuantity;
                } else if (product instanceof Electronics) {
                    eleQuantity = (int) tableCart.getValueAt(rowIndex, 1) + eleQuantity;
                }
            }
        }
        if (cloQuantity >=3 || eleQuantity>=3){
            this.discount20 = totalPrice*0.2;
        }else {
            this.discount20 = 0.0;
        }

        if(newUser == true){
            this.newUserDiscount = totalPrice*0.1;
        }else {
            this.newUserDiscount = 0.0;
        }
        System.out.println(totalPrice +discount20+ newUserDiscount);
        this.finalTotal = totalPrice - discount20 - newUserDiscount;
        this.priceTotal = totalPrice;
        return priceTotal;
    }
    public void showGUI_SC() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 480);
        setTitle("Shopping Cart");
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        Cart();
    }
}

