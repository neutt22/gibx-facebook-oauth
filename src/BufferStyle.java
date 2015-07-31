import javax.swing.text.Style;


public class BufferStyle {
	
	private String message;
	private Style style;
	
	public BufferStyle(String msg, Style style){
		message = msg;
		this.style = style;
	}
	
	public String getMessage(){ return message; }
	public Style getStyle(){ return style; }
	

}
