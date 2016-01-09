import java.awt.Color;
import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
public class Codebook
{
	FileDialog savedialog;
	int[][] temp4depart,newpicarrayr,newpicarrayg,newpicarrayb,codebookr,codebookg,codebookb;
	double[][] YCbCrarray,dctarray,idctarray;
	String[][] RLCarray;
	String[] DPCMarray=new String[3];
	int[] indexarray,picarray;
	RGBarray rgbarray;
	int mywidth,myheight,lbgwidth,lbgheight,lbgdiffwidth,lbgdiffheight,dctdiffwidth,dctdiffheight;
	Processbar process;
	dctshower dctshow;
	Codebook(RGBarray temp)
	{
		rgbarray=temp;
		mywidth=rgbarray.mywidth;
		myheight=rgbarray.myheight;
		lbgwidth=rgbarray.mywidth;
		lbgheight=rgbarray.myheight;
		if(mywidth%8!=0)
		{
			mywidth+=(8-(mywidth%8));
			if(myheight%8!=0)
			{
				myheight+=(8-(myheight%8));
			}
		}
		else
		{
			if(myheight%8!=0)
			{
				myheight+=(8-(myheight%8));
			}
		}
		picarray=rgbarray.colorarray;
		colordepart(picarray,picarray.length);
	}
	void LBG()
	{
		process=new Processbar(rgbarray.showfr.getX(),rgbarray.showfr.getY());
		process.open();
		codebookr=new int[256][16];
		codebookg=new int[256][16];
		codebookb=new int[256][16];
		if(lbgwidth%4!=0)
		{
			lbgwidth+=(4-(lbgwidth%4));
			if(lbgheight%4!=0)
			{
				lbgheight+=(4-(lbgheight%4));
			}
		}
		else
		{
			if(myheight%4!=0)
			{
				lbgheight+=(4-(lbgheight%4));
			}
		}
		newpicarrayr=new int[lbgheight/4*lbgwidth/4][16];
		newpicarrayg=new int[lbgheight/4*lbgwidth/4][16];
		newpicarrayb=new int[lbgheight/4*lbgwidth/4][16];
		indexarray=new int[lbgheight/4*lbgwidth/4];
		lbgdiffwidth=lbgwidth-rgbarray.mywidth;
		lbgdiffheight=lbgheight-rgbarray.myheight;
		int[][] temparray=new int[lbgheight*lbgwidth][3];
		int index=0;
		for(int j=0;j<3;j++)
		{
			for(int i=0,x=0,y=0;i<lbgheight*lbgwidth;i++,x++)
			{
				if((x!=lbgwidth-lbgdiffwidth-1)&&(y!=lbgheight-lbgdiffheight-1))
				{
					temparray[arrayindex(x,y,lbgwidth)][j]=temp4depart[arrayindex(x,y,rgbarray.mywidth)][j];
				}
				else
				{
					if(x==lbgwidth-lbgdiffwidth-1)
					{
						for(int z=0;z<lbgdiffwidth;z++)
						{
							temparray[arrayindex(x+z,y,lbgwidth)][j]=0;
						}
						x=-1;
						y++;
					}
					else
					{
						for(int z=0;z<lbgdiffheight;z++)
						{
							for(int zz=0;zz<lbgwidth;zz++)
							{
								temparray[arrayindex(x+zz,y+z,lbgwidth)][j]=0;
							}
						}
						break;
					}
				}
			}
		}
		int x=0,y=0,xx=0,yy=0;
		for(int i=0;i<lbgheight/4*lbgwidth/4;i++)
		{
			x=0;
			y=0;
			for(int j=0;j<16;j++)
			{
				newpicarrayr[i][j]=temparray[arrayindex(xx+x,yy+y,lbgwidth)][0];
				newpicarrayg[i][j]=temparray[arrayindex(xx+x,yy+y,lbgwidth)][1];
				newpicarrayb[i][j]=temparray[arrayindex(xx+x,yy+y,lbgwidth)][2];
				if(x/3==1)
				{
					x=0;
					y++;
				}
				else
				{
					x++;
				}
			}
			if(xx+4<lbgwidth)
			{
				xx+=4;				
			}
			else
			{
				xx=0;
				yy+=4;
			}
		}
		ArrayList<Integer> randomed=new ArrayList<Integer>(0);
		for(int i=0;i<lbgheight/4;i++)
		{
			randomed.add((lbgwidth/4)+(i*lbgwidth/4-1));
		}
		for(int i=0;i<lbgwidth/4;i++)
		{
			randomed.add(arrayindex(i,lbgheight/4-1,lbgwidth/4));
		}
		for(int i=0;i<256;i++)
		{
			boolean tempflag=false;
			do
			{
				tempflag=false;
				index=randomnum(0,(lbgheight/4*lbgwidth/4)-1);
				for(int z:randomed)
				{
					if(index==z)
					{
						tempflag=true;
					}
				}
				if((randomed.size()-(lbgwidth*lbgheight-1))>=lbgwidth*lbgheight)
				{
					break;
				}
			}while(tempflag);
			for(int j=0;j<16;j++)
			{
				codebookr[i][j]=newpicarrayr[index][j];
				codebookg[i][j]=newpicarrayg[index][j];
				codebookb[i][j]=newpicarrayb[index][j];
			}
		}
		process.totaltimes(20);
		for(int i=0;i<20;i++)
		{
			assign();
			update();
			process.increase();
		}
		process.done();
		try
		{
			Thread.sleep(500);
		}
		catch(Exception e)
		{}
		show();
		process.dispose();
	}
	void jpegcompress()
	{
		rgbtoyuv(picarray,picarray.length);
		process=new Processbar(rgbarray.showfr.getX(),rgbarray.showfr.getY());
		fullDCT(YCbCrarray);
		fullVQ();
		RLC();
		DPCM();
		output();
		process=new Processbar(rgbarray.showfr.getX(),rgbarray.showfr.getY());
		fullRVQ();
		fullIDCT(dctarray);
		yuvtorgb(idctarray,picarray.length);
	}
	void showdct()
	{
		rgbtoyuv(picarray,picarray.length);
		process=new Processbar(rgbarray.showfr.getX(),rgbarray.showfr.getY());
		fullDCT(YCbCrarray);
		dctshow=new dctshower(dctarray,mywidth,myheight);
	}
	void assign()
	{
		int min=0,tempdiff=0,index=0;
		for(int i=0;i<lbgheight/4*lbgwidth/4;i++)
		{
			min=0;
			for(int j=0;j<16;j++)
			{
				min+=total(newpicarrayr[i][j]-codebookr[0][j]);
			}
			min=(int)(Math.sqrt(min));
			for(int z=0;z<256;z++)
			{
				tempdiff=0;
				for(int j=0;j<16;j++)
				{
					tempdiff+=total(newpicarrayr[i][j]-codebookr[z][j]);
				}
				tempdiff=(int)(Math.sqrt(tempdiff));
				if(tempdiff<min)
				{
					min=tempdiff;
					indexarray[index]=z;
				}
			}
			index++;
		}
	}
	void update()
	{
		for(int i:indexarray)
		{
			int[] avgr={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			int[] avgg={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			int[] avgb={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			int count=0,index=0;
			for(int j:indexarray)
			{
				if(j==i)
				{
					for(int z=0;z<16;z++)
					{
						avgr[z]+=newpicarrayr[index][z];
						avgg[z]+=newpicarrayg[index][z];
						avgb[z]+=newpicarrayb[index][z];
					}
					count++;
				}
				index++;
			}
			for(int z=0;z<16;z++)
			{
				codebookr[i][z]=avgr[z]/count;
				codebookg[i][z]=avgg[z]/count;
				codebookb[i][z]=avgb[z]/count;
			}
		}
	}
	void show()
	{
		int[][] temparray=new int[lbgwidth*lbgheight][3];
		int x=0,y=0,xx=0,yy=0,index=0,target;
		for(int i=0;i<lbgheight/4*lbgwidth/4;i++)
		{
			x=0;
			y=0;
			target=indexarray[index];
			for(int j=0;j<16;j++)
			{
				temparray[arrayindex(xx+x,yy+y,lbgwidth)][0]=codebookr[target][j];
				temparray[arrayindex(xx+x,yy+y,lbgwidth)][1]=codebookg[target][j];
				temparray[arrayindex(xx+x,yy+y,lbgwidth)][2]=codebookb[target][j];
				if(x/3==1)
				{
					x=0;
					y++;
				}
				else
				{
					x++;
				}
			}
			if(xx+4<lbgwidth)
			{
				xx+=4;				
			}
			else
			{
				xx=0;
				yy+=4;
			}
			index++;
		}
		index=0;
		x=0;
		y=0;
		for(int i=0;i<lbgheight*lbgwidth;i++,x++)
		{
			if((x!=lbgwidth-lbgdiffwidth-1)&&(y!=lbgheight-lbgdiffheight-1))
			{
				rgbarray.colorarray[arrayindex(x,y,rgbarray.mywidth)]=new Color(temparray[arrayindex(x,y,lbgwidth)][0],temparray[arrayindex(x,y,lbgwidth)][1],temparray[arrayindex(x,y,lbgwidth)][2]).getRGB();
			}
			else
			{
				if(x==lbgwidth-lbgdiffwidth-1)
				{
					x=-1;
					y++;
				}
				else
				{
					break;
				}
			}
		}
	}
	void colordepart(int[] array,int length)
	{
		int index=0;
		temp4depart=new int[length][3];
		for(int i:array)
		{
			temp4depart[index][0]=new Color(i).getRed();
			temp4depart[index][1]=new Color(i).getGreen();
			temp4depart[index][2]=new Color(i).getBlue();
			index++;
		}
	}
	void fullVQ()
	{
		int[][] Yvqtable={{16,11,10,16,24,40,51,61},
						  {12,12,14,19,26,58,60,55},
						  {14,13,16,24,40,57,69,56},
						  {14,17,22,29,51,87,80,62},
						  {18,22,37,56,68,109,103,77},
						  {24,35,55,64,81,104,113,92},
						  {49,64,78,87,103,121,120,101},
						  {72,92,95,98,112,100,103,99}};
		int[][] CbCrvqtable={{17,18,24,47,99,99,99,99},
							 {18,21,26,66,99,99,99,99},
							 {24,26,56,99,99,99,99,99},
							 {47,66,99,99,99,99,99,99},
							 {99,99,99,99,99,99,99,99},
							 {99,99,99,99,99,99,99,99},
							 {99,99,99,99,99,99,99,99},
							 {99,99,99,99,99,99,99,99}};
		for(int yy=0;yy<myheight;yy+=8)
		{
			for(int xx=0;xx<mywidth;xx+=8)
			{
				for(int i=0,x=0,y=0;i<64;i++,x++)
				{
					dctarray[arrayindex(xx+x,yy+y,mywidth)][0]=(float)(dctarray[arrayindex(xx+x,yy+y,mywidth)][0]/Yvqtable[y][x]);
					dctarray[arrayindex(xx+x,yy+y,mywidth)][1]=(float)(dctarray[arrayindex(xx+x,yy+y,mywidth)][1]/CbCrvqtable[y][x]);
					dctarray[arrayindex(xx+x,yy+y,mywidth)][2]=(float)(dctarray[arrayindex(xx+x,yy+y,mywidth)][2]/CbCrvqtable[y][x]);
					if(x==7)
					{
						x=-1;
						y++;
					}
				}
			}
		}
	}
	void fullRVQ()
	{
		int[][] Yvqtable={{16,11,10,16,24,40,51,61},
						  {12,12,14,19,26,58,60,55},
						  {14,13,16,24,40,57,69,56},
						  {14,17,22,29,51,87,80,62},
						  {18,22,37,56,68,109,103,77},
						  {24,35,55,64,81,104,113,92},
						  {49,64,78,87,103,121,120,101},
						  {72,92,95,98,112,100,103,99}};
		int[][] CbCrvqtable={{17,18,24,47,99,99,99,99},
							 {18,21,26,66,99,99,99,99},
							 {24,26,56,99,99,99,99,99},
							 {47,66,99,99,99,99,99,99},
							 {99,99,99,99,99,99,99,99},
							 {99,99,99,99,99,99,99,99},
							 {99,99,99,99,99,99,99,99},
							 {99,99,99,99,99,99,99,99}};
		for(int yy=0;yy<myheight;yy+=8)
		{
			for(int xx=0;xx<mywidth;xx+=8)
			{
				for(int i=0,x=0,y=0;i<64;i++,x++)
				{
					dctarray[arrayindex(xx+x,yy+y,mywidth)][0]=(float)(dctarray[arrayindex(xx+x,yy+y,mywidth)][0]*Yvqtable[y][x]);
					dctarray[arrayindex(xx+x,yy+y,mywidth)][1]=(float)(dctarray[arrayindex(xx+x,yy+y,mywidth)][1]*CbCrvqtable[y][x]);
					dctarray[arrayindex(xx+x,yy+y,mywidth)][2]=(float)(dctarray[arrayindex(xx+x,yy+y,mywidth)][2]*CbCrvqtable[y][x]);
					if(x==7)
					{
						x=-1;
						y++;
					}
				}
			}
		}
	}
	void RLC()
	{
		int[] positionx={0,1,0,0,1,2,3,2,1,0,0,1,2,3,4,5,4,3,2,1,0,0,1,2,3,4,5,6,7,6,5,4,3,2,1,0,1,2,3,4,5,6,7,7,6,5,4,3,2,3,4,5,6,7,7,6,5,4,5,6,7,7,6,7};
		int[] positiony={0,0,1,2,1,0,0,1,2,3,4,3,2,1,0,0,1,2,3,4,5,6,5,4,3,2,1,0,0,1,2,3,4,5,6,7,7,6,5,4,3,2,1,2,3,4,5,6,7,7,6,5,4,3,4,5,6,7,7,6,5,6,7,7};
		for(int z=0;z<3;z++)
		{
			for(int y=0,index=0;y<myheight;y+=8)
			{
				for(int x=0;x<mywidth;x+=8,index++)
				{
					RLCarray[index][z]="";
					for(int i=1,zerocount=0;i<64;i++)
					{
						if((int)(dctarray[arrayindex(x+positionx[i],y+positiony[i],mywidth)][z])==0)
						{
							zerocount++;
							if(i==63)
							{
								RLCarray[index][z]+="(0,0)";
							}
						}
						else
						{
							RLCarray[index][z]+=("("+zerocount+",");
							RLCarray[index][z]+=(String.valueOf((int)(dctarray[arrayindex(x+positionx[i],y+positiony[i],mywidth)][0]))+")");
							zerocount=0;
						}
					}
				}
			}
		}
	}
	void DPCM()
	{
		int[][] temparray=new int[mywidth/8*myheight/8][3];
		for(int z=0;z<3;z++)
		{
			for(int y=0,index=0;y<myheight;y+=8)
			{
				for(int x=0;x<mywidth;x+=8,index++)
				{
					temparray[index][z]=(int)(dctarray[arrayindex(x,y,mywidth)][z]);
				}
			}
		}
		int temp,diff;
		for(int z=0;z<3;z++)
		{
			temp=temparray[0][z];
			DPCMarray[z]=String.valueOf(temp)+",";
			for(int i=1;i<mywidth/8*myheight/8-1;i++)
			{
				diff=temparray[i][z]-temp;
				DPCMarray[z]+=(String.valueOf(diff));
				if(i!=mywidth/8*myheight/8-2)
				{
					DPCMarray[z]+=",";
				}
				temp=temparray[i][z];
			}
		}
	}
	void output()
	{
		FileWriter compressfile;
		BufferedWriter bw;
		savedialog=new FileDialog(rgbarray.showfr,"Pick a Picture",FileDialog.LOAD);
		savedialog.setVisible(true);
		String targetfile=savedialog.getDirectory();
		String targetfilename=savedialog.getFile();
		try
		{
			if(targetfilename!=null)
			{
				compressfile=new FileWriter(targetfile+targetfilename);
				bw=new BufferedWriter(compressfile);
				compressfile.write(rgbarray.mywidth+"\r\n");
				compressfile.write(rgbarray.myheight+"\r\n");
				for(int z=0;z<3;z++)
				{
					compressfile.write(";");
					compressfile.write("\r\n");
					compressfile.write(DPCMarray[z]);
					compressfile.write("\r\n");
					for(int y=0;y<myheight/8;y++)
					{
						for(int x=0;x<mywidth/8;x++)
						{
							compressfile.write(RLCarray[arrayindex(x,y,mywidth/8)][z]);
							compressfile.write("\r\n");
						}
					}
					compressfile.write(";");
					compressfile.write("\r\n");
					compressfile.flush();
				}
				compressfile.close();
			}
		}
		catch(IOException e)
		{
			System.out.println("ERROR");
		}
		/*try
		{
			FileReader fr=new FileReader(targetfile+targetfilename);
			BufferedReader br=new BufferedReader(fr);
			String temp="";
			int test=0;
			while((temp=br.readLine())!=null)
			{
				
			}
		}
		catch(IOException e)
		{
			
		}*/
	}
	void rgbtoyuv(int[] array,int length)
	{
		YCbCrarray=new double[mywidth*myheight][3];
		dctarray=new double[mywidth*myheight][3];
		idctarray=new double[mywidth*myheight][3];
		RLCarray=new String[mywidth/8*myheight/8][3];
		dctdiffwidth=mywidth-rgbarray.mywidth;
		dctdiffheight=myheight-rgbarray.myheight;
		for(int i=0,x=0,y=0;i<mywidth*myheight;i++,x++)
		{
			if((x!=mywidth-dctdiffwidth-1)&&(y!=myheight-dctdiffheight-1))
			{
				YCbCrarray[arrayindex(x,y,mywidth)][0]=0.257*temp4depart[arrayindex(x,y,rgbarray.mywidth)][0]+0.504*temp4depart[arrayindex(x,y,rgbarray.mywidth)][1]+0.098*temp4depart[arrayindex(x,y,rgbarray.mywidth)][2]+16;
				YCbCrarray[arrayindex(x,y,mywidth)][1]=-0.148*temp4depart[arrayindex(x,y,rgbarray.mywidth)][0]-0.291*temp4depart[arrayindex(x,y,rgbarray.mywidth)][1]+0.439*temp4depart[arrayindex(x,y,rgbarray.mywidth)][2]+128;
				YCbCrarray[arrayindex(x,y,mywidth)][2]=0.439*temp4depart[arrayindex(x,y,rgbarray.mywidth)][0]-0.368*temp4depart[arrayindex(x,y,rgbarray.mywidth)][1]-0.071*temp4depart[arrayindex(x,y,rgbarray.mywidth)][2]+128;
			}
			else
			{
				if(x==mywidth-dctdiffwidth-1)
				{
					for(int z=0;z<dctdiffwidth;z++)
					{
						YCbCrarray[arrayindex(x+z,y,mywidth)][0]=0;
						YCbCrarray[arrayindex(x+z,y,mywidth)][1]=0;
						YCbCrarray[arrayindex(x+z,y,mywidth)][2]=0;
					}
					x=-1;
					y++;
				}
				else
				{
					for(int z=0;z<dctdiffheight;z++)
					{
						for(int zz=0;zz<mywidth;zz++)
						{
							YCbCrarray[arrayindex(x+zz,y+z,mywidth)][0]=0;
							YCbCrarray[arrayindex(x+zz,y+z,mywidth)][1]=0;
							YCbCrarray[arrayindex(x+zz,y+z,mywidth)][2]=0;
						}
					}
					break;
				}
			}
		}
	}
	void yuvtorgb(double[][] array,int length)
	{
		int index=0;
		for(int i=0,x=0,y=0;i<lbgheight*lbgwidth;i++,x++)
		{
			if((x!=mywidth-dctdiffwidth-1)&&(y!=myheight-dctdiffheight-1))
			{				
				rgbarray.colorarray[arrayindex(x,y,rgbarray.mywidth)]=new Color(Math.round((float)(1.164*(array[arrayindex(x,y,mywidth)][0]-16)+1.596*(array[arrayindex(x,y,mywidth)][2]-128))),
											Math.round((float)(1.164*(array[arrayindex(x,y,mywidth)][0]-16)-0.391*(array[arrayindex(x,y,mywidth)][1]-128)-0.813*(array[arrayindex(x,y,mywidth)][2]-128))),
											Math.round((float)(1.164*(array[arrayindex(x,y,mywidth)][0]-16)+2.018*(array[arrayindex(x,y,mywidth)][1]-128)))).getRGB();
			}
			else
			{
				if(x==mywidth-dctdiffwidth-1)
				{
					x=-1;
					y++;
				}
				else
				{
					break;
				}
			}
		}
		/*for(int i=0;i<mywidth*myheight;i++)
		{
			if(((i%rgbarray.mywidth)!=(rgbarray.mywidth-1))&&(index<length-1))
			{
				rgbarray.colorarray[index]=new Color(
						Math.round((float)(array[i][0]+1.402*array[i][2])),
						Math.round((float)(array[i][0]-0.344*array[i][1]-0.714*array[i][2])),
						Math.round((float)(array[i][0]+1.772*array[i][1]))).getRGB();
				index++;
			}
			else
			{
				if((i%mywidth)==(mywidth-1))
				{
					index++;
				}
			}
		}*/
	}
	void fullDCT(double[][] temparray)
	{
		process.open();
		process.totaltimes(myheight/8*mywidth/8*3);
		for(int z=0;z<3;z++)
		{
			for(int y=0;y<myheight;y+=8)
			{
				for(int x=0;x<mywidth;x+=8)
				{
					process.increase();
					DCT(temparray,x,y,z);
				}
			}
		}
		process.done();
		process.dispose();
	}
	void fullIDCT(double[][] temparray)
	{
		process.open();
		process.totaltimes(myheight/8*mywidth/8*3);
		for(int z=0;z<3;z++)
		{
			for(int y=0;y<myheight;y+=8)
			{
				for(int x=0;x<mywidth;x+=8)
				{
					process.increase();
					IDCT(temparray,x,y,z);
				}
			}
		}
		process.done();
		process.dispose();
	}
	void DCT(double[][] temparray,int x,int y,int color)
	{
		double[][] temp88=new double[8][8];
		double temp=0,temp2=0;
		int xx=0,yy=0,count=0;
		for(int j=0;j<8;j++)
		{
			for(int i=0;i<8;i++)
			{
				temp88[i][j]=temparray[arrayindex(x,y,mywidth)][color];
				x++;
			}
			y++;
			x-=8;
		}
		y-=8;
		do
		{
			temp=1;
			temp2=0;
			if(xx==0)
			{
				temp=(Math.sqrt(2)/2);
				if(yy==0)
				{
					temp*=(Math.sqrt(2)/2);
				}
				temp/=4;
			}
			else
			{
				if(yy==0)
				{
					temp*=(Math.sqrt(2)/2);
				}
				temp/=4;
			}
			for(int j=0;j<8;j++)
			{
				for(int i=0;i<8;i++)
				{
					temp2+=Math.cos(((2*j+1)*yy*Math.PI)/16)*Math.cos(((2*i+1)*xx*Math.PI)/16)*temp88[j][i];
				}
			}
			temp*=temp2;
			dctarray[arrayindex(x,y,mywidth)][color]=temp;
			x++;
			xx++;
			count++;
			if(count%8==0)
			{
				x-=8;
				xx-=8;
				yy++;
				y++;
			}
		}while(count!=64);
	}
	void IDCT(double[][] temparray,int x,int y,int color)
	{
		double[][] temp88=new double[8][8];
		double temp=0,temp2=0;
		int xx=0,yy=0,count=0;
		for(int j=0;j<8;j++)
		{
			for(int i=0;i<8;i++)
			{
				temp88[i][j]=temparray[arrayindex(x,y,mywidth)][color];
				x++;
			}
			y++;
			x-=8;
		}
		y-=8;
		do
		{
			temp2=0;
			for(int j=0;j<8;j++)
			{
				for(int i=0;i<8;i++)
				{
					temp=1;
					if(i==0)
					{
						temp=(Math.sqrt(2)/2);
						if(j==0)
						{
							temp*=(Math.sqrt(2)/2);
						}
						temp/=4;
					}
					else
					{
						if(j==0)
						{
							temp*=(Math.sqrt(2)/2);
						}
						temp/=4;
					}
					temp2+=temp*Math.cos(((2*yy+1)*j*Math.PI)/16)*Math.cos(((2*xx+1)*i*Math.PI)/16)*temp88[j][i];
				}
			}
			idctarray[arrayindex(x,y,mywidth)][color]=temp2;
			x++;
			xx++;
			count++;
			if(count%8==0)
			{
				x-=8;
				xx-=8;
				yy++;
				y++;
			}
		}while(count!=64);
	}
	int arrayindex(int x,int y,int width)
	{
		return y*width+x;
	}
	int total(int... number)
	{
		int total=0;
		for(int i:number)
		{
			total+=Math.pow(i,2);
		}
		return total;
	}
	int randomnum(int start,int end)
	{
		return start+(int)(Math.random()*(end-start+1));
	}
	int randomnum()
	{
		return (int)(Math.random()*256);
	}
}
