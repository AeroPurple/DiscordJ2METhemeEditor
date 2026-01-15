import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.MaskFormatter;

import cc.nnproject.json.JSON;
import cc.nnproject.json.JSONObject;

public class UserInterface extends JFrame {
	final int APP_WIDTH=900;
	final int APP_HEIGHT=700;
	Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
	final int SCREEN_WIDTH=(int) screenSize.getWidth();
	final int SCREEN_HEIGHT=(int) screenSize.getHeight();
	final int TOOLBAR_HEIGHT=30;
	int colourPaneWidth=325;
	final int COLOUR_PANE_HEIGHT=1210;
	final int SCROLLBAR_WIDTH=((Integer)UIManager.get("ScrollBar.width")).intValue()+1;
	
	final int LABEL_X=6;
	final int HEADER_LABEL_HEIGHT=20;
	final int LABEL_HEIGHT=18;
	int buttonX=220;
	final int BUTTON_WIDTH=24;
	int inputX=buttonX+BUTTON_WIDTH+4;
	int inputWidth=50;
	
	static JToolBar toolbar;
	static JButton newBtn, openBtn, saveBtn, infoBtn;
	static JFormattedTextField channelViewBackgroundColorInput,
								channelViewEmptyTextColorInput,
								timestampColorInput,
								selectedTimestampColorInput,
								linkColorInput,
								messageAuthorColorInput,
								messageContentColorInput,
								recipientMessageContentColorInput,
								statusMessageContentColorInput,
								selectedMessageBackgroundColorInput,
								selectedMessageAuthorColorInput,
								selectedMessageContentColorInput,
								selectedRecipientMessageContentColorInput,
								selectedStatusMessageContentColorInput,
								embedBackgroundColorInput,
								embedTitleColorInput,
								embedDescriptionColorInput,
								selectedEmbedBackgroundColorInput,
								selectedEmbedTitleColorInput,
								selectedEmbedDescriptionColorInput,
								buttonBackgroundColorInput,
								buttonTextColorInput,
								selectedButtonBackgroundColorInput,
								selectedButtonTextColorInput,
								bannerBackgroundColorInput,
								bannerTextColorInput,
								outdatedBannerBackgroundColorInput,
								outdatedBannerTextColorInput,
								typingBannerBackgroundColorInput,
								typingBannerTextColorInput,
								unreadIndicatorBackgroundColorInput,
								unreadIndicatorTextColorInput,
								recipientMessageConnectorColorInput,
								listBackgroundColorInput,
								listTextColorInput,
								listMutedTextColorInput,
								listDescriptionTextColorInput,
								listSelectedBackgroundColorInput,
								listSelectedTextColorInput,
								listSelectedMutedTextColorInput,
								listSelectedDescriptionTextColorInput,
								listNoItemsTextColorInput,
								listIndicatorColorInput,
								dialogBackgroundColorInput,
								dialogTextColorInput,
								emojiPickerBackgroundColorInput,
								loadingScreenBackgroundColorInput,
								loadingScreenTextColorInput,
								keyMapperBackgroundColorInput,
								keyMapperTextColorInput,
								imagePreviewBackgroundColorInput,
								imagePreviewTextColorInput,
								subtextColorInput,
								monospaceTextBackgroundColorInput,
								forwardedTextColorInput,
								editedTextColorInput,
								scrollbarColorInput,
								scrollbarHandleColorInput;
	
	boolean savedBefore=true; //TODO implement unsaved file logic
	
	public UserInterface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds((SCREEN_WIDTH-APP_WIDTH)/2, (SCREEN_HEIGHT-APP_HEIGHT)/2, APP_WIDTH, APP_HEIGHT);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		JPanel contentPane=new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		toolbar=new JToolBar();
		toolbar.setBounds(0, 0, APP_WIDTH, TOOLBAR_HEIGHT);
		toolbar.setFloatable(false);
		
		final JPopupMenu newMenu=new JPopupMenu();
		
		JMenuItem newDark=new JMenuItem("Dark Theme");
		JMenuItem newLight=new JMenuItem("Light Theme");
		JMenuItem newBlack=new JMenuItem("Black Theme");
		
		newDark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Colors.restoreColors(0);
				updateInputBoxes();
			}
		});
		newLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Colors.restoreColors(1);
				updateInputBoxes();
			}
		});
		newBlack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Colors.restoreColors(2);
				updateInputBoxes();
			}
		});
		
		newMenu.add(newDark);
		newMenu.add(newLight);
		newMenu.add(newBlack);
		
		newBtn=new JButton(new ImageIcon("res/page.png"));
		newBtn.setToolTipText("New");
		newBtn.setFocusable(false);
		newBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				newMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		
		openBtn=new JButton(new ImageIcon("res/folder.png"));
		openBtn.setToolTipText("Open");
		openBtn.setFocusable(false);
		openBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				openBtn_mouseClicked(e);
			}
		});
		
		saveBtn=new JButton("Save"); //TODO add an icon
		saveBtn.setToolTipText("Save");
		saveBtn.setFocusable(false);
		saveBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				saveBtn_mouseClicked(e);
			}
		});
		
		infoBtn=new JButton(new ImageIcon("res/information.png"));
		infoBtn.setToolTipText("Info");
		infoBtn.setFocusable(false);
		
		toolbar.add(newBtn);
		toolbar.add(openBtn);
		toolbar.add(saveBtn);
		toolbar.add(infoBtn);
		
		JPanel colourPane=new JPanel();
		colourPane.setLayout(null);
		colourPane.setPreferredSize(new Dimension(colourPaneWidth-SCROLLBAR_WIDTH, COLOUR_PANE_HEIGHT));
		
		//maybe there's a more efficient way to do this...
		
		int labelY=2;
		final int LABEL_RIGHT_PADDING=28;
		final int INPUT_RIGHT_PADDING=8;
		
		//pane width will be determined by the widest label
		//at least, it should be, unless some really weird font is used
		
		JLabel selectedRecipientMessageContentColorLabel=new JLabel("selectedRecipientMessageContentColor");
		selectedRecipientMessageContentColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedRecipientMessageContentColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedRecipientMessageContentColorLabel);
		FontMetrics met=selectedRecipientMessageContentColorLabel.getFontMetrics(selectedRecipientMessageContentColorLabel.getFont());
		colourPaneWidth=met.stringWidth(selectedRecipientMessageContentColorLabel.getText())+LABEL_RIGHT_PADDING;
		colourPane.remove(selectedRecipientMessageContentColorLabel);
		
		channelViewBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		channelViewBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(channelViewBackgroundColorInput);
		channelViewBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		int maxWidth=0;
		for (int i=0; i<16; i++) {
			channelViewBackgroundColorInput.setText(String.format("%1$s%1$s%1$s%1$s%1$s%1$s", Integer.toHexString(i)));
			FontMetrics met2=channelViewBackgroundColorInput.getFontMetrics(channelViewBackgroundColorInput.getFont());
			int curWidth=met2.stringWidth(channelViewBackgroundColorInput.getText());
			if (maxWidth<curWidth) {
				maxWidth=curWidth;
			}
			System.out.println(String.format("%s is %dpx wide",channelViewBackgroundColorInput.getText(), curWidth));
		}
		inputWidth=maxWidth+INPUT_RIGHT_PADDING;
		System.out.println(String.format("inputs are now %d+%d=%dpx wide", maxWidth, INPUT_RIGHT_PADDING, inputWidth));
		colourPane.remove(channelViewBackgroundColorInput);
		
		buttonX=colourPaneWidth;
		inputX=buttonX+BUTTON_WIDTH+4;
		
		JLabel chHeader=new JLabel("Channel view");
		chHeader.setBounds(LABEL_X, labelY, colourPaneWidth, HEADER_LABEL_HEIGHT);
		chHeader.setFont(chHeader.getFont().deriveFont(Font.BOLD));
		chHeader.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(chHeader);
		
		labelY+=HEADER_LABEL_HEIGHT;
		
		JLabel channelViewBackgroundColorLabel=new JLabel("channelViewBackgroundColor");
		channelViewBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		channelViewBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(channelViewBackgroundColorLabel);
		
		JButton channelViewBackgroundColorButton=new JButton();
		channelViewBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(channelViewBackgroundColorButton);
		channelViewBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.channelViewBackgroundColor=chooseHexColor(Colors.channelViewBackgroundColor);
				updateInputBoxes();
			}
		});
		
		channelViewBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		channelViewBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(channelViewBackgroundColorInput);
		channelViewBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		channelViewBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (channelViewBackgroundColorInput.getValue()!=null && ((String) channelViewBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) channelViewBackgroundColorInput.getValue(), 16);
				Colors.channelViewBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel channelViewEmptyTextColorLabel=new JLabel("channelViewEmptyTextColor");
		channelViewEmptyTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		channelViewEmptyTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(channelViewEmptyTextColorLabel);
		
		JButton channelViewEmptyTextColorButton=new JButton();
		channelViewEmptyTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(channelViewEmptyTextColorButton);
		channelViewEmptyTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.channelViewEmptyTextColor=chooseHexColor(Colors.channelViewEmptyTextColor);
				updateInputBoxes();
			}
		});
		
		channelViewEmptyTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		channelViewEmptyTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(channelViewEmptyTextColorInput);
		channelViewEmptyTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		channelViewEmptyTextColorInput.addPropertyChangeListener("value", evt->{
			if (channelViewEmptyTextColorInput.getValue()!=null && ((String) channelViewEmptyTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) channelViewEmptyTextColorInput.getValue(), 16);
				Colors.channelViewEmptyTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel timestampColorLabel=new JLabel("timestampColor");
		timestampColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		timestampColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(timestampColorLabel);
		
		JButton timestampColorButton=new JButton();
		timestampColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(timestampColorButton);
		timestampColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.timestampColor=chooseHexColor(Colors.timestampColor);
				updateInputBoxes();
			}
		});
		
		timestampColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		timestampColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(timestampColorInput);
		timestampColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		timestampColorInput.addPropertyChangeListener("value", evt->{
			if (timestampColorInput.getValue()!=null && ((String) timestampColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) timestampColorInput.getValue(), 16);
				Colors.timestampColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedTimestampColorLabel=new JLabel("selectedTimestampColor");
		selectedTimestampColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedTimestampColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedTimestampColorLabel);
		
		JButton selectedTimestampColorButton=new JButton();
		selectedTimestampColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedTimestampColorButton);
		selectedTimestampColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedTimestampColor=chooseHexColor(Colors.selectedTimestampColor);
				updateInputBoxes();
			}
		});
		
		selectedTimestampColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedTimestampColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedTimestampColorInput);
		selectedTimestampColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedTimestampColorInput.addPropertyChangeListener("value", evt->{
			if (selectedTimestampColorInput.getValue()!=null && ((String) selectedTimestampColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedTimestampColorInput.getValue(), 16);
				Colors.selectedTimestampColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel linkColorLabel=new JLabel("linkColor");
		linkColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		linkColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(linkColorLabel);
		
		JButton linkColorButton=new JButton();
		linkColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(linkColorButton);
		linkColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.linkColor=chooseHexColor(Colors.linkColor);
				updateInputBoxes();
			}
		});
		
		linkColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		linkColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(linkColorInput);
		linkColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		linkColorInput.addPropertyChangeListener("value", evt->{
			if (linkColorInput.getValue()!=null && ((String) linkColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) linkColorInput.getValue(), 16);
				Colors.linkColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel messageAuthorColorLabel=new JLabel("messageAuthorColor");
		messageAuthorColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		messageAuthorColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(messageAuthorColorLabel);
		
		JButton messageAuthorColorButton=new JButton();
		messageAuthorColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(messageAuthorColorButton);
		messageAuthorColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.messageAuthorColor=chooseHexColor(Colors.messageAuthorColor);
				updateInputBoxes();
			}
		});
		
		messageAuthorColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		messageAuthorColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(messageAuthorColorInput);
		messageAuthorColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		messageAuthorColorInput.addPropertyChangeListener("value", evt->{
			if (messageAuthorColorInput.getValue()!=null && ((String) messageAuthorColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) messageAuthorColorInput.getValue(), 16);
				Colors.messageAuthorColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel messageContentColorLabel=new JLabel("messageContentColor");
		messageContentColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		messageContentColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(messageContentColorLabel);
		
		JButton messageContentColorButton=new JButton();
		messageContentColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(messageContentColorButton);
		messageContentColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.messageContentColor=chooseHexColor(Colors.messageContentColor);
				updateInputBoxes();
			}
		});
		
		messageContentColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		messageContentColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(messageContentColorInput);
		messageContentColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		messageContentColorInput.addPropertyChangeListener("value", evt->{
			if (messageContentColorInput.getValue()!=null && ((String) messageContentColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) messageContentColorInput.getValue(), 16);
				Colors.messageContentColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel recipientMessageContentColorLabel=new JLabel("recipientMessageContentColor");
		recipientMessageContentColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		recipientMessageContentColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(recipientMessageContentColorLabel);
		
		JButton recipientMessageContentColorButton=new JButton();
		recipientMessageContentColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(recipientMessageContentColorButton);
		recipientMessageContentColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.recipientMessageContentColor=chooseHexColor(Colors.recipientMessageContentColor);
				updateInputBoxes();
			}
		});
		
		recipientMessageContentColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		recipientMessageContentColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(recipientMessageContentColorInput);
		recipientMessageContentColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		recipientMessageContentColorInput.addPropertyChangeListener("value", evt->{
			if (recipientMessageContentColorInput.getValue()!=null && ((String) recipientMessageContentColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) recipientMessageContentColorInput.getValue(), 16);
				Colors.recipientMessageContentColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel statusMessageContentColorLabel=new JLabel("statusMessageContentColor");
		statusMessageContentColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		statusMessageContentColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(statusMessageContentColorLabel);
		
		JButton statusMessageContentColorButton=new JButton();
		statusMessageContentColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(statusMessageContentColorButton);
		statusMessageContentColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.statusMessageContentColor=chooseHexColor(Colors.statusMessageContentColor);
				updateInputBoxes();
			}
		});
		
		statusMessageContentColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		statusMessageContentColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(statusMessageContentColorInput);
		statusMessageContentColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		statusMessageContentColorInput.addPropertyChangeListener("value", evt->{
			if (statusMessageContentColorInput.getValue()!=null && ((String) statusMessageContentColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) statusMessageContentColorInput.getValue(), 16);
				Colors.statusMessageContentColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedMessageBackgroundColorLabel=new JLabel("selectedMessageBackgroundColor");
		selectedMessageBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedMessageBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedMessageBackgroundColorLabel);
		
		JButton selectedMessageBackgroundColorButton=new JButton();
		selectedMessageBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedMessageBackgroundColorButton);
		selectedMessageBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedMessageBackgroundColor=chooseHexColor(Colors.selectedMessageBackgroundColor);
				updateInputBoxes();
			}
		});
		
		selectedMessageBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedMessageBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedMessageBackgroundColorInput);
		selectedMessageBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedMessageBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (selectedMessageBackgroundColorInput.getValue()!=null && ((String) selectedMessageBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedMessageBackgroundColorInput.getValue(), 16);
				Colors.selectedMessageBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedMessageAuthorColorLabel=new JLabel("selectedMessageAuthorColor");
		selectedMessageAuthorColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedMessageAuthorColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedMessageAuthorColorLabel);
		
		JButton selectedMessageAuthorColorButton=new JButton();
		selectedMessageAuthorColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedMessageAuthorColorButton);
		selectedMessageAuthorColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedMessageAuthorColor=chooseHexColor(Colors.selectedMessageAuthorColor);
				updateInputBoxes();
			}
		});
		
		selectedMessageAuthorColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedMessageAuthorColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedMessageAuthorColorInput);
		selectedMessageAuthorColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedMessageAuthorColorInput.addPropertyChangeListener("value", evt->{
			if (selectedMessageAuthorColorInput.getValue()!=null && ((String) selectedMessageAuthorColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedMessageAuthorColorInput.getValue(), 16);
				Colors.selectedMessageAuthorColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedMessageContentColorLabel=new JLabel("selectedMessageContentColor");
		selectedMessageContentColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedMessageContentColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedMessageContentColorLabel);
		
		JButton selectedMessageContentColorButton=new JButton();
		selectedMessageContentColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedMessageContentColorButton);
		selectedMessageContentColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedMessageContentColor=chooseHexColor(Colors.selectedMessageContentColor);
				updateInputBoxes();
			}
		});
		
		selectedMessageContentColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedMessageContentColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedMessageContentColorInput);
		selectedMessageContentColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedMessageContentColorInput.addPropertyChangeListener("value", evt->{
			if (selectedMessageContentColorInput.getValue()!=null && ((String) selectedMessageContentColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedMessageContentColorInput.getValue(), 16);
				Colors.selectedMessageContentColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		selectedRecipientMessageContentColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		colourPane.add(selectedRecipientMessageContentColorLabel);
		
		JButton selectedRecipientMessageContentColorButton=new JButton();
		selectedRecipientMessageContentColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedRecipientMessageContentColorButton);
		selectedRecipientMessageContentColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedRecipientMessageContentColor=chooseHexColor(Colors.selectedRecipientMessageContentColor);
				updateInputBoxes();
			}
		});
		
		selectedRecipientMessageContentColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedRecipientMessageContentColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedRecipientMessageContentColorInput);
		selectedRecipientMessageContentColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedRecipientMessageContentColorInput.addPropertyChangeListener("value", evt->{
			if (selectedRecipientMessageContentColorInput.getValue()!=null && ((String) selectedRecipientMessageContentColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedRecipientMessageContentColorInput.getValue(), 16);
				Colors.selectedRecipientMessageContentColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedStatusMessageContentColorLabel=new JLabel("selectedStatusMessageContentColor");
		selectedStatusMessageContentColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedStatusMessageContentColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedStatusMessageContentColorLabel);
		
		JButton selectedStatusMessageContentColorButton=new JButton();
		selectedStatusMessageContentColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedStatusMessageContentColorButton);
		selectedStatusMessageContentColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedStatusMessageContentColor=chooseHexColor(Colors.selectedStatusMessageContentColor);
				updateInputBoxes();
			}
		});
		
		selectedStatusMessageContentColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedStatusMessageContentColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedStatusMessageContentColorInput);
		selectedStatusMessageContentColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedStatusMessageContentColorInput.addPropertyChangeListener("value", evt->{
			if (selectedStatusMessageContentColorInput.getValue()!=null && ((String) selectedStatusMessageContentColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedStatusMessageContentColorInput.getValue(), 16);
				Colors.selectedStatusMessageContentColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel embedHeader=new JLabel("Embedded content");
		embedHeader.setBounds(LABEL_X, labelY, colourPaneWidth, HEADER_LABEL_HEIGHT);
		embedHeader.setFont(embedHeader.getFont().deriveFont(Font.BOLD));
		embedHeader.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(embedHeader);
		
		labelY+=HEADER_LABEL_HEIGHT;
		
		JLabel embedBackgroundColorLabel=new JLabel("embedBackgroundColor");
		embedBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		embedBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(embedBackgroundColorLabel);
		
		JButton embedBackgroundColorButton=new JButton();
		embedBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(embedBackgroundColorButton);
		embedBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.embedBackgroundColor=chooseHexColor(Colors.embedBackgroundColor);
				updateInputBoxes();
			}
		});
		
		embedBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		embedBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(embedBackgroundColorInput);
		embedBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		embedBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (embedBackgroundColorInput.getValue()!=null && ((String) embedBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) embedBackgroundColorInput.getValue(), 16);
				Colors.embedBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel embedTitleColorLabel=new JLabel("embedTitleColor");
		embedTitleColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		embedTitleColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(embedTitleColorLabel);
		
		JButton embedTitleColorButton=new JButton();
		embedTitleColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(embedTitleColorButton);
		embedTitleColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.embedTitleColor=chooseHexColor(Colors.embedTitleColor);
				updateInputBoxes();
			}
		});
		
		embedTitleColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		embedTitleColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(embedTitleColorInput);
		embedTitleColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		embedTitleColorInput.addPropertyChangeListener("value", evt->{
			if (embedTitleColorInput.getValue()!=null && ((String) embedTitleColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) embedTitleColorInput.getValue(), 16);
				Colors.embedTitleColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel embedDescriptionColorLabel=new JLabel("embedDescriptionColor");
		embedDescriptionColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		embedDescriptionColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(embedDescriptionColorLabel);
		
		JButton embedDescriptionColorButton=new JButton();
		embedDescriptionColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(embedDescriptionColorButton);
		embedDescriptionColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.embedDescriptionColor=chooseHexColor(Colors.embedDescriptionColor);
				updateInputBoxes();
			}
		});
		
		embedDescriptionColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		embedDescriptionColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(embedDescriptionColorInput);
		embedDescriptionColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		embedDescriptionColorInput.addPropertyChangeListener("value", evt->{
			if (embedDescriptionColorInput.getValue()!=null && ((String) embedDescriptionColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) embedDescriptionColorInput.getValue(), 16);
				Colors.embedDescriptionColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedEmbedBackgroundColorLabel=new JLabel("selectedEmbedBackgroundColor");
		selectedEmbedBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedEmbedBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedEmbedBackgroundColorLabel);
		
		JButton selectedEmbedBackgroundColorButton=new JButton();
		selectedEmbedBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedEmbedBackgroundColorButton);
		selectedEmbedBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedEmbedBackgroundColor=chooseHexColor(Colors.selectedEmbedBackgroundColor);
				updateInputBoxes();
			}
		});
		
		selectedEmbedBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedEmbedBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedEmbedBackgroundColorInput);
		selectedEmbedBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedEmbedBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (selectedEmbedBackgroundColorInput.getValue()!=null && ((String) selectedEmbedBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedEmbedBackgroundColorInput.getValue(), 16);
				Colors.selectedEmbedBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedEmbedTitleColorLabel=new JLabel("selectedEmbedTitleColor");
		selectedEmbedTitleColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedEmbedTitleColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedEmbedTitleColorLabel);
		
		JButton selectedEmbedTitleColorButton=new JButton();
		selectedEmbedTitleColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedEmbedTitleColorButton);
		selectedEmbedTitleColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedEmbedTitleColor=chooseHexColor(Colors.selectedEmbedTitleColor);
				updateInputBoxes();
			}
		});
		
		selectedEmbedTitleColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedEmbedTitleColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedEmbedTitleColorInput);
		selectedEmbedTitleColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedEmbedTitleColorInput.addPropertyChangeListener("value", evt->{
			if (selectedEmbedTitleColorInput.getValue()!=null && ((String) selectedEmbedTitleColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedEmbedTitleColorInput.getValue(), 16);
				Colors.selectedEmbedTitleColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedEmbedDescriptionColorLabel=new JLabel("selectedEmbedDescriptionColor");
		selectedEmbedDescriptionColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedEmbedDescriptionColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedEmbedDescriptionColorLabel);
		
		JButton selectedEmbedDescriptionColorButton=new JButton();
		selectedEmbedDescriptionColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedEmbedDescriptionColorButton);
		selectedEmbedDescriptionColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedEmbedDescriptionColor=chooseHexColor(Colors.selectedEmbedDescriptionColor);
				updateInputBoxes();
			}
		});
		
		selectedEmbedDescriptionColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedEmbedDescriptionColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedEmbedDescriptionColorInput);
		selectedEmbedDescriptionColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedEmbedDescriptionColorInput.addPropertyChangeListener("value", evt->{
			if (selectedEmbedDescriptionColorInput.getValue()!=null && ((String) selectedEmbedDescriptionColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedEmbedDescriptionColorInput.getValue(), 16);
				Colors.selectedEmbedDescriptionColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel btnHeader=new JLabel("Buttons");
		btnHeader.setBounds(LABEL_X, labelY, colourPaneWidth, HEADER_LABEL_HEIGHT);
		btnHeader.setFont(btnHeader.getFont().deriveFont(Font.BOLD));
		btnHeader.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(btnHeader);
		
		labelY+=HEADER_LABEL_HEIGHT;
		
		JLabel buttonBackgroundColorLabel=new JLabel("buttonBackgroundColor");
		buttonBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		buttonBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(buttonBackgroundColorLabel);
		
		JButton buttonBackgroundColorButton=new JButton();
		buttonBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(buttonBackgroundColorButton);
		buttonBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.buttonBackgroundColor=chooseHexColor(Colors.buttonBackgroundColor);
				updateInputBoxes();
			}
		});
		
		buttonBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		buttonBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(buttonBackgroundColorInput);
		buttonBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		buttonBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (buttonBackgroundColorInput.getValue()!=null && ((String) buttonBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) buttonBackgroundColorInput.getValue(), 16);
				Colors.buttonBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel buttonTextColorLabel=new JLabel("buttonTextColor");
		buttonTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		buttonTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(buttonTextColorLabel);
		
		JButton buttonTextColorButton=new JButton();
		buttonTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(buttonTextColorButton);
		buttonTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.buttonTextColor=chooseHexColor(Colors.buttonTextColor);
				updateInputBoxes();
			}
		});
		
		buttonTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		buttonTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(buttonTextColorInput);
		buttonTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		buttonTextColorInput.addPropertyChangeListener("value", evt->{
			if (buttonTextColorInput.getValue()!=null && ((String) buttonTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) buttonTextColorInput.getValue(), 16);
				Colors.buttonTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedButtonBackgroundColorLabel=new JLabel("selectedButtonBackgroundColor");
		selectedButtonBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedButtonBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedButtonBackgroundColorLabel);
		
		JButton selectedButtonBackgroundColorButton=new JButton();
		selectedButtonBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedButtonBackgroundColorButton);
		selectedButtonBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedButtonBackgroundColor=chooseHexColor(Colors.selectedButtonBackgroundColor);
				updateInputBoxes();
			}
		});
		
		selectedButtonBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedButtonBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedButtonBackgroundColorInput);
		selectedButtonBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedButtonBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (selectedButtonBackgroundColorInput.getValue()!=null && ((String) selectedButtonBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedButtonBackgroundColorInput.getValue(), 16);
				Colors.selectedButtonBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel selectedButtonTextColorLabel=new JLabel("selectedButtonTextColor");
		selectedButtonTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		selectedButtonTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(selectedButtonTextColorLabel);
		
		JButton selectedButtonTextColorButton=new JButton();
		selectedButtonTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(selectedButtonTextColorButton);
		selectedButtonTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.selectedButtonTextColor=chooseHexColor(Colors.selectedButtonTextColor);
				updateInputBoxes();
			}
		});
		
		selectedButtonTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		selectedButtonTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(selectedButtonTextColorInput);
		selectedButtonTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		selectedButtonTextColorInput.addPropertyChangeListener("value", evt->{
			if (selectedButtonTextColorInput.getValue()!=null && ((String) selectedButtonTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) selectedButtonTextColorInput.getValue(), 16);
				Colors.selectedButtonTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel bannerHeader=new JLabel("Banners");
		bannerHeader.setBounds(LABEL_X, labelY, colourPaneWidth, HEADER_LABEL_HEIGHT);
		bannerHeader.setFont(bannerHeader.getFont().deriveFont(Font.BOLD));
		bannerHeader.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(bannerHeader);
		
		labelY+=HEADER_LABEL_HEIGHT;
		
		JLabel bannerBackgroundColorLabel=new JLabel("bannerBackgroundColor");
		bannerBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		bannerBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(bannerBackgroundColorLabel);
		
		JButton bannerBackgroundColorButton=new JButton();
		bannerBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(bannerBackgroundColorButton);
		bannerBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.bannerBackgroundColor=chooseHexColor(Colors.bannerBackgroundColor);
				updateInputBoxes();
			}
		});
		
		bannerBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		bannerBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(bannerBackgroundColorInput);
		bannerBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		bannerBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (bannerBackgroundColorInput.getValue()!=null && ((String) bannerBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) bannerBackgroundColorInput.getValue(), 16);
				Colors.bannerBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel bannerTextColorLabel=new JLabel("bannerTextColor");
		bannerTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		bannerTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(bannerTextColorLabel);
		
		JButton bannerTextColorButton=new JButton();
		bannerTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(bannerTextColorButton);
		bannerTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.bannerTextColor=chooseHexColor(Colors.bannerTextColor);
				updateInputBoxes();
			}
		});
		
		bannerTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		bannerTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(bannerTextColorInput);
		bannerTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		bannerTextColorInput.addPropertyChangeListener("value", evt->{
			if (bannerTextColorInput.getValue()!=null && ((String) bannerTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) bannerTextColorInput.getValue(), 16);
				Colors.bannerTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel outdatedBannerBackgroundColorLabel=new JLabel("outdatedBannerBackgroundColor");
		outdatedBannerBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		outdatedBannerBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(outdatedBannerBackgroundColorLabel);
		
		JButton outdatedBannerBackgroundColorButton=new JButton();
		outdatedBannerBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(outdatedBannerBackgroundColorButton);
		outdatedBannerBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.outdatedBannerBackgroundColor=chooseHexColor(Colors.outdatedBannerBackgroundColor);
				updateInputBoxes();
			}
		});
		
		outdatedBannerBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		outdatedBannerBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(outdatedBannerBackgroundColorInput);
		outdatedBannerBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		outdatedBannerBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (outdatedBannerBackgroundColorInput.getValue()!=null && ((String) outdatedBannerBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) outdatedBannerBackgroundColorInput.getValue(), 16);
				Colors.outdatedBannerBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel outdatedBannerTextColorLabel=new JLabel("outdatedBannerTextColor");
		outdatedBannerTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		outdatedBannerTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(outdatedBannerTextColorLabel);
		
		JButton outdatedBannerTextColorButton=new JButton();
		outdatedBannerTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(outdatedBannerTextColorButton);
		outdatedBannerTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.outdatedBannerTextColor=chooseHexColor(Colors.outdatedBannerTextColor);
				updateInputBoxes();
			}
		});
		
		outdatedBannerTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		outdatedBannerTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(outdatedBannerTextColorInput);
		outdatedBannerTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		outdatedBannerTextColorInput.addPropertyChangeListener("value", evt->{
			if (outdatedBannerTextColorInput.getValue()!=null && ((String) outdatedBannerTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) outdatedBannerTextColorInput.getValue(), 16);
				Colors.outdatedBannerTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel typingBannerBackgroundColorLabel=new JLabel("typingBannerBackgroundColor");
		typingBannerBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		typingBannerBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(typingBannerBackgroundColorLabel);
		
		JButton typingBannerBackgroundColorButton=new JButton();
		typingBannerBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(typingBannerBackgroundColorButton);
		typingBannerBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.typingBannerBackgroundColor=chooseHexColor(Colors.typingBannerBackgroundColor);
				updateInputBoxes();
			}
		});
		
		typingBannerBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		typingBannerBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(typingBannerBackgroundColorInput);
		typingBannerBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		typingBannerBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (typingBannerBackgroundColorInput.getValue()!=null && ((String) typingBannerBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) typingBannerBackgroundColorInput.getValue(), 16);
				Colors.typingBannerBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel typingBannerTextColorLabel=new JLabel("typingBannerTextColor");
		typingBannerTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		typingBannerTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(typingBannerTextColorLabel);
		
		JButton typingBannerTextColorButton=new JButton();
		typingBannerTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(typingBannerTextColorButton);
		typingBannerTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.typingBannerTextColor=chooseHexColor(Colors.typingBannerTextColor);
				updateInputBoxes();
			}
		});
		
		typingBannerTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		typingBannerTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(typingBannerTextColorInput);
		typingBannerTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		typingBannerTextColorInput.addPropertyChangeListener("value", evt->{
			if (typingBannerTextColorInput.getValue()!=null && ((String) typingBannerTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) typingBannerTextColorInput.getValue(), 16);
				Colors.typingBannerTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel unreadIndicatorBackgroundColorLabel=new JLabel("unreadIndicatorBackgroundColor");
		unreadIndicatorBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		unreadIndicatorBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(unreadIndicatorBackgroundColorLabel);
		
		JButton unreadIndicatorBackgroundColorButton=new JButton();
		unreadIndicatorBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(unreadIndicatorBackgroundColorButton);
		unreadIndicatorBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.unreadIndicatorBackgroundColor=chooseHexColor(Colors.unreadIndicatorBackgroundColor);
				updateInputBoxes();
			}
		});
		
		unreadIndicatorBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		unreadIndicatorBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(unreadIndicatorBackgroundColorInput);
		unreadIndicatorBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		unreadIndicatorBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (unreadIndicatorBackgroundColorInput.getValue()!=null && ((String) unreadIndicatorBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) unreadIndicatorBackgroundColorInput.getValue(), 16);
				Colors.unreadIndicatorBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel unreadIndicatorTextColorLabel=new JLabel("unreadIndicatorTextColor");
		unreadIndicatorTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		unreadIndicatorTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(unreadIndicatorTextColorLabel);
		
		JButton unreadIndicatorTextColorButton=new JButton();
		unreadIndicatorTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(unreadIndicatorTextColorButton);
		unreadIndicatorTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.unreadIndicatorTextColor=chooseHexColor(Colors.unreadIndicatorTextColor);
				updateInputBoxes();
			}
		});
		
		unreadIndicatorTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		unreadIndicatorTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(unreadIndicatorTextColorInput);
		unreadIndicatorTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		unreadIndicatorTextColorInput.addPropertyChangeListener("value", evt->{
			if (unreadIndicatorTextColorInput.getValue()!=null && ((String) unreadIndicatorTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) unreadIndicatorTextColorInput.getValue(), 16);
				Colors.unreadIndicatorTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel recipientMessageConnectorColorLabel=new JLabel("recipientMessageConnectorColor");
		recipientMessageConnectorColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		recipientMessageConnectorColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(recipientMessageConnectorColorLabel);
		
		JButton recipientMessageConnectorColorButton=new JButton();
		recipientMessageConnectorColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(recipientMessageConnectorColorButton);
		recipientMessageConnectorColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.recipientMessageConnectorColor=chooseHexColor(Colors.recipientMessageConnectorColor);
				updateInputBoxes();
			}
		});
		
		recipientMessageConnectorColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		recipientMessageConnectorColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(recipientMessageConnectorColorInput);
		recipientMessageConnectorColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		recipientMessageConnectorColorInput.addPropertyChangeListener("value", evt->{
			if (recipientMessageConnectorColorInput.getValue()!=null && ((String) recipientMessageConnectorColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) recipientMessageConnectorColorInput.getValue(), 16);
				Colors.recipientMessageConnectorColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listHeader=new JLabel("Lists");
		listHeader.setBounds(LABEL_X, labelY, colourPaneWidth, HEADER_LABEL_HEIGHT);
		listHeader.setFont(listHeader.getFont().deriveFont(Font.BOLD));
		listHeader.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listHeader);
		
		labelY+=HEADER_LABEL_HEIGHT;
		
		JLabel listBackgroundColorLabel=new JLabel("listBackgroundColor");
		listBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listBackgroundColorLabel);
		
		JButton listBackgroundColorButton=new JButton();
		listBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listBackgroundColorButton);
		listBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listBackgroundColor=chooseHexColor(Colors.listBackgroundColor);
				updateInputBoxes();
			}
		});
		
		listBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listBackgroundColorInput);
		listBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (listBackgroundColorInput.getValue()!=null && ((String) listBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listBackgroundColorInput.getValue(), 16);
				Colors.listBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listTextColorLabel=new JLabel("listTextColor");
		listTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listTextColorLabel);
		
		JButton listTextColorButton=new JButton();
		listTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listTextColorButton);
		listTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listTextColor=chooseHexColor(Colors.listTextColor);
				updateInputBoxes();
			}
		});
		
		listTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listTextColorInput);
		listTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listTextColorInput.addPropertyChangeListener("value", evt->{
			if (listTextColorInput.getValue()!=null && ((String) listTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listTextColorInput.getValue(), 16);
				Colors.listTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listMutedTextColorLabel=new JLabel("listMutedTextColor");
		listMutedTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listMutedTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listMutedTextColorLabel);
		
		JButton listMutedTextColorButton=new JButton();
		listMutedTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listMutedTextColorButton);
		listMutedTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listMutedTextColor=chooseHexColor(Colors.listMutedTextColor);
				updateInputBoxes();
			}
		});
		
		listMutedTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listMutedTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listMutedTextColorInput);
		listMutedTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listMutedTextColorInput.addPropertyChangeListener("value", evt->{
			if (listMutedTextColorInput.getValue()!=null && ((String) listMutedTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listMutedTextColorInput.getValue(), 16);
				Colors.listMutedTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listDescriptionTextColorLabel=new JLabel("listDescriptionTextColor");
		listDescriptionTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listDescriptionTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listDescriptionTextColorLabel);
		
		JButton listDescriptionTextColorButton=new JButton();
		listDescriptionTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listDescriptionTextColorButton);
		listDescriptionTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listDescriptionTextColor=chooseHexColor(Colors.listDescriptionTextColor);
				updateInputBoxes();
			}
		});
		
		listDescriptionTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listDescriptionTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listDescriptionTextColorInput);
		listDescriptionTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listDescriptionTextColorInput.addPropertyChangeListener("value", evt->{
			if (listDescriptionTextColorInput.getValue()!=null && ((String) listDescriptionTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listDescriptionTextColorInput.getValue(), 16);
				Colors.listDescriptionTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listSelectedBackgroundColorLabel=new JLabel("listSelectedBackgroundColor");
		listSelectedBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listSelectedBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listSelectedBackgroundColorLabel);
		
		JButton listSelectedBackgroundColorButton=new JButton();
		listSelectedBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listSelectedBackgroundColorButton);
		listSelectedBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listSelectedBackgroundColor=chooseHexColor(Colors.listSelectedBackgroundColor);
				updateInputBoxes();
			}
		});
		
		listSelectedBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listSelectedBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listSelectedBackgroundColorInput);
		listSelectedBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listSelectedBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (listSelectedBackgroundColorInput.getValue()!=null && ((String) listSelectedBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listSelectedBackgroundColorInput.getValue(), 16);
				Colors.listSelectedBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listSelectedTextColorLabel=new JLabel("listSelectedTextColor");
		listSelectedTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listSelectedTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listSelectedTextColorLabel);
		
		JButton listSelectedTextColorButton=new JButton();
		listSelectedTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listSelectedTextColorButton);
		listSelectedTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listSelectedTextColor=chooseHexColor(Colors.listSelectedTextColor);
				updateInputBoxes();
			}
		});
		
		listSelectedTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listSelectedTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listSelectedTextColorInput);
		listSelectedTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listSelectedTextColorInput.addPropertyChangeListener("value", evt->{
			if (listSelectedTextColorInput.getValue()!=null && ((String) listSelectedTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listSelectedTextColorInput.getValue(), 16);
				Colors.listSelectedTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listSelectedMutedTextColorLabel=new JLabel("listSelectedMutedTextColor");
		listSelectedMutedTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listSelectedMutedTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listSelectedMutedTextColorLabel);
		
		JButton listSelectedMutedTextColorButton=new JButton();
		listSelectedMutedTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listSelectedMutedTextColorButton);
		listSelectedMutedTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listSelectedMutedTextColor=chooseHexColor(Colors.listSelectedMutedTextColor);
				updateInputBoxes();
			}
		});
		
		listSelectedMutedTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listSelectedMutedTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listSelectedMutedTextColorInput);
		listSelectedMutedTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listSelectedMutedTextColorInput.addPropertyChangeListener("value", evt->{
			if (listSelectedMutedTextColorInput.getValue()!=null && ((String) listSelectedMutedTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listSelectedMutedTextColorInput.getValue(), 16);
				Colors.listSelectedMutedTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listSelectedDescriptionTextColorLabel=new JLabel("listSelectedDescriptionTextColor");
		listSelectedDescriptionTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listSelectedDescriptionTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listSelectedDescriptionTextColorLabel);
		
		JButton listSelectedDescriptionTextColorButton=new JButton();
		listSelectedDescriptionTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listSelectedDescriptionTextColorButton);
		listSelectedDescriptionTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listSelectedDescriptionTextColor=chooseHexColor(Colors.listSelectedDescriptionTextColor);
				updateInputBoxes();
			}
		});
		
		listSelectedDescriptionTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listSelectedDescriptionTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listSelectedDescriptionTextColorInput);
		listSelectedDescriptionTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listSelectedDescriptionTextColorInput.addPropertyChangeListener("value", evt->{
			if (listSelectedDescriptionTextColorInput.getValue()!=null && ((String) listSelectedDescriptionTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listSelectedDescriptionTextColorInput.getValue(), 16);
				Colors.listSelectedDescriptionTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listNoItemsTextColorLabel=new JLabel("listNoItemsTextColor");
		listNoItemsTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listNoItemsTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listNoItemsTextColorLabel);
		
		JButton listNoItemsTextColorButton=new JButton();
		listNoItemsTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listNoItemsTextColorButton);
		listNoItemsTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listNoItemsTextColor=chooseHexColor(Colors.listNoItemsTextColor);
				updateInputBoxes();
			}
		});
		
		listNoItemsTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listNoItemsTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listNoItemsTextColorInput);
		listNoItemsTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listNoItemsTextColorInput.addPropertyChangeListener("value", evt->{
			if (listNoItemsTextColorInput.getValue()!=null && ((String) listNoItemsTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listNoItemsTextColorInput.getValue(), 16);
				Colors.listNoItemsTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel listIndicatorColorLabel=new JLabel("listIndicatorColor");
		listIndicatorColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		listIndicatorColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(listIndicatorColorLabel);
		
		JButton listIndicatorColorButton=new JButton();
		listIndicatorColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(listIndicatorColorButton);
		listIndicatorColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.listIndicatorColor=chooseHexColor(Colors.listIndicatorColor);
				updateInputBoxes();
			}
		});
		
		listIndicatorColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		listIndicatorColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(listIndicatorColorInput);
		listIndicatorColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		listIndicatorColorInput.addPropertyChangeListener("value", evt->{
			if (listIndicatorColorInput.getValue()!=null && ((String) listIndicatorColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) listIndicatorColorInput.getValue(), 16);
				Colors.listIndicatorColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel dialogHeader=new JLabel("Dialogs and pop-ups");
		dialogHeader.setBounds(LABEL_X, labelY, colourPaneWidth, HEADER_LABEL_HEIGHT);
		dialogHeader.setFont(dialogHeader.getFont().deriveFont(Font.BOLD));
		dialogHeader.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(dialogHeader);
		
		labelY+=HEADER_LABEL_HEIGHT;
		
		JLabel dialogBackgroundColorLabel=new JLabel("dialogBackgroundColor");
		dialogBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		dialogBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(dialogBackgroundColorLabel);
		
		JButton dialogBackgroundColorButton=new JButton();
		dialogBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(dialogBackgroundColorButton);
		dialogBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.dialogBackgroundColor=chooseHexColor(Colors.dialogBackgroundColor);
				updateInputBoxes();
			}
		});
		
		dialogBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		dialogBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(dialogBackgroundColorInput);
		dialogBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		dialogBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (dialogBackgroundColorInput.getValue()!=null && ((String) dialogBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) dialogBackgroundColorInput.getValue(), 16);
				Colors.dialogBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel dialogTextColorLabel=new JLabel("dialogTextColor");
		dialogTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		dialogTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(dialogTextColorLabel);
		
		JButton dialogTextColorButton=new JButton();
		dialogTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(dialogTextColorButton);
		dialogTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.dialogTextColor=chooseHexColor(Colors.dialogTextColor);
				updateInputBoxes();
			}
		});
		
		dialogTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		dialogTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(dialogTextColorInput);
		dialogTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		dialogTextColorInput.addPropertyChangeListener("value", evt->{
			if (dialogTextColorInput.getValue()!=null && ((String) dialogTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) dialogTextColorInput.getValue(), 16);
				Colors.dialogTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel emojiPickerBackgroundColorLabel=new JLabel("emojiPickerBackgroundColor");
		emojiPickerBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		emojiPickerBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(emojiPickerBackgroundColorLabel);
		
		JButton emojiPickerBackgroundColorButton=new JButton();
		emojiPickerBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(emojiPickerBackgroundColorButton);
		emojiPickerBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.emojiPickerBackgroundColor=chooseHexColor(Colors.emojiPickerBackgroundColor);
				updateInputBoxes();
			}
		});
		
		emojiPickerBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		emojiPickerBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(emojiPickerBackgroundColorInput);
		emojiPickerBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		emojiPickerBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (emojiPickerBackgroundColorInput.getValue()!=null && ((String) emojiPickerBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) emojiPickerBackgroundColorInput.getValue(), 16);
				Colors.emojiPickerBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel loadingScreenBackgroundColorLabel=new JLabel("loadingScreenBackgroundColor");
		loadingScreenBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		loadingScreenBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(loadingScreenBackgroundColorLabel);
		
		JButton loadingScreenBackgroundColorButton=new JButton();
		loadingScreenBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(loadingScreenBackgroundColorButton);
		loadingScreenBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.loadingScreenBackgroundColor=chooseHexColor(Colors.loadingScreenBackgroundColor);
				updateInputBoxes();
			}
		});
		
		loadingScreenBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		loadingScreenBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(loadingScreenBackgroundColorInput);
		loadingScreenBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		loadingScreenBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (loadingScreenBackgroundColorInput.getValue()!=null && ((String) loadingScreenBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) loadingScreenBackgroundColorInput.getValue(), 16);
				Colors.loadingScreenBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel loadingScreenTextColorLabel=new JLabel("loadingScreenTextColor");
		loadingScreenTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		loadingScreenTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(loadingScreenTextColorLabel);
		
		JButton loadingScreenTextColorButton=new JButton();
		loadingScreenTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(loadingScreenTextColorButton);
		loadingScreenTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.loadingScreenTextColor=chooseHexColor(Colors.loadingScreenTextColor);
				updateInputBoxes();
			}
		});
		
		loadingScreenTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		loadingScreenTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(loadingScreenTextColorInput);
		loadingScreenTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		loadingScreenTextColorInput.addPropertyChangeListener("value", evt->{
			if (loadingScreenTextColorInput.getValue()!=null && ((String) loadingScreenTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) loadingScreenTextColorInput.getValue(), 16);
				Colors.loadingScreenTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel keyMapperBackgroundColorLabel=new JLabel("keyMapperBackgroundColor");
		keyMapperBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		keyMapperBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(keyMapperBackgroundColorLabel);
		
		JButton keyMapperBackgroundColorButton=new JButton();
		keyMapperBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(keyMapperBackgroundColorButton);
		keyMapperBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.keyMapperBackgroundColor=chooseHexColor(Colors.keyMapperBackgroundColor);
				updateInputBoxes();
			}
		});
		
		keyMapperBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		keyMapperBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(keyMapperBackgroundColorInput);
		keyMapperBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		keyMapperBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (keyMapperBackgroundColorInput.getValue()!=null && ((String) keyMapperBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) keyMapperBackgroundColorInput.getValue(), 16);
				Colors.keyMapperBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel keyMapperTextColorLabel=new JLabel("keyMapperTextColor");
		keyMapperTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		keyMapperTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(keyMapperTextColorLabel);
		
		JButton keyMapperTextColorButton=new JButton();
		keyMapperTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(keyMapperTextColorButton);
		keyMapperTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.keyMapperTextColor=chooseHexColor(Colors.keyMapperTextColor);
				updateInputBoxes();
			}
		});
		
		keyMapperTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		keyMapperTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(keyMapperTextColorInput);
		keyMapperTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		keyMapperTextColorInput.addPropertyChangeListener("value", evt->{
			if (keyMapperTextColorInput.getValue()!=null && ((String) keyMapperTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) keyMapperTextColorInput.getValue(), 16);
				Colors.keyMapperTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel imagePreviewBackgroundColorLabel=new JLabel("imagePreviewBackgroundColor");
		imagePreviewBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		imagePreviewBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(imagePreviewBackgroundColorLabel);
		
		JButton imagePreviewBackgroundColorButton=new JButton();
		imagePreviewBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(imagePreviewBackgroundColorButton);
		imagePreviewBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.imagePreviewBackgroundColor=chooseHexColor(Colors.imagePreviewBackgroundColor);
				updateInputBoxes();
			}
		});
		
		imagePreviewBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		imagePreviewBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(imagePreviewBackgroundColorInput);
		imagePreviewBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		imagePreviewBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (imagePreviewBackgroundColorInput.getValue()!=null && ((String) imagePreviewBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) imagePreviewBackgroundColorInput.getValue(), 16);
				Colors.imagePreviewBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel imagePreviewTextColorLabel=new JLabel("imagePreviewTextColor");
		imagePreviewTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		imagePreviewTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(imagePreviewTextColorLabel);
		
		JButton imagePreviewTextColorButton=new JButton();
		imagePreviewTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(imagePreviewTextColorButton);
		imagePreviewTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.imagePreviewTextColor=chooseHexColor(Colors.imagePreviewTextColor);
				updateInputBoxes();
			}
		});
		
		imagePreviewTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		imagePreviewTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(imagePreviewTextColorInput);
		imagePreviewTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		imagePreviewTextColorInput.addPropertyChangeListener("value", evt->{
			if (imagePreviewTextColorInput.getValue()!=null && ((String) imagePreviewTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) imagePreviewTextColorInput.getValue(), 16);
				Colors.imagePreviewTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel xTextHeader=new JLabel("Message text");
		xTextHeader.setBounds(LABEL_X, labelY, colourPaneWidth, HEADER_LABEL_HEIGHT);
		xTextHeader.setFont(xTextHeader.getFont().deriveFont(Font.BOLD));
		xTextHeader.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(xTextHeader);
		
		labelY+=HEADER_LABEL_HEIGHT;
		
		JLabel subtextColorLabel=new JLabel("subtextColor");
		subtextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		subtextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(subtextColorLabel);
		
		JButton subtextColorButton=new JButton();
		subtextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(subtextColorButton);
		subtextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.subtextColor=chooseHexColor(Colors.subtextColor);
				updateInputBoxes();
			}
		});
		
		subtextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		subtextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(subtextColorInput);
		subtextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		subtextColorInput.addPropertyChangeListener("value", evt->{
			if (subtextColorInput.getValue()!=null && ((String) subtextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) subtextColorInput.getValue(), 16);
				Colors.subtextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel monospaceTextBackgroundColorLabel=new JLabel("monospaceTextBackgroundColor");
		monospaceTextBackgroundColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		monospaceTextBackgroundColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(monospaceTextBackgroundColorLabel);
		
		JButton monospaceTextBackgroundColorButton=new JButton();
		monospaceTextBackgroundColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(monospaceTextBackgroundColorButton);
		monospaceTextBackgroundColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.monospaceTextBackgroundColor=chooseHexColor(Colors.monospaceTextBackgroundColor);
				updateInputBoxes();
			}
		});
		
		monospaceTextBackgroundColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		monospaceTextBackgroundColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(monospaceTextBackgroundColorInput);
		monospaceTextBackgroundColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		monospaceTextBackgroundColorInput.addPropertyChangeListener("value", evt->{
			if (monospaceTextBackgroundColorInput.getValue()!=null && ((String) monospaceTextBackgroundColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) monospaceTextBackgroundColorInput.getValue(), 16);
				Colors.monospaceTextBackgroundColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel forwardedTextColorLabel=new JLabel("forwardedTextColor");
		forwardedTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		forwardedTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(forwardedTextColorLabel);
		
		JButton forwardedTextColorButton=new JButton();
		forwardedTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(forwardedTextColorButton);
		forwardedTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.forwardedTextColor=chooseHexColor(Colors.forwardedTextColor);
				updateInputBoxes();
			}
		});
		
		forwardedTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		forwardedTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(forwardedTextColorInput);
		forwardedTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		forwardedTextColorInput.addPropertyChangeListener("value", evt->{
			if (forwardedTextColorInput.getValue()!=null && ((String) forwardedTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) forwardedTextColorInput.getValue(), 16);
				Colors.forwardedTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel editedTextColorLabel=new JLabel("editedTextColor");
		editedTextColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		editedTextColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(editedTextColorLabel);
		
		JButton editedTextColorButton=new JButton();
		editedTextColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(editedTextColorButton);
		editedTextColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.editedTextColor=chooseHexColor(Colors.editedTextColor);
				updateInputBoxes();
			}
		});
		
		editedTextColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		editedTextColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(editedTextColorInput);
		editedTextColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		editedTextColorInput.addPropertyChangeListener("value", evt->{
			if (editedTextColorInput.getValue()!=null && ((String) editedTextColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) editedTextColorInput.getValue(), 16);
				Colors.editedTextColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel scrollHeader=new JLabel("Scrollbar");
		scrollHeader.setBounds(LABEL_X, labelY, colourPaneWidth, HEADER_LABEL_HEIGHT);
		scrollHeader.setFont(scrollHeader.getFont().deriveFont(Font.BOLD));
		scrollHeader.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(scrollHeader);
		
		labelY+=HEADER_LABEL_HEIGHT;
		
		JLabel scrollbarColorLabel=new JLabel("scrollbarColor");
		scrollbarColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		scrollbarColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(scrollbarColorLabel);
		
		JButton scrollbarColorButton=new JButton();
		scrollbarColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(scrollbarColorButton);
		scrollbarColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.scrollbarColor=chooseHexColor(Colors.scrollbarColor);
				updateInputBoxes();
			}
		});
		
		scrollbarColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		scrollbarColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(scrollbarColorInput);
		scrollbarColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		scrollbarColorInput.addPropertyChangeListener("value", evt->{
			if (scrollbarColorInput.getValue()!=null && ((String) scrollbarColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) scrollbarColorInput.getValue(), 16);
				Colors.scrollbarColor=colorInt;
			}
		});
		
		labelY+=LABEL_HEIGHT;
		
		JLabel scrollbarHandleColorLabel=new JLabel("scrollbarHandleColor");
		scrollbarHandleColorLabel.setBounds(LABEL_X, labelY, colourPaneWidth, LABEL_HEIGHT);
		scrollbarHandleColorLabel.setHorizontalAlignment(JLabel.LEFT);
		colourPane.add(scrollbarHandleColorLabel);
		
		JButton scrollbarHandleColorButton=new JButton();
		scrollbarHandleColorButton.setBounds(buttonX,labelY+1,BUTTON_WIDTH,LABEL_HEIGHT-2);
		colourPane.add(scrollbarHandleColorButton);
		scrollbarHandleColorButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Colors.scrollbarHandleColor=chooseHexColor(Colors.scrollbarHandleColor);
				updateInputBoxes();
			}
		});
		
		scrollbarHandleColorInput=new JFormattedTextField(createFormatter("HHHHHH"));
		scrollbarHandleColorInput.setBounds(inputX, labelY+1, inputWidth, LABEL_HEIGHT-2);
		colourPane.add(scrollbarHandleColorInput);
		scrollbarHandleColorInput.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
		scrollbarHandleColorInput.addPropertyChangeListener("value", evt->{
			if (scrollbarHandleColorInput.getValue()!=null && ((String) scrollbarHandleColorInput.getValue()).length()==6) {
				int colorInt=Integer.parseInt((String) scrollbarHandleColorInput.getValue(), 16);
				Colors.scrollbarHandleColor=colorInt;
			}
		});
		
		colourPaneWidth=inputX+inputWidth+LABEL_RIGHT_PADDING;
		
		JScrollPane scrollPane=new JScrollPane(colourPane);
		scrollPane.setBounds(0, TOOLBAR_HEIGHT, colourPaneWidth, APP_HEIGHT-TOOLBAR_HEIGHT*2-5);
		scrollPane.getVerticalScrollBar().setUnitIncrement(LABEL_HEIGHT);
		
		contentPane.add(toolbar);
		contentPane.add(scrollPane);
	}

	protected void openBtn_mouseClicked(MouseEvent e) {
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileFilter() {
			
			public String getDescription() {
				return "JSON theme";
			}
			
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					String filename=f.getName().toLowerCase();
					return filename.endsWith(".json");
				}
			}
		});
		int result=fileChooser.showOpenDialog(fileChooser);
		if (result==JFileChooser.APPROVE_OPTION) {
			boolean confirmed=false;
			if (!savedBefore) {
				confirmed=(showQuestion("You have unsaved changes. Load new theme anyway?", "Unsaved File")==JOptionPane.YES_OPTION);
			} else {
				confirmed=true;
			}
			if (confirmed) {
				File selectedFile=fileChooser.getSelectedFile();
				String fileContent="";
				try {
					fileContent=new String(Files.readAllBytes(selectedFile.toPath()));
				} catch (IOException e1) {
					showExceptionMessage(e1.getLocalizedMessage());
				}
				JSONObject imported_palette=null;
				try {
					imported_palette=JSON.getObject(fileContent);
					importAllColors(imported_palette);
					updateInputBoxes();
				} catch (Exception e1) {
					showErrorMessage(String.format("Could not parse this file into a JSON object.\nReason: %s", e1), "JSON Exception");
				}
				savedBefore=true;
			}
		}
	}
	
	protected void saveBtn_mouseClicked(MouseEvent e) {
		JFileChooser fileChooser=new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(new FileFilter() {
			public String getDescription() {
				return "JSON theme";
			}
			
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					String filename=f.getName().toLowerCase();
					return filename.endsWith(".json");
				}
			}
		});
		int result=fileChooser.showSaveDialog(fileChooser);
		if (result==JFileChooser.APPROVE_OPTION) {
			String fileName=fileChooser.getSelectedFile().getAbsolutePath();
			if (!fileName.toLowerCase().endsWith(".json")) {
				fileName+=".json";
			}
			File selectedFile=new File(fileName);
			System.out.println(selectedFile);
			try {
				boolean confirmed=false;
				if (!selectedFile.createNewFile()) {
					confirmed=(showQuestion("Overwrite file?", "File Already Exists")==JOptionPane.YES_OPTION);
				} else {
					confirmed=true;
				}
				if (confirmed) {
					PrintWriter writer=new PrintWriter(selectedFile, "UTF-8");
					writer.print(exportColors());
					writer.close();
				}
			} catch (IOException e1) {
				showExceptionMessage(e1.getMessage());
			}
		}
	}
	
	protected static void updateInputBoxes() {
		channelViewBackgroundColorInput.setText(String.format("%06x", Colors.channelViewBackgroundColor));
		channelViewEmptyTextColorInput.setText(String.format("%06x", Colors.channelViewEmptyTextColor));
		timestampColorInput.setText(String.format("%06x", Colors.timestampColor));
		selectedTimestampColorInput.setText(String.format("%06x", Colors.selectedTimestampColor));
		linkColorInput.setText(String.format("%06x", Colors.linkColor));
		messageAuthorColorInput.setText(String.format("%06x", Colors.messageAuthorColor));
		messageContentColorInput.setText(String.format("%06x", Colors.messageContentColor));
		recipientMessageContentColorInput.setText(String.format("%06x", Colors.recipientMessageContentColor));
		statusMessageContentColorInput.setText(String.format("%06x", Colors.statusMessageContentColor));
		selectedMessageBackgroundColorInput.setText(String.format("%06x", Colors.selectedMessageBackgroundColor));
		selectedMessageAuthorColorInput.setText(String.format("%06x", Colors.selectedMessageAuthorColor));
		selectedMessageContentColorInput.setText(String.format("%06x", Colors.selectedMessageContentColor));
		selectedRecipientMessageContentColorInput.setText(String.format("%06x", Colors.selectedRecipientMessageContentColor));
		selectedStatusMessageContentColorInput.setText(String.format("%06x", Colors.selectedStatusMessageContentColor));
		embedBackgroundColorInput.setText(String.format("%06x", Colors.embedBackgroundColor));
		embedTitleColorInput.setText(String.format("%06x", Colors.embedTitleColor));
		embedDescriptionColorInput.setText(String.format("%06x", Colors.embedDescriptionColor));
		selectedEmbedBackgroundColorInput.setText(String.format("%06x", Colors.selectedEmbedBackgroundColor));
		selectedEmbedTitleColorInput.setText(String.format("%06x", Colors.selectedEmbedTitleColor));
		selectedEmbedDescriptionColorInput.setText(String.format("%06x", Colors.selectedEmbedDescriptionColor));
		buttonBackgroundColorInput.setText(String.format("%06x", Colors.buttonBackgroundColor));
		buttonTextColorInput.setText(String.format("%06x", Colors.buttonTextColor));
		selectedButtonBackgroundColorInput.setText(String.format("%06x", Colors.selectedButtonBackgroundColor));
		selectedButtonTextColorInput.setText(String.format("%06x", Colors.selectedButtonTextColor));
		bannerBackgroundColorInput.setText(String.format("%06x", Colors.bannerBackgroundColor));
		bannerTextColorInput.setText(String.format("%06x", Colors.bannerTextColor));
		outdatedBannerBackgroundColorInput.setText(String.format("%06x", Colors.outdatedBannerBackgroundColor));
		outdatedBannerTextColorInput.setText(String.format("%06x", Colors.outdatedBannerTextColor));
		typingBannerBackgroundColorInput.setText(String.format("%06x", Colors.typingBannerBackgroundColor));
		typingBannerTextColorInput.setText(String.format("%06x", Colors.typingBannerTextColor));
		unreadIndicatorBackgroundColorInput.setText(String.format("%06x", Colors.unreadIndicatorBackgroundColor));
		unreadIndicatorTextColorInput.setText(String.format("%06x", Colors.unreadIndicatorTextColor));
		recipientMessageConnectorColorInput.setText(String.format("%06x", Colors.recipientMessageConnectorColor));
		listBackgroundColorInput.setText(String.format("%06x", Colors.listBackgroundColor));
		listTextColorInput.setText(String.format("%06x", Colors.listTextColor));
		listMutedTextColorInput.setText(String.format("%06x", Colors.listMutedTextColor));
		listDescriptionTextColorInput.setText(String.format("%06x", Colors.listDescriptionTextColor));
		listSelectedBackgroundColorInput.setText(String.format("%06x", Colors.listSelectedBackgroundColor));
		listSelectedTextColorInput.setText(String.format("%06x", Colors.listSelectedTextColor));
		listSelectedMutedTextColorInput.setText(String.format("%06x", Colors.listSelectedMutedTextColor));
		listSelectedDescriptionTextColorInput.setText(String.format("%06x", Colors.listSelectedDescriptionTextColor));
		listNoItemsTextColorInput.setText(String.format("%06x", Colors.listNoItemsTextColor));
		listIndicatorColorInput.setText(String.format("%06x", Colors.listIndicatorColor));
		dialogBackgroundColorInput.setText(String.format("%06x", Colors.dialogBackgroundColor));
		dialogTextColorInput.setText(String.format("%06x", Colors.dialogTextColor));
		emojiPickerBackgroundColorInput.setText(String.format("%06x", Colors.emojiPickerBackgroundColor));
		loadingScreenBackgroundColorInput.setText(String.format("%06x", Colors.loadingScreenBackgroundColor));
		loadingScreenTextColorInput.setText(String.format("%06x", Colors.loadingScreenTextColor));
		keyMapperBackgroundColorInput.setText(String.format("%06x", Colors.keyMapperBackgroundColor));
		keyMapperTextColorInput.setText(String.format("%06x", Colors.keyMapperTextColor));
		imagePreviewBackgroundColorInput.setText(String.format("%06x", Colors.imagePreviewBackgroundColor));
		imagePreviewTextColorInput.setText(String.format("%06x", Colors.imagePreviewTextColor));
		subtextColorInput.setText(String.format("%06x", Colors.subtextColor));
		monospaceTextBackgroundColorInput.setText(String.format("%06x", Colors.monospaceTextBackgroundColor));
		forwardedTextColorInput.setText(String.format("%06x", Colors.forwardedTextColor));
		editedTextColorInput.setText(String.format("%06x", Colors.editedTextColor));
		scrollbarColorInput.setText(String.format("%06x", Colors.scrollbarColor));
		scrollbarHandleColorInput.setText(String.format("%06x", Colors.scrollbarHandleColor));
	}
	
	protected static void importAllColors(JSONObject palFile) {
		Colors.channelViewBackgroundColor=(int) Long.parseLong(palFile.getString("channelViewBackground"), 16);
		Colors.channelViewEmptyTextColor=(int) Long.parseLong(palFile.getString("channelViewEmptyText"), 16);
		Colors.timestampColor=(int) Long.parseLong(palFile.getString("timestamp"), 16);
		Colors.selectedTimestampColor=(int) Long.parseLong(palFile.getString("selectedTimestamp"), 16);
		Colors.linkColor=(int) Long.parseLong(palFile.getString("link"), 16);
		Colors.messageAuthorColor=(int) Long.parseLong(palFile.getString("messageAuthor"), 16);
		Colors.messageContentColor=(int) Long.parseLong(palFile.getString("messageContent"), 16);
		Colors.recipientMessageContentColor=(int) Long.parseLong(palFile.getString("recipientMessageContent"), 16);
		Colors.statusMessageContentColor=(int) Long.parseLong(palFile.getString("statusMessageContent"), 16);
		Colors.selectedMessageBackgroundColor=(int) Long.parseLong(palFile.getString("selectedMessageBackground"), 16);
		Colors.selectedMessageAuthorColor=(int) Long.parseLong(palFile.getString("selectedMessageAuthor"), 16);
		Colors.selectedMessageContentColor=(int) Long.parseLong(palFile.getString("selectedMessageContent"), 16);
		Colors.selectedRecipientMessageContentColor=(int) Long.parseLong(palFile.getString("selectedRecipientMessageContent"), 16);
		Colors.selectedStatusMessageContentColor=(int) Long.parseLong(palFile.getString("selectedStatusMessageContent"), 16);
		Colors.embedBackgroundColor=(int) Long.parseLong(palFile.getString("embedBackground"), 16);
		Colors.embedTitleColor=(int) Long.parseLong(palFile.getString("embedTitle"), 16);
		Colors.embedDescriptionColor=(int) Long.parseLong(palFile.getString("embedDescription"), 16);
		Colors.selectedEmbedBackgroundColor=(int) Long.parseLong(palFile.getString("selectedEmbedBackground"), 16);
		Colors.selectedEmbedTitleColor=(int) Long.parseLong(palFile.getString("selectedEmbedTitle"), 16);
		Colors.selectedEmbedDescriptionColor=(int) Long.parseLong(palFile.getString("selectedEmbedDescription"), 16);
		Colors.buttonBackgroundColor=(int) Long.parseLong(palFile.getString("buttonBackground"), 16);
		Colors.buttonTextColor=(int) Long.parseLong(palFile.getString("buttonText"), 16);
		Colors.selectedButtonBackgroundColor=(int) Long.parseLong(palFile.getString("selectedButtonBackground"), 16);
		Colors.selectedButtonTextColor=(int) Long.parseLong(palFile.getString("selectedButtonText"), 16);
		Colors.bannerBackgroundColor=(int) Long.parseLong(palFile.getString("bannerBackground"), 16);
		Colors.bannerTextColor=(int) Long.parseLong(palFile.getString("bannerText"), 16);
		Colors.outdatedBannerBackgroundColor=(int) Long.parseLong(palFile.getString("outdatedBannerBackground"), 16);
		Colors.outdatedBannerTextColor=(int) Long.parseLong(palFile.getString("outdatedBannerText"), 16);
		Colors.typingBannerBackgroundColor=(int) Long.parseLong(palFile.getString("typingBannerBackground"), 16);
		Colors.typingBannerTextColor=(int) Long.parseLong(palFile.getString("typingBannerText"), 16);
		Colors.unreadIndicatorBackgroundColor=(int) Long.parseLong(palFile.getString("unreadIndicatorBackground"), 16);
		Colors.unreadIndicatorTextColor=(int) Long.parseLong(palFile.getString("unreadIndicatorText"), 16);
		Colors.recipientMessageConnectorColor=(int) Long.parseLong(palFile.getString("recipientMessageConnector"), 16);
		Colors.listBackgroundColor=(int) Long.parseLong(palFile.getString("listBackground"), 16);
		Colors.listTextColor=(int) Long.parseLong(palFile.getString("listText"), 16);
		Colors.listMutedTextColor=(int) Long.parseLong(palFile.getString("listMutedText"), 16);
		Colors.listDescriptionTextColor=(int) Long.parseLong(palFile.getString("listDescriptionText"), 16);
		Colors.listSelectedBackgroundColor=(int) Long.parseLong(palFile.getString("listSelectedBackground"), 16);
		Colors.listSelectedTextColor=(int) Long.parseLong(palFile.getString("listSelectedText"), 16);
		Colors.listSelectedMutedTextColor=(int) Long.parseLong(palFile.getString("listSelectedMutedText"), 16);
		Colors.listSelectedDescriptionTextColor=(int) Long.parseLong(palFile.getString("listSelectedDescriptionText"), 16);
		Colors.listNoItemsTextColor=(int) Long.parseLong(palFile.getString("listNoItemsText"), 16);
		Colors.listIndicatorColor=(int) Long.parseLong(palFile.getString("listIndicator"), 16);
		Colors.dialogBackgroundColor=(int) Long.parseLong(palFile.getString("dialogBackground"), 16);
		Colors.dialogTextColor=(int) Long.parseLong(palFile.getString("dialogText"), 16);
		Colors.emojiPickerBackgroundColor=(int) Long.parseLong(palFile.getString("emojiPickerBackground"), 16);
		Colors.loadingScreenBackgroundColor=(int) Long.parseLong(palFile.getString("loadingScreenBackground"), 16);
		Colors.loadingScreenTextColor=(int) Long.parseLong(palFile.getString("loadingScreenText"), 16);
		Colors.keyMapperBackgroundColor=(int) Long.parseLong(palFile.getString("keyMapperBackground"), 16);
		Colors.keyMapperTextColor=(int) Long.parseLong(palFile.getString("keyMapperText"), 16);
		Colors.imagePreviewBackgroundColor=(int) Long.parseLong(palFile.getString("imagePreviewBackground"), 16);
		Colors.imagePreviewTextColor=(int) Long.parseLong(palFile.getString("imagePreviewText"), 16);
		Colors.subtextColor=(int) Long.parseLong(palFile.getString("subtext"), 16);
		Colors.monospaceTextBackgroundColor=(int) Long.parseLong(palFile.getString("monospaceTextBackground"), 16);
		Colors.forwardedTextColor=(int) Long.parseLong(palFile.getString("forwardedText"), 16);
		Colors.editedTextColor=(int) Long.parseLong(palFile.getString("editedText"), 16);
		Colors.scrollbarColor=(int) Long.parseLong(palFile.getString("scrollbar"), 16);
		Colors.scrollbarHandleColor=(int) Long.parseLong(palFile.getString("scrollbarHandle"), 16);
	}
	
	protected static JSONObject exportColors() {
		JSONObject palette=new JSONObject();
		palette.put("channelViewBackground", String.format("%06x", Colors.channelViewBackgroundColor));
		palette.put("channelViewEmptyText", String.format("%06x", Colors.channelViewEmptyTextColor));
		palette.put("timestamp", String.format("%06x", Colors.timestampColor));
		palette.put("selectedTimestamp", String.format("%06x", Colors.selectedTimestampColor));
		palette.put("link", String.format("%06x", Colors.linkColor));
		palette.put("messageAuthor", String.format("%06x", Colors.messageAuthorColor));
		palette.put("messageContent", String.format("%06x", Colors.messageContentColor));
		palette.put("recipientMessageContent", String.format("%06x", Colors.recipientMessageContentColor));
		palette.put("statusMessageContent", String.format("%06x", Colors.statusMessageContentColor));
		palette.put("selectedMessageBackground", String.format("%06x", Colors.selectedMessageBackgroundColor));
		palette.put("selectedMessageAuthor", String.format("%06x", Colors.selectedMessageAuthorColor));
		palette.put("selectedMessageContent", String.format("%06x", Colors.selectedMessageContentColor));
		palette.put("selectedRecipientMessageContent", String.format("%06x", Colors.selectedRecipientMessageContentColor));
		palette.put("selectedStatusMessageContent", String.format("%06x", Colors.selectedStatusMessageContentColor));
		palette.put("embedBackground", String.format("%06x", Colors.embedBackgroundColor));
		palette.put("embedTitle", String.format("%06x", Colors.embedTitleColor));
		palette.put("embedDescription", String.format("%06x", Colors.embedDescriptionColor));
		palette.put("selectedEmbedBackground", String.format("%06x", Colors.selectedEmbedBackgroundColor));
		palette.put("selectedEmbedTitle", String.format("%06x", Colors.selectedEmbedTitleColor));
		palette.put("selectedEmbedDescription", String.format("%06x", Colors.selectedEmbedDescriptionColor));
		palette.put("buttonBackground", String.format("%06x", Colors.buttonBackgroundColor));
		palette.put("buttonText", String.format("%06x", Colors.buttonTextColor));
		palette.put("selectedButtonBackground", String.format("%06x", Colors.selectedButtonBackgroundColor));
		palette.put("selectedButtonText", String.format("%06x", Colors.selectedButtonTextColor));
		palette.put("bannerBackground", String.format("%06x", Colors.bannerBackgroundColor));
		palette.put("bannerText", String.format("%06x", Colors.bannerTextColor));
		palette.put("outdatedBannerBackground", String.format("%06x", Colors.outdatedBannerBackgroundColor));
		palette.put("outdatedBannerText", String.format("%06x", Colors.outdatedBannerTextColor));
		palette.put("typingBannerBackground", String.format("%06x", Colors.typingBannerBackgroundColor));
		palette.put("typingBannerText", String.format("%06x", Colors.typingBannerTextColor));
		palette.put("unreadIndicatorBackground", String.format("%06x", Colors.unreadIndicatorBackgroundColor));
		palette.put("unreadIndicatorText", String.format("%06x", Colors.unreadIndicatorTextColor));
		palette.put("recipientMessageConnector", String.format("%06x", Colors.recipientMessageConnectorColor));
		palette.put("listBackground", String.format("%06x", Colors.listBackgroundColor));
		palette.put("listText", String.format("%06x", Colors.listTextColor));
		palette.put("listMutedText", String.format("%06x", Colors.listMutedTextColor));
		palette.put("listDescriptionText", String.format("%06x", Colors.listDescriptionTextColor));
		palette.put("listSelectedBackground", String.format("%06x", Colors.listSelectedBackgroundColor));
		palette.put("listSelectedText", String.format("%06x", Colors.listSelectedTextColor));
		palette.put("listSelectedMutedText", String.format("%06x", Colors.listSelectedMutedTextColor));
		palette.put("listSelectedDescriptionText", String.format("%06x", Colors.listSelectedDescriptionTextColor));
		palette.put("listNoItemsText", String.format("%06x", Colors.listNoItemsTextColor));
		palette.put("listIndicator", String.format("%06x", Colors.listIndicatorColor));
		palette.put("dialogBackground", String.format("%06x", Colors.dialogBackgroundColor));
		palette.put("dialogText", String.format("%06x", Colors.dialogTextColor));
		palette.put("emojiPickerBackground", String.format("%06x", Colors.emojiPickerBackgroundColor));
		palette.put("loadingScreenBackground", String.format("%06x", Colors.loadingScreenBackgroundColor));
		palette.put("loadingScreenText", String.format("%06x", Colors.loadingScreenTextColor));
		palette.put("keyMapperBackground", String.format("%06x", Colors.keyMapperBackgroundColor));
		palette.put("keyMapperText", String.format("%06x", Colors.keyMapperTextColor));
		palette.put("imagePreviewBackground", String.format("%06x", Colors.imagePreviewBackgroundColor));
		palette.put("imagePreviewText", String.format("%06x", Colors.imagePreviewTextColor));
		palette.put("subtext", String.format("%06x", Colors.subtextColor));
		palette.put("monospaceTextBackground", String.format("%06x", Colors.monospaceTextBackgroundColor));
		palette.put("forwardedText", String.format("%06x", Colors.forwardedTextColor));
		palette.put("editedText", String.format("%06x", Colors.editedTextColor));
		palette.put("scrollbar", String.format("%06x", Colors.scrollbarColor));
		palette.put("scrollbarHandle", String.format("%06x", Colors.scrollbarHandleColor));
		return palette;
	}
	
	public static int chooseHexColor(int initialColor) {
		Color initialColorConverted=new Color((initialColor&0xFF0000)>>16,(initialColor&0x00FF00)>>8,initialColor&0x0000FF);
		Color color=JColorChooser.showDialog(null, "Choose a color", initialColorConverted);
		if (color!=null) {
			return color.getBlue()+color.getGreen()*256+color.getRed()*65536;
		} else {
			return initialColor;
		}
	}
	
	public static void showExceptionMessage(String exception) {
		showErrorMessage(String.format("Unexpected error: %s", exception), "Error");
	}
	
	public static void showErrorMessage(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title,
			JOptionPane.ERROR_MESSAGE);
	}
	
	public static int showQuestion(String message, String title) {
		int dialogResult=JOptionPane.showConfirmDialog(null, message, title,
			JOptionPane.YES_NO_OPTION);
		return dialogResult;
	}
	
	protected MaskFormatter createFormatter(String s) {
		MaskFormatter formatter=null;
		try {
			formatter=new MaskFormatter(s);
			formatter.setPlaceholderCharacter((char) (0));
		} catch (java.text.ParseException exc) {
			System.err.println("Formatter Error: "+exc.getMessage());
			System.exit(-1);
		}
		return formatter;
	}
}