import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class FullScreenFrame extends JFrame
{
	private boolean m_bFullScreen;
	private boolean m_bF11Pressed;

	private final Dimension m_screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private Dimension m_previousWindowSize;
	private Point m_previousWindowLocation;

	public FullScreenFrame()
	{
		super("FullScreenFrame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addWindowFocusListener(new WindowFocusListener()
		{
			public void windowGainedFocus(WindowEvent e)
			{
			}

			public void windowLostFocus(WindowEvent e)
			{
				if (m_bFullScreen && !m_bF11Pressed)
				{
					// We lost focus because of Alt+Tab
					// Restore normal size
					toggleFullscreen();
				}
				m_bF11Pressed = false;
			}
		});

		addKeyListener(new KeyAdapter()
		{
			public final void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_F11)
				{
					m_bF11Pressed = true;
					// Will result in lost of focus, so above boolean will be always reset
					toggleFullscreen();
				}
			}
		});

		m_bFullScreen = false;
//		setLocation(100, 100);
		setSize(m_screenSize.width / 2, m_screenSize.height / 2);
//		setUndecorated(false);
		setVisible(true);
	}

	private final void toggleFullscreen()
	{
		m_bFullScreen = !m_bFullScreen;
		if (m_bFullScreen)
		{
			// Store the window state
			m_previousWindowSize = getSize();
			m_previousWindowLocation = getLocation();
			// Clean up window resources
			dispose();
			// Hide the window
			setVisible(false);
			// Get rid of title and borders
			setUndecorated(true);
			// Go to top-left corner
			setLocation(0, 0);
			// And use all the screen real estate (including task bar)
			setSize(m_screenSize);
			// Display it
			setVisible(true);
			// And ensure it will get keyboard input
			requestFocus();
		}
		else
		{
			// Clean up window resources
			dispose();
			// Hide the window
			setVisible(false);
			// Restore previous state
			setLocation(m_previousWindowLocation);
			setSize(m_previousWindowSize.width, m_previousWindowSize.height);
			// Restore title and borders
			setUndecorated(false);
			// For Windows: if switching with Alt+Tab, setVisible gives back focus to the
			// restored window, thus cancelling the task switching.
			// So in this case we tell the window isn't focusable.
			setFocusableWindowState(m_bF11Pressed);
			// Redisplay the window
			setVisible(true);
			// Restore normal behavior
			setFocusableWindowState(true);
			if (m_bF11Pressed)
			{
				// For Linux: restore focus
				requestFocus();
			}
		}
	}

	public static final void main(String[] args)
	{
		new FullScreenFrame();
	}
}
