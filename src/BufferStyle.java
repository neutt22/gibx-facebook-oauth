import javax.swing.text.Style;


public class BufferStyle {
	
	private String message;
	private Style style;
	private int progress = 0;
	
	public BufferStyle(String msg, Style style){
		message = msg;
		this.style = style;
	}
	
	public BufferStyle(String msg, Style style, int progress){
		message = msg;
		this.progress = progress;
		this.style = style;
	}
	
	public String getMessage(){ return message; }
	public Style getStyle(){ return style; }
	public int getProgress(){ return progress; }
	

}
