import java.awt.Color;
import java.util.ArrayList;

public class ImageProcessor {
	// provide a function for reducing the width of an image using
	// the minCostVC algorithm implemented in the DynamicProgramming Class
	int width;
	int height;
	Picture picture;
	
	public ImageProcessor(String imageFile){
		// constructor for ImageProcessor
		picture = new Picture(imageFile);
		width = picture.width();
		height = picture.height();
	}// end constructor ImageProcessor(...)
	
	public Picture reduceWidth(double x){
		// returns a new picture whose width is [x * width]
		int newWidth = (int) Math.round(width * x);
	
		// initialize the importances matrix
		int[][] importance = new int[height][width];
		Color c1, c2;
		
		// create a new Picture with the new width
		int currentWidth = width;
		Picture resized = new Picture(currentWidth, height);
		
		while(currentWidth != newWidth-1){
			// create a new pixels array for the new image
			ArrayList<ArrayList<Pixel>> pixels = new ArrayList<ArrayList<Pixel>>();
			resized = new Picture(currentWidth, height);
			
			for(int i = 0; i < height; i++){
				// for loop over rows in picture
				pixels.add(new ArrayList<Pixel>());
				// initialize y and x importance
				int yImportance;
				int xImportance;

				for(int j = 0; j < width; j++){
					// for loop over columns in picture
					
					if(i == 0){
						c1 = picture.get(j, height-1);
						c2 = picture.get(j, i+1);
					}else if(i == height-1){
						c1 = picture.get(j, i-1);
						c2 = picture.get(j, 0);
					}else{
						c1 = picture.get(j, i-1);
						c2 = picture.get(j, i+1);
					}// end if height is 0
					
					yImportance = distance(new Pixel(c1), new Pixel(c2));
					
					if(j == 0){
						c1 = picture.get(width-1, i);
						c2 = picture.get(j+1, i);
					}else if(j == width-1){
						c1 = picture.get(0, i);
						c2 = picture.get(j-1, i);
					}else{
						c1 = picture.get(j-1, i);
						c2 = picture.get(j+1, i);
					}// end if height is 0
					
					xImportance = distance(new Pixel(c1), new Pixel(c2));
					// set the importance to the sum of the x and y importance
					importance[i][j] = yImportance + xImportance;
					
					// add the pixel to our list
					c1 = picture.get(j, i);
					pixels.get(i).add(new Pixel(c1));
				}// end for loop over columns
				
			}// end for loop over rows
			
			// compute our minimum cut
			ArrayList<Integer> minCut = DynamicProgramming.minCostVC(importance);
			
			for(int i = 0; i < height; i++){
				// for loop over the rows in the picture
				int _y = minCut.get(i+1);
				// don't copy this pixel
				pixels.get(i).remove(_y);
			}// end for loop over the height
			
			// finally copy over all our pixels into the new picture
			for(int i = 0; i < height; i++){
				// for loop over rows in new picture
						
				for(int j = 0; j < currentWidth-1; j++){
					// for loop over columns in new picture
					Pixel p = pixels.get(i).get(j);
					resized.set(j, i, p.color);
				}// end for loop over columns in the new picture
				
			}// end for loop over all pixels
			
			currentWidth--;
		}// end while loop
				
		return resized;
	}// end function reduceWidth(...)
	
	private int distance(Pixel p1, Pixel p2){
		return (int) Math.pow((p1.red-p2.red), 2) + (int) Math.pow((p1.green-p2.green), 2) + (int) Math.pow((p1.blue-p2.blue), 2);
	}// end function to return distance between two Pixels
	
	private class Pixel {
		int red;
		int green;
		int blue;
		Color color;
		
		public Pixel(Color c){
			red = c.getRed();
			green = c.getGreen();
			blue = c.getBlue();
			color = c;
		}// end Pixel constructor
		
	}// end private class Pixel
	
}// end class ImageProcessor