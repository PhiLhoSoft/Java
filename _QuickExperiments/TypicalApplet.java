//~ import netscape.javascript.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

public class TypicalApplet extends Applet
{
	private boolean m_bIsStandalone;
	private Graphics m_graphics;
	private Rectangle m_bounds;

	private TextField m_input;
	private Choice m_combo;
	private Button m_button;
	private Label m_message;

	private final int ELEMENT_SIZE = 10;
	private final int MAX_ELEMENT_SIZE = 100;
	private int m_elementSize = ELEMENT_SIZE;
	private Color m_elementColor = Color.BLACK;

	// For running as plain application
	public static void main(String args[])
	{
		final TypicalApplet applet = new TypicalApplet();
		applet.SetStandalone(true);

		Frame frame = new Frame();
		frame.setTitle("Typical Applet");
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				// JDK 1.1.x style frame exit
				System.exit(0);
			}
		});
		frame.add(applet, BorderLayout.CENTER);

		applet.init();

		frame.setSize(640, 480);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(
				(d.width - frame.getSize().width) / 2,
				(d.height - frame.getSize().height) / 2);
		frame.setVisible(true);

		applet.start();
	}

	// Applet is loaded
	public void init()
	{
		// Create GUI
		setBackground(Color.WHITE);

		m_input = new TextField(5);
		add(new Label("Value:"));
		add(m_input);
		m_input.setText("" + ELEMENT_SIZE);

		m_combo = new Choice();
		m_combo.addItem("Black");
		m_combo.addItem("Red");
		m_combo.addItem("Green");
		m_combo.addItem("Blue");
		add(new Label("Color:"));
		add(m_combo);

		m_button = new Button("Run");
		m_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				ShowStuff();
			}
		});
		add(m_button);

		m_message = new Label("                                    ");
		add(m_message);
	}

	// Applet should start
	public void start()
	{
		// Show GUI
		m_graphics = getGraphics();
		m_bounds = getBounds();
		System.out.println(m_graphics);
		System.out.println(m_bounds);
	}

	// Applet should stop its execution
	public void stop()
	{
	}

	// Applet should destroy any resources that it has allocated
	public void destroy()
	{
	}

	// Information about this applet.
	// Shown to public with applet info
	public String getAppletInfo()
	{
		return "Typical Applet by PhiLho version 1.0";
	}

	// Information about the parameters that are understood by this applet.
	// Shown to public with applet info
	public String[][] getParameterInfo()
	{
		String[][] info =
		{
			{ "value", "integer",      "Explanation" },
			{ "image", "URL of image", "Another explanation" }
		};
		return info;
	}

	public void paint(Graphics g)
	{
		g.setColor(Color.ORANGE);
		g.fillRoundRect(m_bounds.x + m_elementSize, m_bounds.y + m_elementSize,
				m_bounds.width - 2 * m_elementSize,
				m_bounds.height - 2 * m_elementSize,
				m_elementSize, m_elementSize
		);
	}

	public void update(Graphics g)
	{
		paint(g);
	}

	// Returns the value of the named parameter in the HTML tag
	public String GetParameter(String key, String def)
	{
		return m_bIsStandalone ?
				System.getProperty(key, def) :
				(getParameter(key) != null ? getParameter(key) : def);
	}

	private void SetStandalone(boolean b)
	{
		m_bIsStandalone = b;
	}

	private void ShowStuff()
	{
		String arg = m_combo.getSelectedItem();
		if (arg.equals("Black")) m_elementColor = Color.BLACK;
		else if (arg.equals("Red"))  m_elementColor = Color.RED;
		else if (arg.equals("Green"))  m_elementColor = Color.GREEN;
		else if (arg.equals("Blue"))  m_elementColor = Color.BLUE;

		m_elementSize = Integer.parseInt(m_input.getText());
		if (m_elementSize < 1 || m_elementSize > MAX_ELEMENT_SIZE)
		{
			m_elementSize = ELEMENT_SIZE;
			m_input.setText("" + m_elementSize);
		}
		int elementNb = m_bounds.width * m_bounds.height / m_elementSize / m_elementSize;

		long timeStart = System.currentTimeMillis();
		InitDisplay();
		m_message.setText("Running");
		for (int i = 0; i < elementNb; i++)
		{
			Draw();
		}
		long timeEnd = System.currentTimeMillis();
		m_message.setText("Elts: " + elementNb + " Time: " + (timeEnd - timeStart));
	}

	private void InitDisplay()
	{
		m_graphics.setColor(Color.YELLOW);
		m_graphics.fillRect(m_bounds.x, m_bounds.y, m_bounds.width, m_bounds.height);
	}

	private void Draw()
	{
		m_graphics.setColor(m_elementColor);
		m_graphics.drawOval(
				GetRandomPos(m_bounds.x, m_bounds.width),
				GetRandomPos(m_bounds.y, m_bounds.height),
				m_elementSize, m_elementSize);
	}

	private int GetRandomPos(int min, int max)
	{
		return (int) (min + (max - min - m_elementSize) * Math.random());
	}

	/*
	private void ToJavaScript()
	{
		// Unrelated demo code
		JSObject window = JSObject.getWindow(this);
		JSObject document = (JSObject) win.getMember("document");

		window = JSObject.getWindow(this);
		document = (JSObject) window.getMember("document");

		JSObject loc = (JSObject) document.getMember("location");

		String s = (String) loc.getMember("href");  // document.location.href
		win.call("f", null);             // Call f() in HTML page
	}
*/
}
