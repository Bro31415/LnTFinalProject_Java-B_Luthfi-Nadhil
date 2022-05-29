import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.sql.*;




public class main extends JFrame implements ActionListener, MouseListener{
    Connection con;
    ResultSet rs;
    Statement st;
    PreparedStatement ps;

    int row;

    JPanel
            northPanel, titlePanel,
            centerPanel, kodePanel, namaPanel, hargaPanel, stokPanel, buttonPanel,
            southPanel, tablePanel;
    JLabel
        titleLabel, kodeLabel, namaLabel, hargaLabel, stokLabel;

    JTextField
        kodeTF, namaTF, hargaTF, stokTF;

    JButton
        tambah, update, delete, view;

    JTable
        table;

    JScrollPane
        sp;

    DefaultTableModel
        model;


    public main(){
        frame();
        sqlcon();
        ui();
        setVisible(true);
    }

    public void frame() {
        setTitle("PT Pudding Database System");
        setLayout(new BorderLayout());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

    }

    public void ui() {

        northPanel = new JPanel();
        titlePanel = new JPanel();
        kodePanel = new JPanel(new FlowLayout());
        namaPanel = new JPanel(new FlowLayout());
        hargaPanel = new JPanel(new FlowLayout());
        stokPanel = new JPanel(new FlowLayout());
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        centerPanel = new JPanel(new GridLayout(5,1));
        southPanel = new JPanel();
        tablePanel = new JPanel();


        titleLabel =new JLabel("Database");
        namaLabel = new JLabel("Nama : ");
        hargaLabel = new JLabel("Harga : ");
        stokLabel = new JLabel("Stok : ");


        kodeTF = new JTextField();
        namaTF = new JTextField();
        hargaTF = new JTextField();
        stokTF = new JTextField();


        tambah = new JButton("Add");
        update = new JButton("Update");
        delete = new JButton("Delete");
        view = new JButton("View");


        tambah.addActionListener(this);
        update.addActionListener(this);
        delete.addActionListener(this);
        view.addActionListener(this);


        model = new DefaultTableModel();
        model.addColumn("Kode");
        model.addColumn("Nama");
        model.addColumn("Harga");
        model.addColumn("Stok");


        table = new JTable();
        table.setModel(model);
        sp = new JScrollPane(table);
        table.addMouseListener(this);


        titleLabel.setFont(titleLabel.getFont().deriveFont(20f));


        namaLabel.setPreferredSize(new Dimension(100,20));
        hargaLabel.setPreferredSize(new Dimension(100,20));
        stokLabel.setPreferredSize(new Dimension(100,20));


        namaTF.setPreferredSize(new Dimension(200,20));
        hargaTF.setPreferredSize(new Dimension(200,20));
        stokTF.setPreferredSize(new Dimension(200,20));


        tambah.setPreferredSize(new Dimension(80,20));
        update.setPreferredSize(new Dimension(80,20));
        delete.setPreferredSize(new Dimension(80,20));
        view.setPreferredSize(new Dimension(80,20));

        sp.setPreferredSize(new Dimension(300,100));


        titlePanel.add(titleLabel);
        northPanel.add(titlePanel);

        namaPanel.add(namaLabel);
        namaPanel.add(namaTF);

        hargaPanel.add(hargaLabel);
        hargaPanel.add(hargaTF);

        stokPanel.add(stokLabel);
        stokPanel.add(stokTF);

        buttonPanel.add(tambah);
        buttonPanel.add(view);
        buttonPanel.add(update);
        buttonPanel.add(delete);

        centerPanel.add(kodePanel);
        centerPanel.add(namaPanel);
        centerPanel.add(hargaPanel);
        centerPanel.add(stokPanel);
        centerPanel.add(buttonPanel);

        tablePanel.add(sp);
        southPanel.add(tablePanel);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

    }


    public void sqlcon(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/pudding_db", "root", "");
            st = con.createStatement();

        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Connection Error");
            e.printStackTrace();
        }
    }


    public void view(){
        try{
            String sql = "SELECT * FROM `menu`";

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()){
                model.addRow(new Object[]{
                    rs.getString("KODE"),
                    rs.getString("NAMA"),
                    rs.getString("HARGA"),
                    rs.getString("STOK"),
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String randomcode(){
        Random code = new Random();

        String prefix = "PD-";
        int rand_code1 = code.nextInt(100,999);
        String rand_code2 = prefix + rand_code1;

        return rand_code2;
    }

    public void add_row(String kode, String nama, String harga, String stok){
        Object new_row[] = {kode, nama, harga, stok};
        model.addRow(new_row);
    }


    public void add(){
        try{
            String sql = "INSERT INTO `menu`(`KODE`, `NAMA`, `HARGA`, `STOK`)" + "VALUES((?),(?),(?),(?))";

            String kode = randomcode();
            String nama = namaTF.getText();
            String harga = hargaTF.getText();
            String stok = stokTF.getText();

            ps = con.prepareStatement(sql);
            ps.setString(1, kode);
            ps.setString(2, nama);
            ps.setString(3, harga);
            ps.setString(4, stok);

            if(namaTF.getText().isEmpty() || hargaTF.getText().isEmpty() || stokTF.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please fill in the field!");

            }else{
                ps.execute();

                add_row(kode, nama, harga, stok);
                JOptionPane.showMessageDialog(null, "New entry added");
                clearform();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void delete(){
        try{
            String sql = "DELETE FROM `menu` WHERE KODE = '" + kodeTF.getText() + "'";
            ps  = con.prepareStatement(sql);
            ps.execute();

            clear();

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public void change_entry(String harga, String stok){
        model.setValueAt(harga, row, 2);
        model.setValueAt(stok, row, 3);
    }
    public void update(){
        try {
            String sql = "UPDATE `menu` SET `HARGA`= ? ,`STOK`=? WHERE KODE = '" + kodeTF.getText() + "'";

            String harga = hargaTF.getText();
            String stok = stokTF.getText();

            ps = con.prepareStatement(sql);
            ps.setString(1, harga);
            ps.setString(2, stok);

            if(hargaTF.getText().isEmpty() || stokTF.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please update new stock & price");
            } else {
                ps.execute();
                change_entry(harga, stok);
                JOptionPane.showMessageDialog(null, "Stock & Price updated!");
                clearform();
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void clearform(){
        kodeTF.setText(null);
        namaTF.setText(null);
        hargaTF.setText(null);
        stokTF.setText(null);
    }
    public void clear(){
        model.removeRow(row);
        kodeTF.setText(null);
        namaTF.setText(null);
        hargaTF.setText(null);
        stokTF.setText(null);
    }

    public static void main(String[] args) {
        new main();

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == tambah){
            add();
        } else if(e.getSource() == view) {
            view();
        } else if(e.getSource() == delete){
            delete();
        } else if(e.getSource() == update){
            update(); // Masih rusak
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == table) {
            row = table.getSelectedRow();

            String kode = table.getValueAt(row, 0).toString();
            kodeTF.setText(kode);

            String nama = table.getValueAt(row, 1).toString();
            namaTF.setText(nama);

            String harga = table.getValueAt(row, 2).toString();
            hargaTF.setText(harga);

            String stok = table.getValueAt(row, 3).toString();
            stokTF.setText(stok);

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
