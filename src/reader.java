import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class reader implements ActionListener
{
	//========build UI=========
	Frame main=new Frame("ImageReader");
	TextField tf=new TextField();
	Button brobt=new Button("Browse");
	Button show=new Button("Show");
	Button cancel=new Button("Cancel");
	FileDialog brodg=new FileDialog(main,"Pick a Picture",FileDialog.LOAD);
	String targetfile="",targetfilename="";
	myfilter filefilter=new myfilter();
	BufferedImage img;
	Showfr ab;
	RGBarray array;
	//========cancel button setting=========
	void cancel()
	{
		cancel.setBounds(150, 100, 100, 30);
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
	}
	//========"show" button setting=========
	void showpic()
	{
		show.setBounds(25, 100, 100, 30);
		show.setActionCommand("show");
		show.addActionListener(this);
	}
	//========textfield setting=========
	void textfield()
	{
		tf.setEditable(false);
		tf.setFont(new Font(Font.DIALOG,Font.PLAIN,18));
		tf.setBounds(25, 50, 390, 30);
	}
	//========browse button setting=========
	void browsebutton()
	{
		brobt.addActionListener(this);
		brobt.setActionCommand("browsedialog");
		brobt.setBounds(420,50,70,30);
	}
	//========browse dialog setting=========
	void browsedialog()
	{
		filefilter.cre8filter(".jpg",".png",".bmp",".jpeg");
		brodg.setDirectory("C:\\");
		brodg.setFile("*.jpg;*.png;*.bmp;*.jpeg");
		brodg.setFilenameFilter(filefilter);
		brodg.setVisible(true);
		targetfile=brodg.getDirectory();
		targetfilename=brodg.getFile();
		if(targetfilename==null)
		{
			tf.setText("NO Select File");
		}
		else
		{
			tf.setText(targetfile+targetfilename);
			try
			{
				//========image reading=========
				img =ImageIO.read(new File(targetfile+targetfilename));
				array=new RGBarray(img);
				ab=new Showfr(array);
				array.input(ab);
			}
			catch(IOException e)
			{
				tf.setText("Image Loading Fail");
			}
		}
	}
	//=====constructor======
	reader()
	{
		textfield();
		browsebutton();
		showpic();
		cancel();

		main.setBounds(400,200,500,150);
		main.setTitle("ImageReader");
		main.setVisible(true);
		main.addWindowListener(new closeX());
		main.setLayout(null);
		main.setResizable(false);
		main.add(tf);
		main.add(brobt);
		main.add(show);
		main.add(cancel);
	}
	//========button action=================
	public void actionPerformed(ActionEvent e)
	{
		String temp=e.getActionCommand();
		if(temp=="show")
		{
			ab.showfr(img);
		}
		else if(temp=="cancel")
		{
			main.dispose();
		}
		else if(temp=="browsedialog")
		{
			browsedialog();
		}
	}
	//==========main=========
	public static void main(String[] arg)
	{
		reader ab=new reader();
	}
	//=====for window closing=========
	class closeX extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			main.dispose();
			System.exit(0);
		}
	}
	//=====For OS X File Filter=========
	class myfilter implements FilenameFilter
	{
		String[] filters;
		public void cre8filter(String...filters)
		{
			this.filters=filters;
		}
		public boolean accept(File dir,String name)
		{
			for(String filter:filters)
			{
				if(name.endsWith(filter))
				{
					return true;
				}
			}
			return false;
		}
	}
}
