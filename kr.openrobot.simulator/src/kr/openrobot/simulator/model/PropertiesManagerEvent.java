package kr.openrobot.simulator.model;

import java.util.EventObject;

public class PropertiesManagerEvent extends EventObject 
{
   private static final long serialVersionUID = 3697053173951102953L;

   private final IPropertiesItem[] added;
   private final IPropertiesItem[] removed;

   public PropertiesManagerEvent(
      PropertiesManager source,
      IPropertiesItem[] itemsAdded, IPropertiesItem[] itemsRemoved
   ) {
      super(source);
      added = itemsAdded;
      removed = itemsRemoved;
   }

   public IPropertiesItem[] getItemsAdded() {
      return added;
   }

   public IPropertiesItem[] getItemsRemoved() {
      return removed;
   }
}