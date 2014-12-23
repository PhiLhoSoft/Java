package org.philhosoft.formattedtext.format;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class TestContextWithStringBuilder
{
	@Test
	public void testStack() throws Exception
	{
		ContextWithStringBuilder context = new ContextWithStringBuilder();

		context.push("", true, true);
		context.push("", false, true);
		context.push("", true, false);
		context.push("", false, false);

		assertThat(context.isFirst()).isFalse();
		assertThat(context.isLast()).isFalse();

		context.pop();

		assertThat(context.isFirst()).isTrue();
		assertThat(context.isLast()).isFalse();

		context.pop();

		assertThat(context.isFirst()).isFalse();
		assertThat(context.isLast()).isTrue();

		context.pop();

		assertThat(context.isFirst()).isTrue();
		assertThat(context.isLast()).isTrue();

		context.pop();

		assertThat(context.isFirst()).isFalse();
		assertThat(context.isLast()).isFalse();
	}

	@Test
	public void testUpdate() throws Exception
	{
		ContextWithStringBuilder context = new ContextWithStringBuilder();

		context.push("", true, true);
		context.push("", false, false);

		assertThat(context.isFirst()).isFalse();
		assertThat(context.isLast()).isFalse();

		context.setFirstLast("", true, false);

		assertThat(context.isFirst()).isTrue();
		assertThat(context.isLast()).isFalse();

		context.pop();

		assertThat(context.isFirst()).isTrue();
		assertThat(context.isLast()).isTrue();

		context.pop();

		assertThat(context.isFirst()).isFalse();
		assertThat(context.isLast()).isFalse();
	}
}
