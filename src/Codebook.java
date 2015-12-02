import java.awt.Color;
public class Codebook
{
	int[][] codebook=new int[256][4],picarray;
	
	int[] indexarray,diffarray;
	boolean flag4changed=false;
	RGBarray rgbarray;
	int mywidth,myheight;
	Codebook(RGBarray temp)
	{
		rgbarray=temp;
		mywidth=rgbarray.mywidth;
		myheight=rgbarray.myheight;
		if(mywidth%4!=0)
		{
			mywidth-=(mywidth%4);
			if(myheight%4!=0)
			{
				myheight-=(myheight%4);
			}
		}
		else
		{
			if(myheight%4!=0)
			{
				myheight-=(myheight%4);
			}
		}
		indexarray=new int[(mywidth/4)*(myheight/4)];
		diffarray=new int[(mywidth/4)*(myheight/4)];
		randomcode();
	}
	void randomcode()
	{
		for(int i=0;i<256;i++)
		{
			for(int j=0;j<4;j++)
			{
				Color temp=new Color(randomnum(),randomnum(),randomnum());
				codebook[i][j]=temp.getRGB();
			}
		}
	}
	void compress(int[][] picarraytemp)
	{
		picarray=picarraytemp;
		do
		{
			flag4changed=false;
			assign();
			update();
		}while(flag4changed);
	}
	void assign()
	{
		int arrayx=0,arrayy=0,diff=0,index=0;
		for(int j=0;j<indexarray.length;j++)
		{
			diff=total(picarray[arrayx][arrayy],picarray[arrayx+1][arrayy],picarray[arrayx][arrayy+1],
					picarray[arrayx+1][arrayy+1])-total(codebook[0][0],codebook[0][1],codebook[0][2],codebook[0][3]);
			for(int i=1;i<256;i++)
			{
				int tempdiff=total(picarray[arrayx][arrayy],picarray[arrayx+1][arrayy],picarray[arrayx][arrayy+1],
						picarray[arrayx+1][arrayy+1])-total(codebook[i][0],codebook[i][1],codebook[i][2],codebook[i][3]);
				if(tempdiff<diff)
				{
					diff=tempdiff;
					index=i;
				}
			}
			indexarray[j]=index;
			diffarray[j]=diff;
			if(arrayx>mywidth/4)
			{
				arrayx=0;
				arrayy+=2;
			}
			else
			{
				arrayx+=2;
			}
		}
	}
	void update()
	{
		for(int i:diffarray)
		{
			if(i>10)
			{
				flag4changed=true;
			}
		}
	}
	int total(int a,int b,int c,int d)
	{
		return (Math.abs(a)+Math.abs(b)+Math.abs(c)+Math.abs(d));
	}
	int randomnum()
	{
		return (int)(Math.random()*256);
	}
}
