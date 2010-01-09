package net.sourceforge.jnhf.reil;

public class StandardEnvironment {

    private int nextVariable = 0;

    public int generateNextVariable() {
	return nextVariable++;
    }

    public OperandSize getArchitectureSize() {
	return OperandSize.WORD;
    }

    public int getNextVariable() {
	return nextVariable;
    }

    public String getNextVariableString() {
    	return "t" + generateNextVariable();
//	return String.format("t%d", generateNextVariable());
    }

    public void nextInstruction() {
	nextVariable = 0;
    }

    public void setNextVariable(final int nextVariable) {
	this.nextVariable = nextVariable;
    }
}
