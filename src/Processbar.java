import java.awt.Frame;
import java.awt.Label;

public class Processbar extends Frame
{
	private static final long serialVersionUID = 127L;
	Label test=new Label("TEST");
	float percentnow=0,subnumber=1;
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
		test.setText("Processing...."+(int)percentnow+"%");
	}
	void increase()
	{
		percentnow+=subnumber;
		test.setText("Processing...."+(int)percentnow+"%");
	}
	void totaltimes(int times)
	{
		subnumber=(float)100/times;
	}
	void done()
	{
		test.setText("Process Finish");
	}
}
