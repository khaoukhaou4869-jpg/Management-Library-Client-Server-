package IHM;

import model.Book;
import util.ResponseParser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class MainFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton loadButton;
    private JButton borrowButton;
    private JButton returnButton;
    private JButton addButton;
    private JButton deleteButton;
    private JTextField searchField;

    public MainFrame() {
        setTitle("Library System");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Table
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Title", "Author", "Available"}, 0
        );
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        // Button
        loadButton = new JButton("Load Books");
        loadButton.addActionListener(e -> loadBooks());

        borrowButton = new JButton("Borrow Book");
        borrowButton.addActionListener(e -> borrowBook());

        returnButton = new JButton("Return Book");
        returnButton.addActionListener(e -> returnBook());

        addButton = new JButton("Add Book");
        addButton.addActionListener(e -> addBook());

        deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(e -> deleteBook());

        searchField = new JTextField(20);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });

        //  Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(loadButton, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        JPanel searchPanel = new JPanel();

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));

        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topContainer.add(buttonPanel);
        topContainer.add(searchPanel);

        add(topContainer, BorderLayout.NORTH);
        topContainer.add(Box.createVerticalStrut(10));


        setVisible(true);
    }


    private void returnBook() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a book first!");
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);

        try (
                Socket socket = new Socket("localhost", 1234);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                )
        ) {
            // 🔹 send request
            out.println("RETURN|" + bookId);

            String response = in.readLine();

            if ("OK".equals(response)) {
                JOptionPane.showMessageDialog(this, "Book returned successfully ^_^");
                loadBooks();

            } else if ("ALREADY_RETURNED".equals(response)) {
                JOptionPane.showMessageDialog(this, "This book is already available.");

            } else {
                JOptionPane.showMessageDialog(this, "Error returning book");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void borrowBook() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a book first!");
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);

        try (
                Socket socket = new Socket("localhost", 1234);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                )
        ) {
            // 🔹 send request
            out.println("BORROW|" + bookId);

            String response = in.readLine();

            if ("OK".equals(response)) {
                JOptionPane.showMessageDialog(this, "Book borrowed successfully ^_^");
                loadBooks();

            } else if ("NOT_AVAILABLE".equals(response)) {
                JOptionPane.showMessageDialog(this, "This book isn't available.");

            } else {
                JOptionPane.showMessageDialog(this, "Error borrowing book");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Load books from server
    private void loadBooks() {
        try (
                Socket socket = new Socket("localhost", 1234);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                )
        ) {
            // Send request
            out.println("GET_BOOKS");

            //receive response
            String response = in.readLine();

            //      parse
            List<Book> books = ResponseParser.parseBooks(response);

            // Display in table
            updateTable(books);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addBook() {

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();

        Object[] message = {
                "Title:", titleField,
                "Author:", authorField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                message,
                "Add New Book",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {

            String title = titleField.getText();
            String author = authorField.getText();

            if (title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            try (
                    Socket socket = new Socket("localhost", 1234);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream())
                    );
                    PrintWriter out = new PrintWriter(
                            socket.getOutputStream(), true
                    )
            ) {
                out.println("ADD|" + title + "|" + author);

                String response = in.readLine();

                if ("OK".equals(response)) {
                    JOptionPane.showMessageDialog(this, "Book added successfully ^_^");
                    loadBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "Error adding book");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteBook() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a book first!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try (
                Socket socket = new Socket("localhost", 1234);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true)
        ) {
            out.println("DELETE|" + id);

            String response = in.readLine();

            if ("OK".equals(response)) {
                JOptionPane.showMessageDialog(this, "Book deleted!");
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting book");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterTable() {

        String text = searchField.getText().toLowerCase();

        TableRowSorter<DefaultTableModel> sorter =
                new TableRowSorter<>(tableModel);

        table.setRowSorter(sorter);

        if (text.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
        }
    }

    // 🔹 Update JTable
    private void updateTable(List<Book> books) {
        tableModel.setRowCount(0); // clear old data

        for (Book b : books) {
            tableModel.addRow(new Object[]{
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.isAvailable()
            });
        }
    }


    public static void main(String[] args) {
        new MainFrame();
    }
}