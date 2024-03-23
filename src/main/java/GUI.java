import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUI extends JFrame {
    private TableNew table1;
    private String selected;
    private Product cartProduct;
    private int quantity;
    private JTable tableP;
    private JLabel detailsLabel;

    public void shoppingGUI(WestminsterShoppingManager shoppingManager, boolean newUser,String name) {
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        JPanel mainUpper = new JPanel(new BorderLayout());
        JPanel mainUpper1 = new JPanel(new BorderLayout());
        JPanel buttonToRight = new JPanel();
        //Shopping Cart button
        JButton shoppingCart = new JButton("Shopping cart");
        ShoppingCart cart = new ShoppingCart(cartProduct);
        buttonToRight.add(shoppingCart);
        mainUpper1.add(buttonToRight,BorderLayout.EAST);
        mainUpper.add(mainUpper1, BorderLayout.NORTH);

        JPanel categoryPanel = new JPanel(new FlowLayout());
        JLabel category = new JLabel("Select Product Category"); // Use the instance variable
        categoryPanel.add(category);
        JPanel categorySelection = new JPanel(new BorderLayout());
        String[] items = {"All", "Electronics", "Clothing"};
        //Drop down menu
        JComboBox<String> comboBox = new JComboBox<>(items);
        categorySelection.add(comboBox, BorderLayout.CENTER);
        categoryPanel.add(categorySelection);
        mainUpper.add(categoryPanel, BorderLayout.CENTER);
        add(mainUpper);

        JPanel panelM = new JPanel(new BorderLayout());
        JPanel mainUpper2 = new JPanel(new FlowLayout());
        JPanel check1 = new JPanel();
        JPanel check2 = new JPanel();
        mainUpper2.add(check1);
        table1 = new TableNew(shoppingManager);
        //Creating JTable with custom model
        tableP = new JTable(table1);
        tableP.setPreferredScrollableViewportSize(new Dimension(550, 125));
        tableP.setRowHeight(25);
        TableColumn info = tableP.getColumnModel().getColumn(4);
        info.setPreferredWidth(175);
        //Center cell data
        JTableHeader headers = tableP.getTableHeader();
        headers.setPreferredSize(new Dimension(headers.getHeight(), 25 ));
        headers.setFont(new Font("Arial",Font.BOLD,12));
        tableP.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tableP);
        scrollPane.setVisible(true);
        mainUpper2.add(scrollPane);
        mainUpper2.add(check2);
        panelM.add(mainUpper2,BorderLayout.NORTH);
        JPanel p2 = new JPanel();
        panelM.add(p2,BorderLayout.SOUTH);
        mainUpper.add(panelM,BorderLayout.SOUTH);
        mainPanel.add(mainUpper);
        add(mainPanel);

        JPanel mainLower = new JPanel(new BorderLayout());
        JPanel barPanel = new JPanel();
        barPanel.setPreferredSize(new Dimension(650,1));
        barPanel.setBackground(Color.BLACK);
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(85,299));
        mainLower.add(barPanel,BorderLayout.NORTH);
        mainLower.add(sidePanel,BorderLayout.WEST);
        JPanel mainLowerPanel1 = new JPanel(new BorderLayout());
        JPanel informationPanel = new JPanel();
        detailsLabel = new JLabel();//details label
        informationPanel.add(detailsLabel);
        mainLowerPanel1.add(informationPanel, BorderLayout.WEST);
        mainLower.add(mainLowerPanel1,BorderLayout.CENTER);
        JPanel  mainLowerPanel2= new JPanel(new FlowLayout());
        JPanel buttonToCenter = new JPanel();
        JButton addToCart = new JButton("Add to Shopping Cart");//Add to cart button
        buttonToCenter.add(addToCart);
        mainLowerPanel2.add(buttonToCenter);
        mainLower.add(mainLowerPanel2,BorderLayout.SOUTH);
        add(mainLower);
        mainPanel.add(mainLower);
        getContentPane().add(mainPanel);
        setVisible(true);
        shoppingCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cart.showGUI_SC();
            }
        });
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // Get the selected item
                selected = (String) comboBox.getSelectedItem();
                table1.filterCategory(selected);
            }
        });
        tableP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tableP.getSelectedRow();
                if (selectedRow != -1) {
                    Clothing cloSelectedProduct = null;
                    Electronics elePro = null;
                    String id = (String) table1.getValueAt(selectedRow, 0);
                    String name = (String) table1.getValueAt(selectedRow, 1);
                    double price = Double.parseDouble((String) table1.getValueAt(selectedRow, 3));
                    String category = (String) table1.getValueAt(selectedRow, 2);
                    if (category.equals("Clothing")) {
                        selected = (String) comboBox.getSelectedItem();
                        if (table1.getSelected() == "All") {
                            cloSelectedProduct = (Clothing) table1.getList().get(selectedRow);
                        } else if (selected.equals("Clothing")) {
                            table1.filterCategory(selected);
                            cloSelectedProduct = (Clothing) table1.getClothingList().get(selectedRow);
                        }
                        if (cloSelectedProduct != null) {
                            String info = (String) table1.getValueAt(selectedRow, 4);
                            String[] partsNew = info.split(",");
                            String size = partsNew[0].trim();
                            String color = partsNew[1].trim();
                            Clothing cloProduct = new Clothing(id, name, cloSelectedProduct.getNoOfAvailableItems(), price, size, color);
                            updateClothingLabel(cloProduct,informationPanel,cloSelectedProduct.getNoOfAvailableItems());
                        }
                    } else {
                        selected = (String) comboBox.getSelectedItem();
                        if (table1.getSelected() == "All") {
                            elePro = (Electronics) table1.getList().get(selectedRow);
                        } else if (selected.equals("Electronics")) {
                            table1.filterCategory(selected);
                            elePro = (Electronics) table1.getElectronicList().get(selectedRow);
                        }
                        if (elePro != null) {
                            String info = (String) table1.getValueAt(selectedRow, 4);
                            String[] partsNew = info.split(",");
                            String brand = partsNew[0].trim();
                            String warranty = partsNew[1].trim();
                            Electronics eProduct = new Electronics(id, name, elePro.getNoOfAvailableItems(), price, brand, warranty);
                            updateElectronicLabel(eProduct,informationPanel, elePro.getNoOfAvailableItems());
                        }
                    }
                }
            }
        });
        addToCart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                quantity = e.getClickCount();
                int selectedRow = tableP.getSelectedRow();
                if (selectedRow != -1) {
                    String category = (String) table1.getValueAt(selectedRow, 2);
                    Clothing cloSelectedP = null;
                    Electronics eSelectedP = null;
                    if (category.equals("Clothing")) {
                        selected = (String) comboBox.getSelectedItem();
                        if (selected.equals("All")) {
                            cloSelectedP = (Clothing) table1.getList().get(selectedRow);
                        } else if (selected.equals("Clothing")) {
                            table1.filterCategory(selected);
                            cloSelectedP = (Clothing) table1.getClothingList().get(selectedRow);
                        }
                        if (cloSelectedP != null) {
                            double price = Double.parseDouble((String) table1.getValueAt(selectedRow, 3));
                            double totalPrice = price * quantity;
                            int availableItems = cloSelectedP.getNoOfAvailableItems() - quantity;
                            if (availableItems >=0 ){
                                cloSelectedP.setNoOfAvailableItems(availableItems);
                                Clothing cloProduct = new Clothing(cloSelectedP.getProductID(), cloSelectedP.getProductName(),
                                        quantity, totalPrice, cloSelectedP.getSize(), cloSelectedP.getColor());
                                updateClothingLabel(cloSelectedP,informationPanel,availableItems);
                                cart.add(cloProduct, quantity,newUser,name);
                                shoppingManager.saveFile();
                            } else  {
                                cloSelectedP.setNoOfAvailableItems(availableItems);
                                updateClothingLabel(cloSelectedP,informationPanel,availableItems);
                            }
                        }
                    } else {
                        selected = (String) comboBox.getSelectedItem();
                        if (selected.equals("All")) {
                            eSelectedP = (Electronics) table1.getList().get(selectedRow);
                        } else if (selected.equals("Electronics")) {
                            table1.filterCategory(selected);
                            eSelectedP = (Electronics) table1.getElectronicList().get(selectedRow);
                        }
                        if (eSelectedP != null) {
                            double price = Double.parseDouble((String) table1.getValueAt(selectedRow, 3));
                            double totalPrice = price * quantity;
                            int availableItems = eSelectedP.getNoOfAvailableItems() - quantity;
                            if (availableItems >=0){
                                eSelectedP.setNoOfAvailableItems(availableItems);
                                Electronics eProduct = new Electronics(eSelectedP.getProductID(), eSelectedP.getProductName(),
                                        quantity, totalPrice, eSelectedP.getBrand(), eSelectedP.getWarrantyPeriod());
                                updateElectronicLabel(eSelectedP,informationPanel,availableItems);
                                cart.add(eProduct,quantity, newUser,name);
                                shoppingManager.saveFile();
                            }else {
                                eSelectedP.setNoOfAvailableItems(availableItems);
                                updateElectronicLabel(eSelectedP,informationPanel,availableItems);
                            }
                        }
                    }
                }
            }
        });

    }
    public void updateElectronicLabel(Electronics product, JPanel panel,int item){
        if (item > 0){
            detailsLabel = new JLabel("<html><pre style='font-family: Arial;'>"+"<b>Selected Product - Details:</b></pre>"
                    + "<pre><font face='Arial' style='font-weight: normal;'>Product ID: " + product.getProductID() + "\n"+ "\n"
                    + "Category: " + product.getClass() + "\n"+ "\n"
                    + "Name: " + product.getProductName() + "\n"+ "\n"
                    + "Brand: " + product.getBrand() + "\n"+ "\n"
                    + "Warranty Period: " + product.getWarrantyPeriod() + "\n"+ "\n"
                    + "Items Available: " + product.getNoOfAvailableItems() + "</font></pre></html>");
        }else {
            detailsLabel = new JLabel("<html><pre style='font-family: Arial;'>"+"<b>Selected Product - Details:</b></pre>"
                    + "<pre><font face='Arial' style='font-weight: normal;'>Product ID: " + product.getProductID() + "\n"+ "\n"
                    + "Category: " + product.getClass() + "\n"+ "\n"
                    + "Name: " + product.getProductName() + "\n"+ "\n"
                    + "Brand: " + product.getBrand() + "\n"+ "\n"
                    + "Warranty Period: " + product.getWarrantyPeriod() + "\n"+ "\n"
                    + "Items Available: " + "No items available.." + "</font></pre></html>");
        }
        detailsLabel.setFont(new Font("Arial",Font.PLAIN,12));
        panel.removeAll(); // Clear previous content
        panel.add(detailsLabel);
        panel.revalidate();
        panel.repaint();
    }
    public void updateClothingLabel(Clothing product, JPanel panel,int item){
        if (item > 0){
            detailsLabel = new JLabel("<html><pre style='font-family: Arial;'>"+"<b>Selected Product - Details:</b>"
                    + "<pre><font face='Arial' style='font-weight: normal;'>Product ID: " + product.getProductID() + "\n"+ "\n"
                    + "Category: " + product.getClass() + "\n"+ "\n"
                    + "Name: " + product.getProductName() + "\n"+ "\n"
                    + "Size: " + product.getSize() + "\n"+ "\n"
                    + "Color: " + product.getColor() + "\n"+ "\n"
                    + "Items Available: " + product.getNoOfAvailableItems() + "</font></pre></html>");
        }else {
            detailsLabel = new JLabel("<html><pre style='font-family: Arial;'>"+"<b>Selected Product - Details:</b>"
                    + "<pre><font face='Arial' style='font-weight: normal;'>Product ID: " + product.getProductID() + "\n"+ "\n"
                    + "Category: " + product.getClass() + "\n"+ "\n"
                    + "Name: " + product.getProductName() + "\n"+ "\n"
                    + "Size: " + product.getSize() + "\n"+ "\n"
                    + "Color: " + product.getColor() + "\n"+ "\n"
                    + "Items Available: " + "No items available.." + "</font></pre></html>");
        }
        panel.removeAll(); // Clear previous content
        panel.add(detailsLabel);
        panel.revalidate();
        panel.repaint();
    }
    private class  TableNew extends AbstractTableModel {
        private String[] columnNames = {"Product ID", "Name", "Category", "Price($)", "Info"};
        private ArrayList<Product> list;
        private WestminsterShoppingManager shoppingManager;
        private String selected;
        private ArrayList<Product> electronicList = new ArrayList<Product>();
        private ArrayList<Product> clothingList = new ArrayList<Product>();

        public TableNew(WestminsterShoppingManager shoppingManager) {
            this.shoppingManager = shoppingManager;
            this.list = shoppingManager.getProducts();
            this.selected = "All";
            tableP = new JTable(this);
        }
        public ArrayList<Product> getList() {
            return list;
        }
        public String getSelected() {
            return selected;
        }
        public ArrayList<Product> getElectronicList() {
            return electronicList;
        }
        public ArrayList<Product> getClothingList() {
            return clothingList;
        }
        public ArrayList<Product> filterCategory(String selectedCategory){

            if ("All".equals(selectedCategory)){
                list = shoppingManager.getProducts();
                selected=selectedCategory;
                fireTableDataChanged();
            }else if ("Electronics".equals(selectedCategory)) {
                electronicList.clear();
                for (Product product : shoppingManager.getProducts()) {
                    if (product instanceof Electronics) {
                        electronicList.add(product);
                    }
                }
                list = new ArrayList<>(electronicList);
                selected=selectedCategory;
                fireTableDataChanged();
            } else if ("Clothing".equals(selectedCategory)) {
                clothingList.clear();
                for (Product product:shoppingManager.getProducts()){
                    if (product instanceof Clothing) {
                        clothingList.add(product);
                    }
                }
                list = new ArrayList<>(clothingList);
                selected=selectedCategory;
                fireTableDataChanged();
            }
            return list;
        }

        @Override
        public int getRowCount() {
            return Math.max(5,list.size());
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getValueAt(int rowIndex, int columnIndex) {
            CustomTableRenderer renderer = new CustomTableRenderer(selected);
            if (rowIndex < list.size()) {
                Product product = switch (selected) {
                    case "All" -> list.get(rowIndex);
                    case "Electronics" -> electronicList.get(rowIndex);
                    case "Clothing" -> clothingList.get(rowIndex);
                    default -> null;
                };
                if ( product != null){
                    if (columnIndex == 0) {
                        tableP.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
                        return product.getProductID();
                    } else if (columnIndex == 1) {
                        tableP.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
                        return product.getProductName();
                    } else if (columnIndex == 2) {
                        tableP.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
                        return product.getClass().getSimpleName();
                    } else if (columnIndex == 3) {
                        tableP.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
                        return String.valueOf(product.getPrice());
                    } else if (columnIndex == 4) {
                        if (product instanceof Electronics) {
                            Electronics eProduct = (Electronics) product;
                            tableP.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
                            return eProduct.getBrand() + "," + eProduct.getWarrantyPeriod();
                        } else if (product instanceof Clothing) {
                            Clothing cItem = (Clothing) product;
                            tableP.getColumnModel().getColumn(columnIndex).setCellRenderer(renderer);
                            return cItem.getSize() + "," + cItem.getColor();
                        }
                    }
                }
            }else {
                return "";
            }
            return null;
        }
        public String getColumnName(int col) {
            return columnNames[col];
        }
    }
    public class CustomTableRenderer extends DefaultTableCellRenderer{
        private String selected;
        public CustomTableRenderer(String selected) {
            this.selected = selected;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table,Object value, boolean isSelected, boolean hasFocus,int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            ((JLabel) component).setHorizontalAlignment(SwingConstants.CENTER);
            TableNew tableModel = (TableNew) table.getModel();
            Product product = getProductRow(tableModel,row);
            changeColor(product,component,table);
            return component;
        }
        private Product getProductRow(TableNew table,int row){
            if ("All".equals(selected)){
                if(row<table.getList().size()) {
                    return table.getList().get(row);
                }
            } else if ("Electronics".equals(selected)) {
                if(row<table.getElectronicList().size()) {
                    return table.getElectronicList().get(row);
                }
            } else if ("Clothing".equals(selected)) {
                if(row<table.getClothingList().size()) {
                    return table.getClothingList().get(row);
                }
            }
            return null;
        }
        private void changeColor(Product product,Component component,JTable table){
            if (product!=null && product.getNoOfAvailableItems() < 3){
                component.setBackground(Color.red);
            }else {
                component.setBackground(null);
            }
            if (table != null){
                table.repaint();
            }

        }
    }
    public void showGUI(ArrayList<Product> products, boolean newUser,String name) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 630);
        setTitle("Westminster Shopping Centre");
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        shoppingGUI(new WestminsterShoppingManager(products),newUser,name);
    }
}

