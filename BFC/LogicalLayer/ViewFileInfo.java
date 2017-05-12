package BFC;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import java.util.Map;
import java.util.HashMap;
public class ViewFileInfo extends JFrame{
	JPanel p1;
	Font f1;
	DefaultTableModel dtm;
	JTable table;
	JScrollPane jsp;
public ViewFileInfo(){
	super("View File Info Screen");
	f1 = new Font("Courier New",Font.BOLD,13);
	p1 = new JPanel();
	p1.setBackground(Color.white);
	p1.setLayout(new BorderLayout());
	dtm = new DefaultTableModel(){
		public boolean isCellEditable(int r,int c){
			return false;
		}
	};
	table = new JTable(dtm);
	table.setRowHeight(30);
	table.setFont(f1);
	table.getTableHeader().setFont(new Font("Courier New",Font.BOLD,14));
	dtm.addColumn("File Name");
	dtm.addColumn("File ID");
	dtm.addColumn("SHA String");
	dtm.addColumn("Refrence File");
	dtm.addColumn("Start Chunk ID");
	dtm.addColumn("Total Chunks");
	dtm.addColumn("File Size");
	dtm.addColumn("Status");
	jsp = new JScrollPane(table);
	jsp.getViewport().setBackground(Color.white);
	p1.add(jsp,BorderLayout.CENTER);
	getContentPane().add(p1,BorderLayout.CENTER);
}
public void clear(){
	for(int i=dtm.getRowCount()-1;i>=0;i--){
		dtm.removeRow(i);
	}
}
public void readFileInfo(HashMap<String,FileInfo> map){
	try{
		for(Map.Entry<String,FileInfo> entry : map.entrySet()){
			FileInfo fi = entry.getValue();
			Object row[] = {fi.filename,fi.file_id,fi.sha,fi.refrence_id,fi.start_chunk_id,fi.num_chunk,fi.file_size,fi.status};
			dtm.addRow(row);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
}