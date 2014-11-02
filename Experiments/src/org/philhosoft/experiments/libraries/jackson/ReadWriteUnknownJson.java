package org.philhosoft.experiments.libraries.jackson;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.philhosoft.util.ResourceUtil;

/**
 * Reads an unknown (no class defined, but we know the expected content) Json file.
 * Explore it, and alter it, then serialize it back.
 * Attempt to morph Json objects.
 */
public class ReadWriteUnknownJson
{
	/**
	 * @param args
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public static void main(String[] args) throws JsonProcessingException, IOException
	{
		String classPath = ResourceUtil.getClassPath(new ReadWriteUnknownJson());
		String jsonPath = ResourceUtil.getBinaryPath() + classPath;

		File fileIn = new File(jsonPath, "100000680.json");
		File fileOut = new File(jsonPath, "100000680-C.json");

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode object = (ObjectNode) mapper.readTree(fileIn);

		JsonNode id = object.get("id");
		System.out.println(id);

		object.set("newField", id);

		mapper.writeValue(fileOut, object);

		// Other possibilities...

		ObjectNode email = mapper.createObjectNode();
		email.set("type", new TextNode("email"));
		ArrayNode emails = mapper.createArrayNode();
		emails.add("plhoste@somewhere.com").add("philho@somewhereelse.org");
		email.set("refs", emails);
		object.set("email", email);

		object.remove("@class");
		System.out.println(object);
	}
}
