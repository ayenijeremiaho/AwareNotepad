package Aware;


import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ayeni jeremiah
 */

    

/**
 * A font selection dialog.
 * <p>
 * Note: can take a long time to start up on systems with (literally) hundreds
 * of fonts. TODO change list to JList, add a SelectionChangedListener to
 * preview.
 * 
 * @author Ian Darwin
 * @version $Id: FontChooser.java,v 1.19 2004/03/20 20:44:56 ian Exp $
 */
public class Font extends JDialog {

  // Results:

  /** The font the user has chosen */
  protected java.awt.Font resultFont;

  /** The resulting font name */
  protected String resultName;

  /** The resulting font size */
  protected int resultSize;

  /** The resulting boldness */
  protected boolean isBold;

  /** The resulting italicness */
  protected boolean isItalic;

  // Working fields

  /** Display text */
  protected String displayText = "A-Ware";

  /** The list of Fonts */
  protected String fontList[];

  /** The font name chooser */
  protected List fontNameChoice;

  /** The font size chooser */
  protected List fontSizeChoice;

  /** The bold and italic choosers */
  Checkbox bold, italic;

  /** The list of font sizes */
  protected String fontSizes[] = { "8", "10", "11", "12", "14", "16", "18",
      "20", "24", "30", "36", "40", "48", "60", "72" };

  /** The index of the default size (e.g., 14 point == 4) */
  protected static final int DEFAULT_SIZE = 4;

  /**
   * The display area. Use a JLabel as the AWT label doesn't always honor
   * setFont() in a timely fashion :-)
   */
  protected JLabel previewArea;

  /**
   * Construct a FontChooser -- Sets title and gets array of fonts on the
   * system. Builds a GUI to let the user choose one font at one size.
   */
  public Font(Frame f) {
    super(f, "Font Chooser", true);

    Container cp = getContentPane();

    Panel top = new Panel();
    top.setLayout(new FlowLayout());

    fontNameChoice = new List(8);
    top.add(fontNameChoice);

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    // For JDK 1.1: returns about 10 names (Serif, SansSerif, etc.)
    // fontList = toolkit.getFontList();
    // For JDK 1.2: a much longer list; most of the names that come
    // with your OS (e.g., Arial), plus the Sun/Java ones (Lucida,
    // Lucida Bright, Lucida Sans...)
    fontList = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getAvailableFontFamilyNames();

    for (int i = 0; i < fontList.length; i++)
      fontNameChoice.add(fontList[i]);
    fontNameChoice.select(0);

    fontSizeChoice = new List(8);
    top.add(fontSizeChoice);

    for (int i = 0; i < fontSizes.length; i++)
      fontSizeChoice.add(fontSizes[i]);
    fontSizeChoice.select(DEFAULT_SIZE);

    cp.add(top, BorderLayout.NORTH);

    Panel attrs = new Panel();
    top.add(attrs);
    attrs.setLayout(new GridLayout(0, 1));
    attrs.add(bold = new Checkbox("Bold", false));
    attrs.add(italic = new Checkbox("Italic", false));

    previewArea = new JLabel(displayText, JLabel.CENTER);
    previewArea.setSize(200, 50);
    cp.add(previewArea, BorderLayout.CENTER);

    Panel bot = new Panel();

    JButton okButton = new JButton("Apply");
    bot.add(okButton);
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        previewFont();
        dispose();
        setVisible(false);
      }
    });

    JButton pvButton = new JButton("Preview");
    bot.add(pvButton);
    pvButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        previewFont();
      }
    });

    JButton canButton = new JButton("Cancel");
    bot.add(canButton);
    canButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // Set all values to null. Better: restore previous.
        resultFont = null;
        resultName = null;
        resultSize = 0;
        isBold = false;
        isItalic = false;

        dispose();
        setVisible(false);
      }
    });

    cp.add(bot, BorderLayout.SOUTH);

    previewFont(); // ensure view is up to date!

    pack();
    setLocation(100, 100);
  }

  /**
   * Called from the action handlers to get the font info, build a font, and
   * set it.
   */
  protected void previewFont() {
    resultName = fontNameChoice.getSelectedItem();
    String resultSizeName = fontSizeChoice.getSelectedItem();
    int resultSize = Integer.parseInt(resultSizeName);
    isBold = bold.getState();
    isItalic = italic.getState();
    int attrs = java.awt.Font.PLAIN;
    if (isBold)
      attrs = java.awt.Font.BOLD;
    if (isItalic)
      attrs |= java.awt.Font.ITALIC;
    resultFont = new java.awt.Font(resultName, attrs, resultSize);
    // System.out.println("resultName = " + resultName + "; " +
    //     "resultFont = " + resultFont);
    previewArea.setFont(resultFont);
    pack(); // ensure Dialog is big enough.
  }

  /** Retrieve the selected font name. */
  public String getSelectedName() {
    return resultName;
  }

  /** Retrieve the selected size */
  public int getSelectedSize() {
    return resultSize;
  }

  /** Retrieve the selected font, or null */
  public java.awt.Font getSelectedFont() {
    return resultFont;
  }

  /** Simple main program to start it running */
  public static void main(String[] args) {
    final JFrame f = new JFrame("FontChooser Startup");
    final Font fc = new Font(f);
    final Container cp = f.getContentPane();
    cp.setLayout(new GridLayout(0, 1)); // one vertical column

    JButton theButton = new JButton("Change font");
    cp.add(theButton);

    final JLabel theLabel = new JLabel("Java is great!", JLabel.CENTER);
    cp.add(theLabel);

    // Now that theButton and theLabel are ready, make the action listener
    theButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        fc.setVisible(true);
        java.awt.Font myNewFont = fc.getSelectedFont();
        System.out.println("You chose " + myNewFont);
        theLabel.setFont(myNewFont);
        f.pack(); // adjust for new size
        fc.dispose();
      }
    });

    f.setSize(150, 100);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}



//to call it we say jfontchoose.showdialog(textarea);
//textarea.setfont(jfont.getselectedfont());
