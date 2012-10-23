-- Sample luaj program that presents an animated Swing window.
--
-- This basic application handles key, and mouse input, has a basic animation loop,
-- and renders double-buffered graphics including the logo image in a swing frame.
--
-- PhiLho (2012-10-22): some changes, adapting to my coding style,
-- declaring the LuaJava interface variables,
-- changing the string delimiters on Java objects to get different syntax highlighting...

print"SwingTest"

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
local BufferedImage = import 'java.awt.image.BufferedImage'
local SwingUtilities = import 'javax.swing.SwingUtilities'
local Thread = import 'java.lang.Thread'
local ImageIO = import 'javax.imageio.ImageIO'
local File = import 'java.io.File'

-- Set up frame, get content pane
local width, height = 640, 480
local frame = newC (JFrame, "Sample Luaj Swing Application")
local content = frame:getContentPane()

-- Add a buffered image as content
local image = newC (BufferedImage, width, height, BufferedImage.TYPE_INT_RGB)
local icon = new ('javax.swing.ImageIcon', image)
local label = new ('javax.swing.JLabel', icon)

-- Add the main pane to the main content
content:add(label, BorderLayout.CENTER)
frame:setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
frame:pack()


-- Try loading the logo image
local getCurrentPath = function ()
	local ok, currentThread = pcall(Thread.currentThread, Thread)
	if ok then
		local classLoader = currentThread:getContextClassLoader()
		local path = classLoader:getResource(""):getPath()
		local classPath = newC (File, path)
		local currentPath = classPath:getPath() .. "/"
		print("Current path: " .. currentPath)
		return currentPath
	end
end

local loadImage = function (path)
	local s, i = pcall(ImageIO.read, ImageIO, newC (File, path))
	return s and i
end
local imageFile = "PhiLhoLogo.png"
local imagePath = getCurrentPath() .. imageFile
local classPath = PATH or ""
print(classPath)
local logo = loadImage(classPath .. imageFile) or loadImage(imagePath)
local lw, lh = 0, 0
if logo ~= nil then
	lw, lh = logo:getWidth(), logo:getHeight()
end


-- Simple animation framework
local tick, animate, render
local nextTime = 0
tick = newA ('java.lang.Runnable',
{
	run = function ()
		-- Call itself again
		SwingUtilities:invokeLater(tick)
		-- Sleep a bit until it is time to animate again
		if os.time() < nextTime then return Thread:sleep(5) end
		-- Time to animate! Compute the next time
		nextTime = math.max(os.time(), nextTime + 1000 / 60)
		pcall(animate)
		pcall(render)
		label:repaint(0, 0, 0, width, height)
	end
})

-- The animation step moves the line endpoints
local x1, y1, x2, y2, xi, yi = 160, 240, 480, 240, 0, 0
local vx1, vy1, vx2, vy2, vxi, vyi = -5, -6, 7, 8, 3, 1
local advance = function (x, vx, max, rnd)
	x = x + vx
	if x < 0 then
		return 0, math.random(2, 10)
	elseif x > max then
		return max, math.random(-10, -2)
	end
	return x, vx
end
animate = function ()
	x1, y1, x2, y2 = x1+1, y1+1, x2-1, y2-1
	x1, vx1 = advance(x1, vx1, width)
	y1, vy1 = advance(y1, vy1, height)
	x2, vx2 = advance(x2, vx2, width)
	y2, vy2 = advance(y2, vy2, height)
	xi, vxi = advance(xi, vxi, width  - lw)
	yi, vyi = advance(yi, vyi, height  - lh)
end

-- The render step draws the scene
local g = image:getGraphics()
-- Background transparency to add a trailing effect to the animation
local bg = new ('java.awt.Color', 0x22112244, true)
local fg = new ('java.awt.Color', 0xFFAA33)

render = function ()
	g:setColor(bg)
	g:fillRect(0, 0, width, height)
	if logo ~= nil then g:drawImage(logo, xi, yi) end
	g:setColor(fg)
	g:drawLine(x1, y1, x2, y2)
end

-- Add mouse listeners for specific mouse events
label:addMouseListener(newA ('java.awt.event.MouseListener',
{
	mousePressed = function (e)
		--print('mousePressed', e:getX(), e:getY(), e)
		x1, y1 = e:getX(), e:getY()
	end,
	-- mouseClicked = function (e) end,
	-- mouseEntered = function (e) end,
	-- mouseExited = function (e) end,
	-- mouseReleased = function (e) end,
}))
label:addMouseMotionListener(newA ('java.awt.event.MouseMotionListener',
{
	mouseDragged = function (e)
		--print('mouseDragged', e:getX(), e:getY(), e)
		x2, y2 = e:getX(), e:getY()
	end,
	-- mouseMoved= function (e) end,
}))

-- Add key listeners
frame:addKeyListener(newA ('java.awt.event.KeyListener',
{
	keyPressed = function (e)
		local id, code, char, text = e:getID(), e:getKeyCode(), e:getKeyChar(), e:getKeyText(e:getKeyCode())
		print("key id, code, char, text", id, code, char, text)
	end,
	keyReleased = function (e)
		-- Protects the call because it fails for modifier keys or special keys like direction keys
		print("pcall(string.char, char)", pcall(string.char, e:getKeyChar()))
	end,
	-- keyTyped = function (e) end,
}))

-- Use the window listener to kick off animation
frame:addWindowListener(newA ('java.awt.event.WindowListener',
{
	windowOpened = function (e)
		SwingUtilities:invokeLater(tick)
	end,
	-- windowActivated = function (e) end,
	-- windowClosed = function (e) end,
	-- windowClosing = function (e) end,
	-- windowDeactivated = function (e) end,
	-- windowDeiconified = function (e) end,
	-- windowIconified = function (e) end,
}))

-- Set window visible last to start application
frame:setVisible(true)

return "Launched"
