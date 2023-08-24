import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Action extends JFrame {
    private List<Peserta> pesertaList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTable pesertaTable;
    private JButton addButton, deleteButton, updateButton;

    private JTextField namaField, alamatField, teleponField;

    public Action() {
        setTitle("Data Peserta Bootcamp");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        //inisialisasi button
        addButton = new JButton("Tambah");
        deleteButton = new JButton("Hapus");
        updateButton = new JButton("Update");
        //tabel & kolom
        String[] columnNames = {"Nama", "Alamat", "Telepon"};
        tableModel = new DefaultTableModel(columnNames, 0);
        pesertaTable = new JTable(tableModel);
        //panel button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        //tampilkan form input
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInputForm();
            }
        });
        //default button non aktif
        deleteButton.setEnabled(false);
        updateButton.setEnabled(false);
        //hapus data terpilih
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deletePeserta();
            }
        });
        //update data terplih
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePeserta();
            }
        });

        //update status button sesuai row
        pesertaTable.getSelectionModel().addListSelectionListener(e -> updateButtonsState());

        JScrollPane scrollPane = new JScrollPane(pesertaTable);
        //panel utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void deletePeserta() {
        int selectedRowIndex = pesertaTable.getSelectedRow(); //ambil indeks
        if (selectedRowIndex != -1) { //jika ada baris yang dipilih
            pesertaList.remove(selectedRowIndex); //hapus dari daftar
            tableModel.removeRow(selectedRowIndex); //hapus dari tabel
            clearFields();
            updateButtonsState();
        }
    }

    private void updatePeserta() {
        int selectedRowIndex = pesertaTable.getSelectedRow();
        if (selectedRowIndex != -1) {
            Peserta peserta = pesertaList.get(selectedRowIndex);
            //panel untuk update
            JPanel updatePanel = new JPanel(new GridLayout(3, 2));
            updatePanel.add(new JLabel("Nama:"));
            namaField = new JTextField(peserta.getNama(), 20);
            updatePanel.add(namaField);
            updatePanel.add(new JLabel("Alamat:"));
            alamatField = new JTextField(peserta.getAlamat(), 20);
            updatePanel.add(alamatField);
            updatePanel.add(new JLabel("Telepon:"));
            teleponField = new JTextField(peserta.getTelepon(), 20);
            updatePanel.add(teleponField);

            int result = JOptionPane.showConfirmDialog(null, updatePanel, "Update Peserta",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newNama = namaField.getText();
                String newAlamat = alamatField.getText();
                String newTelepon = teleponField.getText();

                if (!newNama.isEmpty()) {
                    peserta.setNama(newNama);
                }
                if (!newAlamat.isEmpty()) {
                    peserta.setAlamat(newAlamat);
                }
                if (!newTelepon.isEmpty()) {
                    peserta.setTelepon(newTelepon);
                }
                //update data dalam tabel
                tableModel.setValueAt(peserta.getNama(), selectedRowIndex, 0);
                tableModel.setValueAt(peserta.getAlamat(), selectedRowIndex, 1);
                tableModel.setValueAt(peserta.getTelepon(), selectedRowIndex, 2);

                clearFields();
                updateButtonsState();
            }
        }
    }

    //kosongkan input field
    private void clearFields() {
        namaField.setText("");
        alamatField.setText("");
        teleponField.setText("");
    }

    //atur button update&delete
    private void updateButtonsState() {
        int selectedRowIndex = pesertaTable.getSelectedRow();
        deleteButton.setEnabled(selectedRowIndex != -1);
        updateButton.setEnabled(selectedRowIndex != -1);
    }

    //form tambah
    private void showInputForm() {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Nama:"));
        namaField = new JTextField(20);
        inputPanel.add(namaField);
        inputPanel.add(new JLabel("Alamat:"));
        alamatField = new JTextField(20);
        inputPanel.add(alamatField);
        inputPanel.add(new JLabel("Telepon:"));
        teleponField = new JTextField(20);
        inputPanel.add(teleponField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Tambah Peserta",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String nama = namaField.getText().trim();
            String alamat = alamatField.getText().trim();
            String telepon = teleponField.getText().trim();

            if (nama.isEmpty() || alamat.isEmpty() || telepon.isEmpty()) {
                String errorMessage = "Data berikut tidak boleh kosong:\n";

                if (nama.isEmpty()) {
                    errorMessage += "- Nama\n";
                }
                if (alamat.isEmpty()) {
                    errorMessage += "- Alamat\n";
                }
                if (telepon.isEmpty()) {
                    errorMessage += "- Telepon\n";
                }

                JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            } else if (nama.replaceAll("\\s+", "").isEmpty() || alamat.replaceAll("\\s+", "").isEmpty() || telepon.replaceAll("\\s+", "").isEmpty()) {
                JOptionPane.showMessageDialog(null, "Data tidak boleh hanya berisi spasi!", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!telepon.matches("\\d+")) {
                JOptionPane.showMessageDialog(null, "Nomor telepon harus berisi angka saja!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Peserta peserta = new Peserta(nama, alamat, telepon);
                pesertaList.add(peserta);
                tableModel.addRow(new Object[]{nama, alamat, telepon});
                clearFields();
            }
        }
    }
}
