import java.util.*;

public class MapObjectFactory implements ObjectFactory
{
  public Object makeObject()
  {
    return new HashMap();
  }
}
