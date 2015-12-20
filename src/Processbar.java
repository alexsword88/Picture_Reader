import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Frame;
import java.awt.Label;

public class Processbar extends Frame
{
	private static final long serialVersionUID = 127L;
	Label test=new Label("TEST");
	int percentnow=0;
	Processbar(int x,int y)
	{
		this.setTitle("Process");
		this.setBounds(x+50,y+50,220,80);
		this.setResizable(false);
		this.setLayout(null);
		test.setBounds(5,0,110,100);
		this.add(test);
	}
	void open()
	{
		this.setVisible(true);
		percentnow=0;
		test.setText("Processing...."+percentnow+"%");
	}
	void increase()
	{
		percentnow++;
		test.setText("Processing...."+percentnow+"%");
	}
	void done()
	{
		test.setText("Process Finish");
	}
}
