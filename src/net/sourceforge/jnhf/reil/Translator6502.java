package net.sourceforge.jnhf.reil;

import java.util.List;

import net.sourceforge.jnhf.disassembler.Instruction;
import net.sourceforge.jnhf.reil.translators.AdcTranslator;
import net.sourceforge.jnhf.reil.translators.AndTranslator;
import net.sourceforge.jnhf.reil.translators.AslTranslator;
import net.sourceforge.jnhf.reil.translators.BccTranslator;
import net.sourceforge.jnhf.reil.translators.BcsTranslator;
import net.sourceforge.jnhf.reil.translators.BeqTranslator;
import net.sourceforge.jnhf.reil.translators.BitTranslator;
import net.sourceforge.jnhf.reil.translators.BmiTranslator;
import net.sourceforge.jnhf.reil.translators.BneTranslator;
import net.sourceforge.jnhf.reil.translators.BplTranslator;
import net.sourceforge.jnhf.reil.translators.BvcTranslator;
import net.sourceforge.jnhf.reil.translators.BvsTranslator;
import net.sourceforge.jnhf.reil.translators.ClcTranslator;
import net.sourceforge.jnhf.reil.translators.CldTranslator;
import net.sourceforge.jnhf.reil.translators.CliTranslator;
import net.sourceforge.jnhf.reil.translators.ClvTranslator;
import net.sourceforge.jnhf.reil.translators.CmpTranslator;
import net.sourceforge.jnhf.reil.translators.CpxTranslator;
import net.sourceforge.jnhf.reil.translators.CpyTranslator;
import net.sourceforge.jnhf.reil.translators.DecTranslator;
import net.sourceforge.jnhf.reil.translators.DexTranslator;
import net.sourceforge.jnhf.reil.translators.DeyTranslator;
import net.sourceforge.jnhf.reil.translators.EorTranslator;
import net.sourceforge.jnhf.reil.translators.IncTranslator;
import net.sourceforge.jnhf.reil.translators.InxTranslator;
import net.sourceforge.jnhf.reil.translators.InyTranslator;
import net.sourceforge.jnhf.reil.translators.JmpTranslator;
import net.sourceforge.jnhf.reil.translators.JsrTranslator;
import net.sourceforge.jnhf.reil.translators.LdaTranslator;
import net.sourceforge.jnhf.reil.translators.LdxTranslator;
import net.sourceforge.jnhf.reil.translators.LdyTranslator;
import net.sourceforge.jnhf.reil.translators.LsrTranslator;
import net.sourceforge.jnhf.reil.translators.NopTranslator;
import net.sourceforge.jnhf.reil.translators.OraTranslator;
import net.sourceforge.jnhf.reil.translators.PhaTranslator;
import net.sourceforge.jnhf.reil.translators.PhpTranslator;
import net.sourceforge.jnhf.reil.translators.PlaTranslator;
import net.sourceforge.jnhf.reil.translators.PlpTranslator;
import net.sourceforge.jnhf.reil.translators.RolTranslator;
import net.sourceforge.jnhf.reil.translators.RorTranslator;
import net.sourceforge.jnhf.reil.translators.RtiTranslator;
import net.sourceforge.jnhf.reil.translators.RtsTranslator;
import net.sourceforge.jnhf.reil.translators.SbcTranslator;
import net.sourceforge.jnhf.reil.translators.SecTranslator;
import net.sourceforge.jnhf.reil.translators.SedTranslator;
import net.sourceforge.jnhf.reil.translators.SeiTranslator;
import net.sourceforge.jnhf.reil.translators.StaTranslator;
import net.sourceforge.jnhf.reil.translators.StxTranslator;
import net.sourceforge.jnhf.reil.translators.StyTranslator;
import net.sourceforge.jnhf.reil.translators.TaxTranslator;
import net.sourceforge.jnhf.reil.translators.TayTranslator;
import net.sourceforge.jnhf.reil.translators.TsxTranslator;
import net.sourceforge.jnhf.reil.translators.TxaTranslator;
import net.sourceforge.jnhf.reil.translators.TxsTranslator;
import net.sourceforge.jnhf.reil.translators.TyaTranslator;

public class Translator6502
{
	public static List<ReilInstruction> translate(final StandardEnvironment environment, final Instruction instruction) throws InternalTranslationException
	{
		if (instruction.getMnemonic().equalsIgnoreCase("adc"))
		{
			return AdcTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("and"))
		{
			return AndTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("asl"))
		{
			return AslTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("bcc"))
		{
			return BccTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("bcs"))
		{
			return BcsTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("beq"))
		{
			return BeqTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("bit"))
		{
			return BitTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("bmi"))
		{
			return BmiTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("bne"))
		{
			return BneTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("bpl"))
		{
			return BplTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("bvc"))
		{
			return BvcTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("bvs"))
		{
			return BvsTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("clc"))
		{
			return ClcTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("cld"))
		{
			return CldTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("cli"))
		{
			return CliTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("clv"))
		{
			return ClvTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("cmp"))
		{
			return CmpTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("cpx"))
		{
			return CpxTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("cpy"))
		{
			return CpyTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("dec"))
		{
			return DecTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("dex"))
		{
			return DexTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("dey"))
		{
			return DeyTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("eor"))
		{
			return EorTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("inc"))
		{
			return IncTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("inx"))
		{
			return InxTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("iny"))
		{
			return InyTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("jmp"))
		{
			return JmpTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("jsr"))
		{
			return JsrTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("lda"))
		{
			return LdaTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("ldx"))
		{
			return LdxTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("ldy"))
		{
			return LdyTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("lsr"))
		{
			return LsrTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("nop"))
		{
			return NopTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("ora"))
		{
			return OraTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("pha"))
		{
			return PhaTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("php"))
		{
			return PhpTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("pla"))
		{
			return PlaTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("plp"))
		{
			return PlpTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("rol"))
		{
			return RolTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("ror"))
		{
			return RorTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("rti"))
		{
			return RtiTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("rts"))
		{
			return RtsTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("sbc"))
		{
			return SbcTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("sec"))
		{
			return SecTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("sed"))
		{
			return SedTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("sei"))
		{
			return SeiTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("sta"))
		{
			return StaTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("stx"))
		{
			return StxTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("sty"))
		{
			return StyTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("tax"))
		{
			return TaxTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("tay"))
		{
			return TayTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("tsx"))
		{
			return TsxTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("txa"))
		{
			return TxaTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("txs"))
		{
			return TxsTranslator.translate(environment, instruction);
		}
		else if (instruction.getMnemonic().equalsIgnoreCase("tya"))
		{
			return TyaTranslator.translate(environment, instruction);
		}
		else
		{
			throw new InternalTranslationException(String.format("Unknown mnemonic %s", instruction.getMnemonic()));
		}
	}
}
