package net.sourceforge.jnhf.fceux.tracefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import net.sourceforge.jnhf.fceux.tracefile.IllegalTraceLineException;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogLine;
import net.sourceforge.jnhf.fceux.tracefile.TraceLogParser;

import org.junit.Test;

public class TraceLogParserTest
{
	@Test
	public void testParseLine1() throws IllegalTraceLineException
	{
		assertNull(TraceLogParser.parseLine("FCEUX 2.1.0a - Trace Log File"));
	}

	@Test
	public void testParseLine2() throws IllegalTraceLineException
	{
		final TraceLogLine line = TraceLogParser.parseLine("$FEB0:E8        INX                        A:01 X:00 Y:04 S:00 P:NvUbdizc");

		assertEquals("FEB0", line.getAddress());
		assertEquals("E8", line.getData());
		assertEquals("INX", line.getInstruction());
		assertEquals(null, line.getMemoryAddress());
		assertEquals(null, line.getMemoryValue());
		assertEquals("01", line.getValueA());
		assertEquals("00", line.getValueX());
		assertEquals("04", line.getValueY());
		assertEquals("NvUbdizc", line.getValueFlags());
	}

	@Test
	public void testParseLine3() throws IllegalTraceLineException
	{
		final TraceLogLine line = TraceLogParser.parseLine("$FEA6:86 90     STX $0090 = #$33           A:21 X:22 Y:23 S:00 P:nvUbdiZc");

		assertEquals("FEA6", line.getAddress());
		assertEquals("8690", line.getData());
		assertEquals("STX $0090", line.getInstruction());
		assertEquals("0090", line.getMemoryAddress());
		assertEquals("33", line.getMemoryValue());
		assertEquals("21", line.getValueA());
		assertEquals("22", line.getValueX());
		assertEquals("23", line.getValueY());
		assertEquals("nvUbdiZc", line.getValueFlags());
	}

	@Test
	public void testParseLine4() throws IllegalTraceLineException
	{
		final TraceLogLine line = TraceLogParser.parseLine("$FEAA:B5 80     LDA $80,X @ $0084 = #$01   A:02 X:04 Y:03 S:00 P:nvUbdizc");

		assertEquals("FEAA", line.getAddress());
		assertEquals("B580", line.getData());
		assertEquals("LDA $80,X", line.getInstruction());
		assertEquals("0084", line.getMemoryAddress());
		assertEquals("01", line.getMemoryValue());
		assertEquals("02", line.getValueA());
		assertEquals("04", line.getValueX());
		assertEquals("03", line.getValueY());
		assertEquals("nvUbdizc", line.getValueFlags());
	}

	@Test
	public void testParseLine5() throws IllegalTraceLineException
	{
		final TraceLogLine line = TraceLogParser.parseLine("$C006:A5 EA     LDA $00EA = #$00           A:03 X:04 Y:03 S:00 P:nvUbdIzc");

		assertEquals("C006", line.getAddress());
		assertEquals("A5EA", line.getData());
		assertEquals("LDA $00EA", line.getInstruction());
		assertEquals("00EA", line.getMemoryAddress());
		assertEquals("00", line.getMemoryValue());
		assertEquals("03", line.getValueA());
		assertEquals("04", line.getValueX());
		assertEquals("03", line.getValueY());
		assertEquals("nvUbdIzc", line.getValueFlags());
	}
}
