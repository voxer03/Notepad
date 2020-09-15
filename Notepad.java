import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class Notepad extends JFrame implements ActionListener{
	
	private JScrollPane sp;
	private JTextArea area;	
	private JMenuBar mb;
	
	private JMenu file;
	private JMenuItem nw;
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem saveas;
	private JMenuItem exit;
	
	private JMenu edit;
	private JMenuItem redo;
	private JMenuItem undo;
	private JMenuItem cut;
	private JMenuItem copy;
	private JMenuItem paste;
	
	private JMenu format;
	private JCheckBoxMenuItem wrap;
//private JMenuItem font;
	
	private JMenu help;
	private JMenuItem about;
	
	private JPopupMenu pop; 
	private JMenuItem selectall;
	private JMenuItem ct;
	private JMenuItem cpy;
	private JMenuItem pste;
	
	UndoManager um;
	
	Notepad(){
		super("Notepad");
		
		isWrap=false;
		um=new UndoManager();
		area=new JTextArea();
		area.setFont(new Font("Arial", Font.PLAIN, 14));
		area.getDocument().addUndoableEditListener(
				new UndoableEditListener() {
					public void undoableEditHappened(UndoableEditEvent ee) {
						um.addEdit(ee.getEdit());
					}
				});
		
		
		sp=new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
		sp.setBorder(BorderFactory.createEmptyBorder());
		add(sp);	
		
		pop = new JPopupMenu();
		ct= new JMenuItem("Cut");
		cpy= new JMenuItem("Copy");
		pste= new JMenuItem("Paste   ");
		selectall=new JMenuItem("Select All");
		
		ct.addActionListener(this);
		pop.add(ct);
		cpy.addActionListener(this);
		pop.add(cpy);
		pste.addActionListener(this);
		pop.add(pste);
		pop.addSeparator();
		selectall.addActionListener(this);
		pop.add(selectall);
		
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\\\java\\\\eclipse\\\\notepad2\\\\src\\\\notepad.png"));
				
		mb=new JMenuBar();
		
		file= new JMenu("File");
		
		nw=new JMenuItem("New");
		open=new JMenuItem("Open..");
		save=new JMenuItem("Save");
		saveas=new JMenuItem("Save As  ");
		exit=new JMenuItem("Exit");
		
		nw.addActionListener(this);
		nw.setAccelerator(KeyStroke.getKeyStroke('N', CTRL_DOWN_MASK));
		file.add(nw);
		open.addActionListener(this);
		file.add(open);
		file.addSeparator();
		save.addActionListener(this);
		save.setAccelerator(KeyStroke.getKeyStroke('S', CTRL_DOWN_MASK ));
		file.add(save);
		saveas.addActionListener(this);
		file.add(saveas);
		file.addSeparator();
		exit.addActionListener(this);
		file.add(exit);

		mb.add(file);

		edit=new JMenu("Edit");
		
		redo=new JMenuItem("Redo");
		undo= new JMenuItem("Undo");
		cut= new JMenuItem("Cut");
		copy= new JMenuItem("Copy");
		paste= new JMenuItem("Paste   ");
		
		redo.addActionListener(this);
		redo.setAccelerator(KeyStroke.getKeyStroke('Y', CTRL_DOWN_MASK ));
		edit.add(redo);
		undo.addActionListener(this);
		undo.setAccelerator(KeyStroke.getKeyStroke('Z', CTRL_DOWN_MASK ));
		edit.add(undo);
		edit.addSeparator();
		cut.addActionListener(this);
		cut.setAccelerator(KeyStroke.getKeyStroke('X', CTRL_DOWN_MASK ));
		edit.add(cut);
		copy.addActionListener(this);
		copy.setAccelerator(KeyStroke.getKeyStroke('C', CTRL_DOWN_MASK ));
		edit.add(copy);
		paste.addActionListener(this);
		paste.setAccelerator(KeyStroke.getKeyStroke('V', CTRL_DOWN_MASK ));
		edit.add(paste);
		
		mb.add(edit);

		format=new JMenu("Format");														
		wrap=new JCheckBoxMenuItem("Word Wrap");
		//font=new JMenuItem("Font..");
		
		wrap.addActionListener(this);
		format.add(wrap);
		//font.addActionListener(this);
		//format.add(font);
		
		mb.add(format);

		help= new JMenu("Help");
		
		about=new JMenuItem("About Notepad ");
		about.addActionListener(this);
		help.add(about);
		mb.add(help);
		
		area.addMouseListener(new MouseAdapter() {
			public void mouseReleased (MouseEvent me) {
				if (me.getButton()==MouseEvent.BUTTON3) {
					pop.show(me.getComponent(),me.getX(),me.getY());
				}
			}
		});
		
 		setJMenuBar(mb);	
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	@Override 
	public void actionPerformed(ActionEvent e){
		JMenuItem mi= (JMenuItem) e.getSource();
		
		if(mi==nw) {
			setTitle("Untitled.txt");
			area.setText("");
			filename=null ;
			fileadd=null ;
		}
		else if(mi==open) {
			FileDialog fd=new FileDialog(this,"Open",FileDialog.LOAD);
			fd.setVisible(true);
			if(fd.getFile()!=null) {
				filename=fd.getFile();
				fileadd=fd.getDirectory();
				setTitle(filename);
			}
			try {
				BufferedReader br=new BufferedReader(new FileReader(fileadd+filename));
				area.setText("");
				String line;
				while ((line = br.readLine())!=null) {
					area.append(line+"\n");
				}
				br.close();
			}
			catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "There was problem in Accessing the File","Error",JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(mi==save) {
			if (filename ==null) {
				saveAs();
			}
			else {
				try {
					FileWriter fw= new FileWriter(fileadd+filename);
					fw.write(area.getText());
					setTitle(filename);
					fw.close();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(this, "There was problem in Accessing the File","Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if(mi==saveas) {
			saveAs();
		}
		else if(mi==wrap) {
			if(isWrap==false) {
				isWrap=true;
				area.setLineWrap(true);
				area.setWrapStyleWord(true);
				mi.setSelected(true);
			}
			else {
				isWrap=false;
				area.setLineWrap(false);
				area.setWrapStyleWord(false);
				mi.setSelected(false);				
			}
		}
		else if(mi==undo) {
			try {
				um.undo();
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "All Undo operations done");
			}
		}
		else if(mi==redo) {
			try {
				um.redo();
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(this, "All Redo operations done");
			}
		}
		else if(mi==about) {
			JOptionPane.showMessageDialog(this, "Developed By: Voxer\nDate: Sep 12, 2020","About Notepad",JOptionPane.PLAIN_MESSAGE);
		}
		else if(mi==cut || mi==ct) {
			area.cut();
		}
		else if(mi==copy || mi==cpy) {
			area.copy();
		}
		else if(mi==paste || mi==pste) {
			area.paste();
		}
		else if(mi==selectall) {
			area.selectAll();
		}
		else if(mi==exit) {
			System.exit(0);
		}
		
	}
	
	//ActionPerformed end;
	
	private void saveAs() {
		FileDialog fd=new FileDialog(this,"Save",FileDialog.SAVE);
		fd.setVisible(true);
		if(fd.getFile()!=null) {
			filename=fd.getFile();
			fileadd=fd.getDirectory();
			setTitle(filename);
		}
		
		try {
			FileWriter fw= new FileWriter(fileadd+filename);
			for(String line:area.getText().split("\n")) {
				fw.append(line+"\n");
			}
			fw.close();
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(this, "There was problem in Accessing the File","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String fileadd;
	private String filename;
	private boolean isWrap;
	private String selected;
	
	public static void main(String...s){
		new Notepad();
	}

}
