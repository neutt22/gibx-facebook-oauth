import java.awt.Color;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;


public class GUIWorker {
	
	private StyledDocument docBuffer;
	private Style defaultStyle = StyleContext.getDefaultStyleContext ().getStyle (StyleContext.DEFAULT_STYLE);
	private Style STYLE_SUCCESS;
	
	public GUIWorker(StyledDocument doc){
		docBuffer = doc;
		STYLE_SUCCESS = docBuffer.addStyle("STYLE_SUCCESS", defaultStyle);
		StyleConstants.setForeground(STYLE_SUCCESS, Color.decode("#00CC33"));
		StyleConstants.setBold(STYLE_SUCCESS, true);
		worker.execute();
	}
	
	SwingWorker<Boolean, String> worker = new SwingWorker<Boolean, String>(){

		@Override
		protected Boolean doInBackground() throws Exception {
			for(int i = 0; i < 100; i++){
				Thread.sleep(100);
				publish(i + " haha");
			}
			return true;
		}
		
		@Override
		protected void process(List<String> chunks){
			String i = chunks.get(chunks.size() - 1);
			try {
				docBuffer.insertString(docBuffer.getLength(), i, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		protected void done(){
			boolean status;
		}
		
	};

}
