package com.siweidg.comm.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class ChartUtils {
	
	
		public static void createBarChart(double[][] data,String[] columnKeys,String[] rowkeys,
				String title,String xname,String yname,
				String outputPath, int weight, int height) {
			
			CategoryDataset cd = createDataset(data, columnKeys, rowkeys);
			JFreeChart chart = createChart(cd, title, xname, yname);
			saveAsFile(chart, outputPath, weight, height);
		}
		
        // 保存为文件  
        private static void saveAsFile(JFreeChart chart, String outputPath, int weight, int height) {  
            FileOutputStream out = null;  
            try {  
                File outFile = new File(outputPath);  
                if (!outFile.getParentFile().exists()) {  
                    outFile.getParentFile().mkdirs();  
                }  
                out = new FileOutputStream(outputPath);  
                // 保存为PNG文件  
                ChartUtilities.writeChartAsPNG(out, chart, weight, height);  
                // 保存为JPEG文件  
                // ChartUtilities.writeChartAsJPEG(out, chart, 500, 400);  
                out.flush();  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } finally {  
                if (out != null) {  
                    try {  
                        out.close();  
                    } catch (IOException e) {  
                        // do nothing  
                    }  
                }  
            }  
        }  
      
        // 根据CategoryDataset生成JFreeChart对象  
        private static JFreeChart createChart(CategoryDataset categoryDataset,String title,String xname,String yname) {  
            JFreeChart jfreechart = ChartFactory.createBarChart(title, // 标题  
                    xname, // categoryAxisLabel （category轴，横轴，X轴的标签）  
                    yname, // valueAxisLabel（value轴，纵轴，Y轴的标签）  
                    categoryDataset, // dataset  
                    PlotOrientation.VERTICAL, false, // legend  
                    false, // tooltips  
                    false); // URLs  
      
            Font labelFont = new Font("SansSerif", Font.TRUETYPE_FONT, 12);  
      
            jfreechart.setTextAntiAlias(false);  
            jfreechart.setBackgroundPaint(Color.WHITE);  
      
            CategoryPlot plot = jfreechart.getCategoryPlot();// 获得图表区域对象  
      
            // 设置横虚线可见  
            plot.setRangeGridlinesVisible(true);  
            // 虚线色彩  
            plot.setRangeGridlinePaint(Color.gray);  
            // 数据轴精度  
            NumberAxis vn = (NumberAxis) plot.getRangeAxis();  
            // vn.setAutoRangeIncludesZero(true);  
            DecimalFormat df = new DecimalFormat("#0.0");  
            vn.setNumberFormatOverride(df); // 数据轴数据标签的显示格式  
      
            // x轴设置  
            CategoryAxis domainAxis = plot.getDomainAxis();  
            domainAxis.setLabelFont(labelFont);// 轴标题  
            domainAxis.setTickLabelFont(labelFont);// 轴数值  
            // Lable（Math.PI/3.0）度倾斜  
            // domainAxis.setCategoryLabelPositions(CategoryLabelPositions  
            // .createUpRotationLabelPositions(Math.PI / 3.0));  
            domainAxis.setMaximumCategoryLabelWidthRatio(8.00f);// 横轴上的 Lable  
            // 是否完整显示  
      
            // 设置距离图片左端距离  
            domainAxis.setLowerMargin(0.1);  
            // 设置距离图片右端距离  
            domainAxis.setUpperMargin(0.1);  
            // 设置 columnKey 是否间隔显示  
            // domainAxis.setSkipCategoryLabelsToFit(true);  
            plot.setDomainAxis(domainAxis);  
            // 设置柱图背景色（注意，系统取色的时候要使用16位的模式来查看颜色编码，这样比较准确）  
            plot.setBackgroundPaint(new Color(255, 128, 128));  
      
            // y轴设置  
            ValueAxis rangeAxis = plot.getRangeAxis();  
            rangeAxis.setLabelFont(labelFont);  
            rangeAxis.setTickLabelFont(labelFont);  
            // 设置最高的一个 Item 与图片顶端的距离  
            rangeAxis.setUpperMargin(0.15);  
            // 设置最低的一个 Item 与图片底端的距离  
            rangeAxis.setLowerMargin(0.15);  
            plot.setRangeAxis(rangeAxis);  
      
            // 解决中文乱码问题(关键)  
            TextTitle textTitle = jfreechart.getTitle();  
            textTitle.setFont(new Font("黑体", Font.PLAIN, 20));  
            domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 11));  
            domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));  
            vn.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 12));  
            vn.setLabelFont(new Font("黑体", Font.PLAIN, 12));  
            // jfreechart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));  
      
            BarRenderer renderer = new BarRenderer();  
            // 设置柱子宽度  
            renderer.setMaximumBarWidth(0.6);  
            // 设置柱子高度  
            renderer.setMinimumBarLength(0.2);  
            // 设置柱子边框颜色  
            renderer.setBaseOutlinePaint(new Color(239, 228, 176));  
            // 设置柱子边框可见  
            renderer.setDrawBarOutline(true);  
            // // 设置柱的颜色  
            renderer.setSeriesPaint(0, new Color(4,88,190));  
            // 设置每个地区所包含的平行柱的之间距离  
            renderer.setItemMargin(0.5);  
            jfreechart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);  
            // 显示每个柱的数值，并修改该数值的字体属性  
            renderer.setIncludeBaseInRange(true);  
            renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());  
            renderer.setBaseItemLabelsVisible(true);  
            plot.setRenderer(renderer);  
            // 设置柱的透明度  
            plot.setForegroundAlpha(1.0f);  
      
            // 背景色 透明度  
            plot.setBackgroundAlpha(0.5f);  
      
            return jfreechart;  
        }  
      
        
        
        // 创建CategoryDataset对象  
        public static CategoryDataset createDataset(double[][] data,String[] columnKeys,String[] rowkeys) { 
            CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowkeys, columnKeys, data);  
            return dataset;  
        }  
      
}
