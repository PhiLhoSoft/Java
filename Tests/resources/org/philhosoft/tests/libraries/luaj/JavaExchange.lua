-- Don't show this on each eval
if rep ~= nil then print"Exchanging stuff with Java" end

-- Call a Lua function created from Java
function callRep()
	print(rep(5, "Brr"))
	print(rep(4))
	print(rep("Ni!"))
	print(rep(3, "Mmm", 'discard'))
end

function callJavaFun()
	print(javaFun("String"))
	print(javaFun(42))
	print(javaFun(true))
	print(javaFun(1, "Two", false, rep, { 5 }))
end

function calledByJava(param)
	if type(param) == 'string' then
		return param:gsub("Java", "Lua")
	end
end

if rep ~= nil then
	callRep()
	callJavaFun()
	return "Done!"
end

-- javaParam should be set by the Java environment
return calledByJava(javaParam)
