-- Another Luaj Swing test

print"SwingTestToo"

-- LuaJava interface (see http://www.keplerproject.org/luajava/manual.html#luareference reference)

-- import and static import: create a Java class
local import = luajava.bindClass
-- As the new keyword: create a new class instance out of a fully qualified class name
local new = luajava.newInstance
-- new class: create a new class instance out of a Java class imported with 'import'
local newC = luajava.new
-- new anonymous class: create a new class instance out of a fully qualified interface name (or more)
local newA = luajava.createProxy

-- Bind to classes we need
local BorderLayout = import 'java.awt.BorderLayout'
local JFrame = import 'javax.swing.JFrame'
local JButton = import 'javax.swing.JButton'
local SwingUtilities = import 'javax.swing.SwingUtilities'
local Thread = import 'java.lang.Thread'
local File = import 'java.io.File'

-- Set up frame, get content pane
local width, height = 640, 480
local frame = new ('javax.swing.JFrame', "Another Sample Luaj Swing Application")
local content = frame:getContentPane()


-- Add the main pane to the main content
local label = new ('javax.swing.JLabel', "Test Playground")
content:add(label, BorderLayout.NORTH)
local button = newC (JButton, "Click Me!")
content:add(button, BorderLayout.CENTER)
frame:setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
frame:pack()

-- Add mouse listeners for specific mouse events
button:addActionListener(newA ('java.awt.event.ActionListener',
{
	actionPerformed = function (e)
		print('Action', e)
	end,
}))

-- Set window visible last to start app.
frame:setVisible(true)
