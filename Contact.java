import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Database {
    void intoDatabase(String name, String gender, String address, String phone, String email) {
        if (emailExists(email)) {
            JOptionPane.showMessageDialog(null, "Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "INSERT INTO details (name, gender, address, phone, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/contact?serverTimezone=UTC",
                "root", "");
                PreparedStatement st = con.prepareStatement(query)) {
            st.setString(1, name);
            st.setString(2, gender);
            st.setString(3, address);
            st.setString(4, phone);
            st.setString(5, email);
            int rowsInserted = st.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "A new user is inserted successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean emailExists(String email) {
        String sql = "SELECT email FROM details WHERE email = ?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/contact?serverTimezone=UTC",
                "root", "");
                PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    String[] searchDatabaseByEmail(String email) {
        String[] contactdetails = new String[5];
        String sql = "SELECT name, gender, address, phone, email FROM details WHERE email = ?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/contact?serverTimezone=UTC",
                "root", "");
                PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    contactdetails[0] = result.getString("name");
                    contactdetails[1] = result.getString("gender");
                    contactdetails[2] = result.getString("address");
                    contactdetails[3] = result.getString("phone");
                    contactdetails[4] = result.getString("email");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactdetails;
    }

    void updateDatabase(String name, String gender, String address, String phone, String email) {
        String query = "UPDATE details SET gender=?, address=?, phone=?, name=? WHERE email=?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/contact?serverTimezone=UTC",
                "root", "");
                PreparedStatement st = con.prepareStatement(query)) {
            st.setString(1, gender);
            st.setString(2, address);
            st.setString(3, phone);
            st.setString(4, name);
            st.setString(5, email);
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "User updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No user found with this email!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void deleteFromDatabase(String email) {
        String query = "DELETE FROM details WHERE email=?";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/contact?serverTimezone=UTC",
                "root", "");
                PreparedStatement st = con.prepareStatement(query)) {
            st.setString(1, email);
            int rowsDeleted = st.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "User deleted successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No user found with this email!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class Contact {
    public static void main(String[] args) {
        JFrame j1 = new JFrame();
        j1.setSize(400, 500);
        j1.setTitle("Contact Management");
        j1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create panels
        JPanel insertPanel = createInsertPanel();
        JPanel editPanel = createEditPanel();
        JPanel deletePanel = createDeletePanel();
        JPanel searchByEmailPanel = createSearchByEmailPanel();

        // Add panels to the tabbed pane
        tabbedPane.addTab("Insert", insertPanel);
        tabbedPane.addTab("Edit", editPanel);
        tabbedPane.addTab("Delete", deletePanel);
        tabbedPane.addTab("Search by Email", searchByEmailPanel);

        j1.add(tabbedPane);
        j1.setVisible(true);
    }

    private static JPanel createInsertPanel() {
        JPanel insertPanel = new JPanel();
        insertPanel.setLayout(new GridBagLayout());
        insertPanel.setBorder(BorderFactory.createTitledBorder("Insert New Contact"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField t1 = new JTextField(15); // Name
        JTextField t2 = new JTextField(15); // Address
        JTextField t3 = new JTextField(15); // Phone
        JTextField t4 = new JTextField(15); // Email for insert

        gbc.gridx = 0;
        gbc.gridy = 0;
        insertPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(t1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        insertPanel.add(new JLabel("Gender:"), gbc);
        JPanel genderPanel = new JPanel();
        JRadioButton maleButton = new JRadioButton("Male");
        JRadioButton femaleButton = new JRadioButton("Female");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        gbc.gridx = 1;
        insertPanel.add(genderPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        insertPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(t2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        insertPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(t3, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        insertPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        insertPanel.add(t4, gbc);

        JButton b1 = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        insertPanel.add(b1, gbc);

        b1.addActionListener(e -> {
            String name = t1.getText();
            String gender = maleButton.isSelected() ? "Male" : "Female";
            String address = t2.getText();
            String phone = t3.getText();
            String email = t4.getText();
            Database d1 = new Database();
            if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "COMPLETE THE FORM", "Warning Message",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                d1.intoDatabase(name, gender, address, phone, email);
                t1.setText("");
                t2.setText("");
                t3.setText("");
                t4.setText("");
                genderGroup.clearSelection();
            }
        });

        return insertPanel;
    }

    private static JPanel createEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        editPanel.setBorder(BorderFactory.createTitledBorder("Edit Contact"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField t6 = new JTextField(15); // Email for edit
        JTextField t7 = new JTextField(15); // Name
        JTextField t8 = new JTextField(15); // Address
        JTextField t9 = new JTextField(15); // Phone
        JTextField t10 = new JTextField(15); // Gender

        gbc.gridx = 0;
        gbc.gridy = 0;
        editPanel.add(new JLabel("Email for Edit:"), gbc);
        gbc.gridx = 1;
        editPanel.add(t6, gbc);

        JButton b2 = new JButton("Search");
        gbc.gridx = 2;
        editPanel.add(b2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        editPanel.add(t7, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        editPanel.add(t10, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        editPanel.add(t8, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        editPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        editPanel.add(t9, gbc);

        JButton b3 = new JButton("Update");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        editPanel.add(b3, gbc);

        b2.addActionListener(e -> {
            String email = t6.getText();
            Database d1 = new Database();
            String[] details = d1.searchDatabaseByEmail(email);
            if (details[0] != null) {
                t7.setText(details[0]);
                t10.setText(details[1]);
                t8.setText(details[2]);
                t9.setText(details[3]);
            } else {
                JOptionPane.showMessageDialog(null, "No user found with this email!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        b3.addActionListener(e -> {
            String email = t6.getText();
            String name = t7.getText();
            String gender = t10.getText();
            String address = t8.getText();
            String phone = t9.getText();

            if (name.isEmpty() || gender.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "COMPLETE THE FORM", "Warning Message",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                Database d1 = new Database();
                d1.updateDatabase(name, gender, address, phone, email);
            }
        });

        return editPanel;
    }

    private static JPanel createDeletePanel() {
        JPanel deletePanel = new JPanel();
        deletePanel.setLayout(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Contact"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField t11 = new JTextField(15); // Email for delete

        gbc.gridx = 0;
        gbc.gridy = 0;
        deletePanel.add(new JLabel("Email for Delete:"), gbc);
        gbc.gridx = 1;
        deletePanel.add(t11, gbc);

        JButton b4 = new JButton("Delete");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        deletePanel.add(b4, gbc);

        b4.addActionListener(e -> {
            String email = t11.getText();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter an email!", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                Database d1 = new Database();
                d1.deleteFromDatabase(email);
            }
        });

        return deletePanel;
    }

    private static JPanel createSearchByEmailPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new GridBagLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search by Email"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField t12 = new JTextField(15); // Email for search
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(t12, gbc);

        JButton searchButton = new JButton("Search");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        searchPanel.add(searchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        searchPanel.add(scrollPane, gbc);

        searchButton.addActionListener(e -> {
            String email = t12.getText();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter an email!", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                Database d1 = new Database();
                String[] details = d1.searchDatabaseByEmail(email);
                textArea.setText(""); // Clear previous search results
                if (details[0] != null) {
                    textArea.append("Name: " + details[0] + "\n");
                    textArea.append("Gender: " + details[1] + "\n");
                    textArea.append("Address: " + details[2] + "\n");
                    textArea.append("Phone: " + details[3] + "\n");
                    textArea.append("Email: " + details[4] + "\n");
                } else {
                    textArea.append("No user found with this email!\n");
                }
            }
        });

        return searchPanel;
    }
}