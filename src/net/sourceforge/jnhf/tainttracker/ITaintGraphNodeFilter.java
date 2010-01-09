package net.sourceforge.jnhf.tainttracker;

public interface ITaintGraphNodeFilter
{
	boolean matches(TaintGraphNode node);
}
