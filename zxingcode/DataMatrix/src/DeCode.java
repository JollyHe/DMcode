import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.lang.model.element.VariableElement;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.ImageReader;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.common.StringUtils;
import com.google.zxing.datamatrix.DataMatrixReader;

public class DeCode {
	public static int total = 0;

	private static final int blkSize = 100; // block size
	private static final int patchSize = 4; // patch size
	private static final double lambda = 5.0; // control the relative importance of contrast loss and information loss
	private static final double eps = 1e-8;
	private static final int krnlSize = 10;

	public static void main(String[] args) {
		System.load("F:\\二维码识别\\zxingcode\\DataMatrix\\lib\\x64\\opencv_java411.dll");
		
		int mark = 1; //1表示识别原图为白底黑点，2表示识别原图为黑底白点
		int successed = 0;
		int failed = 0;
		int notfound = 0;
		int format = 0;
		int checksum = 0;
		
		if (mark == 1) {
			File file = new File("F:\\DMdecode\\DecodeImage\\test325\\testNew");
	        File [] files = file.listFiles();
	        for (int i = 0; i < files.length; i++)
	        {
	            String fileName = files[i].getName();
	            String imgPath = "F:\\DMdecode\\DecodeImage\\test325\\testNew\\" + fileName;
	            
	            Mat mat = Imgcodecs.imread(imgPath);
	    		
	    		BufferedImage img = Mat2BufImg(mat, ".bmp");
	    		LuminanceSource source = new BufferedImageLuminanceSource(img);
	    		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	    		Result result;
	    		
    			try {
					result = new DataMatrixReader().decode(bitmap);
					System.out.println(String.valueOf(result.getText()));
	    			Imgcodecs.imwrite("F:\\DMdecode\\DecodeImage\\test325\\success\\" + fileName + "_" + result + ".bmp", mat);
	    			successed++;
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
	    			Imgcodecs.imwrite("F:\\DMdecode\\DecodeImage\\test325\\notfound\\" + "NotFoundException" + "_" + fileName + ".bmp", mat);
	    			failed++;
	    			notfound++;
				} catch (ChecksumException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Imgcodecs.imwrite("F:\\DMdecode\\DecodeImage\\test325\\checksum\\" + "ChecksumException" + "_" + fileName + ".bmp", mat);
	    			failed++;
	    			checksum++;
				} catch (FormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Imgcodecs.imwrite("F:\\DMdecode\\DecodeImage\\test325\\format\\" + "FormatException" + "_" + fileName + ".bmp", mat);
	    			failed++;
	    			format++;
				}	    				    		 	    	    		
	        }
	        System.out.println("识别成功：" + successed + "\n识别失败：" + failed + "\n识别成功率：" + ((float)successed / (failed + successed)));
	        System.out.println("NotFound：" + notfound + "\nFormat：" + format + "\nChecksum：" + checksum);
	        successed = 0;
	        failed = 0;	
		}
		else {
			File file = new File("F:\\DMdecode\\DecodeImage\\White");
	        File [] files = file.listFiles();
	        for (int i = 0; i < files.length; i++)
	        {
	            String fileName = files[i].getName();
	            String imgPath = "F:\\DMdecode\\DecodeImage\\White\\" + fileName;
	            
	            Mat mat = Imgcodecs.imread(imgPath);
	    		
	    		BufferedImage img = Mat2BufImg(mat, ".bmp");
	    		LuminanceSource source = new BufferedImageLuminanceSource(img);
	    		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	    		Result result;
	    		try {
					result = new DataMatrixReader().decode(bitmap);
					System.out.println(String.valueOf(result.getText()));
	    			Imgcodecs.imwrite("F:\\DMdecode\\DecodeImage\\DecodeResult\\White\\" + result + "_" + i + ".bmp", mat);
	    			successed++;
				} catch (NotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
	    			Imgcodecs.imwrite("F:\\DMdecode\\DecodeImage\\DecodeResult\\White\\" + "NotFoundException" + "_" + i + ".bmp", mat);
	    			failed++;
				} catch (ChecksumException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Imgcodecs.imwrite("F:\\DMdecode\\DecodeImage\\DecodeResult\\White\\" + "ChecksumException" + "_" + i + ".bmp", mat);
	    			failed++;
				} catch (FormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Imgcodecs.imwrite("F:\\DMdecode\\DecodeImage\\DecodeResult\\White\\" + "FormatException" + "_" + i + ".bmp", mat);
	    			failed++;
				}	    		
	        }
	        System.out.println("识别成功：" + successed + "\n识别失败：" + failed + "\n识别成功率：" + ((float)successed / (failed + successed)));
	        successed = 0;
	        failed = 0;
		}
	}
	
		
		/*String imgPath = "F:\\DMdecode\\DecodeImage\\test1.bmp";
		
		Mat mat = Imgcodecs.imread(imgPath);
		
		BufferedImage img = Mat2BufImg(mat, ".bmp");
		LuminanceSource source = new BufferedImageLuminanceSource(img);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		//System.out.print(bitmap);
		Result result;
		try {
			result = new DataMatrixReader().decode(bitmap);
			System.out.println(String.valueOf(result.getText()));
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}	   		
		
	}*/
		
		/*File file = new File("F:\\DMdecode\\DecodeImage\\test325\\testNew");
        File [] files = file.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            String fileName = files[i].getName();
            String imgPath = "F:\\DMdecode\\DecodeImage\\test325\\testNew\\" + fileName;
            
            Mat mat = Imgcodecs.imread(imgPath);
    		
    		BufferedImage img = Mat2BufImg(mat, ".bmp");
    		LuminanceSource source = new BufferedImageLuminanceSource(img);
    		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
    		Result result;
    		
			try {
				result = new DataMatrixReader().decode(bitmap);
				System.out.println(String.valueOf(result.getText()));
				successed++;
    			
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				failed++;
				notfound++;
			} catch (ChecksumException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				failed++;
				checksum++;
			} catch (FormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				failed++;
				format++;
			}	    				    		 	    	    		
        }
        System.out.println("识别成功：" + successed + "\n识别失败：" + failed + "\n识别成功率：" + ((float)successed / (failed + successed)));
        System.out.println("NotFound：" + notfound + "\nFormat：" + format + "\nChecksum：" + checksum);
    }*/

	

	public static BufferedImage Mat2BufImg(Mat matrix, String fileExtension) {
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(fileExtension, matrix, mob);
		byte[] byteArray = mob.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImage;
	}

	public static Mat BufImg2Mat(BufferedImage original, int imgType, int matType) {
		if (original == null) {
			throw new IllegalArgumentException("original == null");
		}

		if (original.getType() != imgType) {


			BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);


			Graphics2D g = image.createGraphics();
			try {
				g.setComposite(AlphaComposite.Src);
				g.drawImage(original, 0, 0, null);
			} finally {
				g.dispose();
			}
		}

		byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(original.getHeight(), original.getWidth(), matType);
		mat.put(0, 0, pixels);
		return mat;
	}
}
