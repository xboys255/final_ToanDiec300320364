import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
/*
 * Created by JFormDesigner on Sat Aug 08 08:51:45 PDT 2020
 */


/**
 * @author Diec Tin Toan
 */
public class SavingForm {
    DefaultTableModel tableModel, interesttable;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    List<Savings> savingsinfo = new ArrayList<>();

    public SavingForm() {

        initComponents();
        tableModel = (DefaultTableModel) informationtable1.getModel();
        interesttable = (DefaultTableModel) interesttable2.getModel();
        informationtable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                interesttable.setRowCount(0);
                custNumtextField1.setText(informationtable1.getValueAt(informationtable1.getSelectedRow(), 0).toString());

                custNametextField2.setText(informationtable1.getValueAt(informationtable1.getSelectedRow(), 1).toString());
                deposittextField3.setText(informationtable1.getValueAt(informationtable1.getSelectedRow(), 2).toString());
                yeartextField4.setText(informationtable1.getValueAt(informationtable1.getSelectedRow(), 3).toString());

                if (informationtable1.getValueAt(informationtable1.getSelectedRow(), 4).toString().equalsIgnoreCase("Deluxe")) {


                    for (Integer i = 1; i < Integer.parseInt(yeartextField4.getText()); ++i) {

                        double money = Double.parseDouble(deposittextField3.getText());
                        double interest = money * 0.15;
                        double endingmoney = money + interest;

                        interesttable.addRow(new Object[]{i, deposittextField3.getText(), interest, endingmoney});
                    }
                } else if (informationtable1.getValueAt(informationtable1.getSelectedRow(), 4).toString().equalsIgnoreCase("Regular")) {
                    double money = Double.parseDouble(deposittextField3.getText());
                    double interest = money * 0.1;
                    double endingmoney = money + interest;

                    for (Integer i = 0; i < Integer.parseInt(yeartextField4.getText()); ++i) {

                        double interest1 = money * 0.1;
                        double endingmoney1 = money + interest;

                        interesttable.addRow(new Object[]{i+1, deposittextField3.getText(), interest, endingmoney});
                    }
                }


            }
        });
    }

    public void showData() {


        tableModel.setRowCount(0);

        Object[][] custData = new Object[100][5];
        Deluxe deluxes[] = new Deluxe[100];
        Regular regulars[] = new Regular[100];
        HashMap<String, Object> custdata = new HashMap<>();


        try {

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/savings", "root", "");
            statement = connection.createStatement();

            String sql = "select * from savingstable";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Savings savings = new Savings(resultSet.getString("custno"), resultSet.getString("custname"),
                        resultSet.getString("savtype"), resultSet.getDouble("cdep"), resultSet.getInt("nyears"));

                savingsinfo.add(savings);
            }

        } catch (SQLException exception) {
            Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            if (connection != null) {
                try {

                    connection.close();
                } catch (SQLException exception) {
                    Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
                }
            }
            if (statement != null) {
                try {

                    statement.close();
                } catch (SQLException exception) {
                    Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
                }
            }
        }


        for (Savings savings : savingsinfo) {

            tableModel.addRow(new Object[]{savings.getCustNum(), savings.getCustName(), savings.getDeposit(), savings.getYear(), savings.getSavType()});
        }

    }

    private void addbutton1MouseClicked(MouseEvent e) {
        // TODO add your code here
        try {
            if (custNametextField2.getText().trim().isEmpty() || custNumtextField1.getText().trim().isEmpty() || yeartextField4.getText().trim().isEmpty() ||
                    deposittextField3.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please input all data");

            } else {


                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/savings", "root", "");
                preparedStatement = connection.prepareStatement("Select * from savingstable where custno = ?");

                preparedStatement.setString(1, custNumtextField1.getText());
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.isBeforeFirst()) {

                    JOptionPane.showMessageDialog(null, "Your customer is already in the database");

                    custNametextField2.setText("");
                    custNumtextField1.setText("");
                    yeartextField4.setText("");
                    deposittextField3.setText("");

                    return;
                } else {

                    String sql = "insert into savingstable values (?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, custNumtextField1.getText());
                    preparedStatement.setString(2, custNametextField2.getText());
                    preparedStatement.setString(3, deposittextField3.getText());
                    preparedStatement.setString(4, yeartextField4.getText());
                    if (comboBox1.getSelectedIndex() == 0) {

                        preparedStatement.setString(5, "Deluxe");
                    } else if (comboBox1.getSelectedIndex() == 1) {
                        preparedStatement.setString(5, "Regular");
                    }

                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record added");

                    custNumtextField1.setText("");
                    custNametextField2.setText("");
                    yeartextField4.setText("");
                    deposittextField3.setText("");
                    showData();


                }

            }
        } catch (SQLException exception) {
            Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
        } finally {
            if (connection != null) {
                try {

                    connection.close();
                } catch (SQLException exception) {
                    Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
                }
            }
            if (preparedStatement != null) {
                try {

                    preparedStatement.close();
                } catch (SQLException exception) {
                    Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
                }
            }
        }

    }

    private void editbutton2MouseClicked(MouseEvent e) {
        // TODO add your code here
        if (custNametextField2.getText().trim().isEmpty() || custNumtextField1.getText().trim().isEmpty() || yeartextField4.getText().trim().isEmpty() ||
                deposittextField3.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please input all data");

        } else {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/savings", "root", "");
                preparedStatement = connection.prepareStatement("Select * from savingstable where custno = ?");

                preparedStatement.setString(1, custNumtextField1.getText());
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.isBeforeFirst()) {

                    JOptionPane.showMessageDialog(null, "Your customer is already in the database");

                    custNametextField2.setText("");
                    custNumtextField1.setText("");
                    yeartextField4.setText("");
                    deposittextField3.setText("");

                    return;
                } else {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/savings", "root", "");
                    preparedStatement = connection.prepareStatement("update savingstable set custno = ?, custname=?, cdep=?, nyears=?,savtype=? where name=?");

                    preparedStatement.setString(1, custNumtextField1.getText());
                    preparedStatement.setString(2, custNametextField2.getText());
                    preparedStatement.setString(3, deposittextField3.getText());
                    preparedStatement.setString(4, yeartextField4.getText());
                    if (comboBox1.getSelectedIndex() == 0) {

                        preparedStatement.setString(5, "Deluxe");
                    } else if (comboBox1.getSelectedIndex() == 1) {
                        preparedStatement.setString(5, "Regular");
                    }
                    preparedStatement.setString(6, custNametextField2.getText());

                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record updated");
                    custNumtextField1.setText("");
                    custNametextField2.setText("");
                    yeartextField4.setText("");
                    deposittextField3.setText("");
                    custNumtextField1.requestFocus();
                    showData();


                }
            } catch (SQLException exception) {
                Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
            } finally {
                if (connection != null) {
                    try {

                        connection.close();
                    } catch (SQLException exception) {
                        Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
                    }
                }
                if (preparedStatement != null) {
                    try {

                        preparedStatement.close();
                    } catch (SQLException exception) {
                        Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
                    }
                }
            }
        }
    }

    private void deletebutton3MouseClicked(MouseEvent e) {
        // TODO add your code here
        if (custNametextField2.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please input name to delete");
        } else {

            try {
                int respsone = JOptionPane.showConfirmDialog(mainPanel, "Do you really want to delete data?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (respsone == JOptionPane.YES_OPTION) {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/savings", "root", "");
                    preparedStatement = connection.prepareStatement("delete from savingstable where custname=?");
                    preparedStatement.setString(1, custNametextField2.getText());
                    showData();
                } else {
                    return;
                }


            } catch (SQLException exception) {
                Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
            } finally {
                if (connection != null) {
                    try {

                        connection.close();
                    } catch (SQLException exception) {
                        Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
                    }
                }
                if (preparedStatement != null) {
                    try {

                        preparedStatement.close();
                    } catch (SQLException exception) {
                        Logger.getLogger(SavingForm.class.getName()).log(Level.SEVERE, null, exception);
                    }
                }
            }
        }
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Diec Tin Toan
        mainPanel = new JPanel();
        custNumtextField1 = new JTextField();
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        custNametextField2 = new JTextField();
        deposittextField3 = new JTextField();
        yeartextField4 = new JTextField();
        comboBox1 = new JComboBox<>();
        scrollPane1 = new JScrollPane();
        informationtable1 = new JTable();
        addbutton1 = new JButton();
        editbutton2 = new JButton();
        deletebutton3 = new JButton();
        scrollPane2 = new JScrollPane();
        interesttable2 = new JTable();

        //======== mainPanel ========
        {
            mainPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new
            javax.swing.border.EmptyBorder(0,0,0,0), "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e",javax
            .swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM,new java
            .awt.Font("Dialo\u0067",java.awt.Font.BOLD,12),java.awt
            .Color.red),mainPanel. getBorder()));mainPanel. addPropertyChangeListener(new java.beans.
            PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("borde\u0072".
            equals(e.getPropertyName()))throw new RuntimeException();}});
            mainPanel.setLayout(null);
            mainPanel.add(custNumtextField1);
            custNumtextField1.setBounds(345, 25, 330, custNumtextField1.getPreferredSize().height);

            //---- label1 ----
            label1.setText("Enter the Customer Number");
            label1.setFont(new Font("Segoe UI", Font.BOLD, 12));
            mainPanel.add(label1);
            label1.setBounds(new Rectangle(new Point(35, 30), label1.getPreferredSize()));

            //---- label2 ----
            label2.setText("Enter the Customer Name");
            label2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            mainPanel.add(label2);
            label2.setBounds(35, 75, 136, 16);

            //---- label3 ----
            label3.setText("Enter the Initial Deposit");
            label3.setFont(new Font("Segoe UI", Font.BOLD, 12));
            mainPanel.add(label3);
            label3.setBounds(35, 120, 250, 16);

            //---- label4 ----
            label4.setText("Enter the number of years");
            label4.setFont(new Font("Segoe UI", Font.BOLD, 12));
            mainPanel.add(label4);
            label4.setBounds(35, 165, 136, 16);

            //---- label5 ----
            label5.setText("Choose the type of savings");
            label5.setFont(new Font("Segoe UI", Font.BOLD, 12));
            mainPanel.add(label5);
            label5.setBounds(35, 215, 136, 16);
            mainPanel.add(custNametextField2);
            custNametextField2.setBounds(345, 70, 330, 30);
            mainPanel.add(deposittextField3);
            deposittextField3.setBounds(350, 115, 325, 30);
            mainPanel.add(yeartextField4);
            yeartextField4.setBounds(350, 160, 325, 30);

            //---- comboBox1 ----
            comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                "Savings-Deluxe",
                "Savings-Regular"
            }));
            mainPanel.add(comboBox1);
            comboBox1.setBounds(350, 210, 325, comboBox1.getPreferredSize().height);

            //======== scrollPane1 ========
            {

                //---- informationtable1 ----
                informationtable1.setModel(new DefaultTableModel(
                    new Object[][] {
                    },
                    new String[] {
                        "Number", "Name", "Deposit", "Years", "Type of Savings"
                    }
                ));
                scrollPane1.setViewportView(informationtable1);
            }
            mainPanel.add(scrollPane1);
            scrollPane1.setBounds(5, 260, 335, 290);

            //---- addbutton1 ----
            addbutton1.setText("Add");
            addbutton1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addbutton1MouseClicked(e);
                    addbutton1MouseClicked(e);
                }
            });
            mainPanel.add(addbutton1);
            addbutton1.setBounds(15, 570, 88, addbutton1.getPreferredSize().height);

            //---- editbutton2 ----
            editbutton2.setText("Edit");
            editbutton2.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    editbutton2MouseClicked(e);
                }
            });
            mainPanel.add(editbutton2);
            editbutton2.setBounds(120, 570, 95, editbutton2.getPreferredSize().height);

            //---- deletebutton3 ----
            deletebutton3.setText("Delete");
            deletebutton3.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    deletebutton3MouseClicked(e);
                }
            });
            mainPanel.add(deletebutton3);
            deletebutton3.setBounds(235, 570, 100, deletebutton3.getPreferredSize().height);

            //======== scrollPane2 ========
            {

                //---- interesttable2 ----
                interesttable2.setModel(new DefaultTableModel(
                    new Object[][] {
                    },
                    new String[] {
                        "Year", "Starting", "Interest", "Ending Value"
                    }
                ));
                scrollPane2.setViewportView(interesttable2);
            }
            mainPanel.add(scrollPane2);
            scrollPane2.setBounds(350, 260, 350, 290);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < mainPanel.getComponentCount(); i++) {
                    Rectangle bounds = mainPanel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = mainPanel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                mainPanel.setMinimumSize(preferredSize);
                mainPanel.setPreferredSize(preferredSize);
            }
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Savings");
        jFrame.setContentPane(new SavingForm().mainPanel);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
//        git@github.com:xboys255/final_ToanDiec300320364.git
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Diec Tin Toan
    private JPanel mainPanel;
    private JTextField custNumtextField1;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JTextField custNametextField2;
    private JTextField deposittextField3;
    private JTextField yeartextField4;
    private JComboBox<String> comboBox1;
    private JScrollPane scrollPane1;
    private JTable informationtable1;
    private JButton addbutton1;
    private JButton editbutton2;
    private JButton deletebutton3;
    private JScrollPane scrollPane2;
    private JTable interesttable2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
