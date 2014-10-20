package org.philhosoft.experiments.libraries.jukito;

import com.google.inject.assistedinject.Assisted;

public interface AuthorFactory
{
    Author create(@Assisted("first") String firstName, @Assisted("last") String lastName, @Assisted Person agent);
}
