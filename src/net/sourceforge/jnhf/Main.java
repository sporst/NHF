package net.sourceforge.jnhf;

import java.io.File;
import java.io.IOException;

import net.sourceforge.jnhf.romfile.INesRomParser;
import net.sourceforge.jnhf.romfile.INesSplitter;
import net.sourceforge.jnhf.romfile.InvalidRomException;

public class Main
{
	public static void main(final String[] args) throws IOException, InvalidRomException
	{
		INesSplitter.split(INesRomParser.parse(new File("F:\\fceux\\Super Mario Bros. 3 (E).nes")), new File("F:\\fceux"));

//		final INesRom rom = INesRomParser.parse(new File("F:\\fceux\\Gargoyle's Quest II - The Demon Darkness (E).nes"));
//
////		INesSplitter.split(rom, new File("F:\\fceux\\"));
//
//		final List<FunctionCall> functionCalls = FunctionCallFinder.findFunctionCalls(rom.getPRGRoms().get(3).getData());
//
//		Collections.sort(functionCalls, new Comparator<FunctionCall>() {
//
//			@Override
//			public int compare(final FunctionCall o1, final FunctionCall o2)
//			{
//				return o1.getTarget() - o2.getTarget();
//			} });
//
//		for (final FunctionCall functionCall : functionCalls)
//		{
//			System.out.printf("%04X\n", functionCall.getTarget());
//		}
//
	}
}
