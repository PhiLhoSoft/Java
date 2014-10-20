-- To be called from a Java class

function calledByJava(param)
	if type(param) == 'string' then
		return param:gsub("Java", "Lua")
	else
		return "Incorrect parameter"
	end
end

-- Call back functions

function onTalk(javaObj)
	print(type(javaObj) .. " " .. tostring(javaObj))
	print(javaObj.name)
	javaObj:talk()
	return true
end

function onWalk(javaObj)
	javaObj:walk()
	return 1, "km"
end

if javaParam == nil then
	-- Various kinds of Lua types
	return 'a', 1, false, nil, {}, { 'x' }, { x = 'x' }, calledByJava, foo
end

-- javaParam should be set by the Java environment
return calledByJava(javaParam)
