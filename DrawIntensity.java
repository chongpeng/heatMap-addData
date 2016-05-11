#将插值法 得到的数据使用 JFreeChart作图
package domains.user;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
public class DrawIntensity{
    public BufferedImage returnInputStream(Float arr[][],Double difX,Double difY,Double minX,Double minY,Double maxX,Double maxY,int index,String title){
	    	    int width = 550;
	          int height =550;
	          JFreeChart chart = createXYZChart(arr,difX,difY,minX,minY,index,maxX,maxY);
	          BufferedImage buffer=chart.createBufferedImage(width, height);
    	  return buffer;
       }
    private JFreeChart createXYZChart(Float arr[][],Double difX,Double difY,Double minX,Double minY,int index,Double maxX,Double maxY) {
    	  DecimalFormat    df   = new DecimalFormat("######0.000");   
        NumberAxis xAxis = new NumberAxis("经度范围:  "+df.format(minX)+"---"+df.format(maxX));
        NumberAxis yAxis = new NumberAxis("纬度范围:  "+df.format(minY)+"---"+df.format(maxY));
        
        XYZDataset xyzset = createData(arr,difX,difY,minX,minY,index);
        XYPlot plot = new XYPlot(xyzset, xAxis, yAxis, null);
        NumberAxis xAxis1=(NumberAxis)plot.getDomainAxis();
        NumberAxis yAxis1=(NumberAxis)plot.getRangeAxis();
        xAxis1.setAutoRange(true);
        yAxis1.setAutoRange(true);
        xAxis1.setTickLabelsVisible(false);
        yAxis1.setTickLabelsVisible(false);

        XYBlockRenderer r = new XYBlockRenderer();
      LookupPaintScale ps = new LookupPaintScale(-5,230,Color.white);
	      ps.add(0,new Color(139,137,137));
	      ps.add(10,new Color(0,0,238));
	      ps.add(20,new Color(0,255,255));
	      ps.add(30,new Color(102,205,170));
	      
	      ps.add(40,new Color(162,205,90));
	      ps.add(50,Color.yellow);
	      ps.add(60,new Color(255,193,37));
	      ps.add(70,new Color(255,140,106));
	      ps.add(80,new Color(255,105,180));
	      
	      ps.add(90,new Color(224,102,255));
	      ps.add(100,new Color(255,0,255));
	      ps.add(110,new Color(238,0,238));
	      
	      ps.add(120,new Color(255,20,147));
	      ps.add(130,new Color(255,20,147));
	      ps.add(140,new Color(255,62,150));
	      
	      
	      ps.add(150,new Color(238,99,99));
	      ps.add(160,new Color(238,99,99));
	      ps.add(170,new Color(255,99,73));
	      ps.add(180,new Color(255,64,64));
	      ps.add(190,new Color(255,0,0));
	      ps.add(200,new Color(255,48,48));
	     
	      ps.add(210,new Color(255,34,44));
	      ps.add(220,new Color(238,59,59));
	      ps.add(230,new Color(255,0,0));
      
        r.setPaintScale(ps);
        r.setBlockHeight(1.0f);
        r.setBlockWidth(1.0f);
        plot.setRenderer(r);
    JFreeChart chart = new JFreeChart("",JFreeChart.DEFAULT_TITLE_FONT,plot,false);
        NumberAxis scaleAxis = new NumberAxis("");
        scaleAxis.setAxisLinePaint(Color.white);
        scaleAxis.setTickMarkPaint(Color.white);
        scaleAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 12));
        PaintScaleLegend legend = new PaintScaleLegend(ps, scaleAxis);
        legend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        legend.setPadding(new RectangleInsets(5, 5, 5, 5));
        legend.setStripWidth(25);
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setBackgroundPaint(Color.WHITE);
        chart.addSubtitle(legend);
        chart.setBackgroundPaint(Color.white);
        return chart;
    }
    
    private static final String series1="series1";
    private static final int max=3500;
   
   private static DefaultXYZDataset createData(Float arr[][],Double difX,Double difY,Double minX,Double minY,int index){
	   DefaultXYZDataset defaultXYZDataset = new DefaultXYZDataset();
	        double[] y1 = new double[index*index];
          double[] z1 = new double[index*index];
          double[] x1 = new double[index*index];
          double v=1.0;
       for(int i=0;i<index;i++){
    	  for(int j=0;j<index;j++){
    		    x1[i*index+j]=(Double)difX*i*max+minX; 
    		    y1[i*index+j]=(Double)difY*j*max+minY; 
    		    z1[i*index+j]=arr[i][j]*v; 
    	   }
       }
   
       double[][] data1 = new double[][] {x1, y1, z1};
            defaultXYZDataset.addSeries(series1, data1);
	   return defaultXYZDataset;
   }
   
}
