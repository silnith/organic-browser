package org.silnith.grammar.xml;

import static org.silnith.grammar.UnicodeTerminalSymbols.capitalA;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalB;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalC;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalD;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalE;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalF;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalG;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalH;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalI;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalJ;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalK;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalL;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalM;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalN;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalO;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalP;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalQ;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalR;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalS;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalT;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalU;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalV;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalW;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalX;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalY;
import static org.silnith.grammar.UnicodeTerminalSymbols.capitalZ;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallA;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallB;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallC;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallD;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallE;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallF;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallG;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallH;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallI;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallJ;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallK;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallL;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallM;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallN;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallO;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallP;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallQ;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallR;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallS;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallT;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallU;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallV;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallW;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallX;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallY;
import static org.silnith.grammar.UnicodeTerminalSymbols.smallZ;

import org.silnith.grammar.Grammar;
import org.silnith.grammar.NonTerminalSymbol;
import org.silnith.grammar.UnicodeTerminalSymbols;


/**
 * [85] BaseChar ::= [#x0041-#x005A] | [#x0061-#x007A] | [#x00C0-#x00D6] |
 * [#x00D8-#x00F6] | [#x00F8-#x00FF] | [#x0100-#x0131] | [#x0134-#x013E] |
 * [#x0141-#x0148] | [#x014A-#x017E] | [#x0180-#x01C3] | [#x01CD-#x01F0] |
 * [#x01F4-#x01F5] | [#x01FA-#x0217] | [#x0250-#x02A8] | [#x02BB-#x02C1] |
 * #x0386 | [#x0388-#x038A] | #x038C | [#x038E-#x03A1] | [#x03A3-#x03CE] |
 * [#x03D0-#x03D6] | #x03DA | #x03DC | #x03DE | #x03E0 | [#x03E2-#x03F3] |
 * [#x0401-#x040C] | [#x040E-#x044F] | [#x0451-#x045C] | [#x045E-#x0481] |
 * [#x0490-#x04C4] | [#x04C7-#x04C8] | [#x04CB-#x04CC] | [#x04D0-#x04EB] |
 * [#x04EE-#x04F5] | [#x04F8-#x04F9] | [#x0531-#x0556] | #x0559 |
 * [#x0561-#x0586] | [#x05D0-#x05EA] | [#x05F0-#x05F2] | [#x0621-#x063A] |
 * [#x0641-#x064A] | [#x0671-#x06B7] | [#x06BA-#x06BE] | [#x06C0-#x06CE] |
 * [#x06D0-#x06D3] | #x06D5 | [#x06E5-#x06E6] | [#x0905-#x0939] | #x093D |
 * [#x0958-#x0961] | [#x0985-#x098C] | [#x098F-#x0990] | [#x0993-#x09A8] |
 * [#x09AA-#x09B0] | #x09B2 | [#x09B6-#x09B9] | [#x09DC-#x09DD] |
 * [#x09DF-#x09E1] | [#x09F0-#x09F1] | [#x0A05-#x0A0A] | [#x0A0F-#x0A10] |
 * [#x0A13-#x0A28] | [#x0A2A-#x0A30] | [#x0A32-#x0A33] | [#x0A35-#x0A36] |
 * [#x0A38-#x0A39] | [#x0A59-#x0A5C] | #x0A5E | [#x0A72-#x0A74] |
 * [#x0A85-#x0A8B] | #x0A8D | [#x0A8F-#x0A91] | [#x0A93-#x0AA8] |
 * [#x0AAA-#x0AB0] | [#x0AB2-#x0AB3] | [#x0AB5-#x0AB9] | #x0ABD | #x0AE0 |
 * [#x0B05-#x0B0C] | [#x0B0F-#x0B10] | [#x0B13-#x0B28] | [#x0B2A-#x0B30] |
 * [#x0B32-#x0B33] | [#x0B36-#x0B39] | #x0B3D | [#x0B5C-#x0B5D] |
 * [#x0B5F-#x0B61] | [#x0B85-#x0B8A] | [#x0B8E-#x0B90] | [#x0B92-#x0B95] |
 * [#x0B99-#x0B9A] | #x0B9C | [#x0B9E-#x0B9F] | [#x0BA3-#x0BA4] |
 * [#x0BA8-#x0BAA] | [#x0BAE-#x0BB5] | [#x0BB7-#x0BB9] | [#x0C05-#x0C0C] |
 * [#x0C0E-#x0C10] | [#x0C12-#x0C28] | [#x0C2A-#x0C33] | [#x0C35-#x0C39] |
 * [#x0C60-#x0C61] | [#x0C85-#x0C8C] | [#x0C8E-#x0C90] | [#x0C92-#x0CA8] |
 * [#x0CAA-#x0CB3] | [#x0CB5-#x0CB9] | #x0CDE | [#x0CE0-#x0CE1] |
 * [#x0D05-#x0D0C] | [#x0D0E-#x0D10] | [#x0D12-#x0D28] | [#x0D2A-#x0D39] |
 * [#x0D60-#x0D61] | [#x0E01-#x0E2E] | #x0E30 | [#x0E32-#x0E33] |
 * [#x0E40-#x0E45] | [#x0E81-#x0E82] | #x0E84 | [#x0E87-#x0E88] | #x0E8A |
 * #x0E8D | [#x0E94-#x0E97] | [#x0E99-#x0E9F] | [#x0EA1-#x0EA3] | #x0EA5 |
 * #x0EA7 | [#x0EAA-#x0EAB] | [#x0EAD-#x0EAE] | #x0EB0 | [#x0EB2-#x0EB3] |
 * #x0EBD | [#x0EC0-#x0EC4] | [#x0F40-#x0F47] | [#x0F49-#x0F69] |
 * [#x10A0-#x10C5] | [#x10D0-#x10F6] | #x1100 | [#x1102-#x1103] |
 * [#x1105-#x1107] | #x1109 | [#x110B-#x110C] | [#x110E-#x1112] | #x113C |
 * #x113E | #x1140 | #x114C | #x114E | #x1150 | [#x1154-#x1155] | #x1159 |
 * [#x115F-#x1161] | #x1163 | #x1165 | #x1167 | #x1169 | [#x116D-#x116E] |
 * [#x1172-#x1173] | #x1175 | #x119E | #x11A8 | #x11AB | [#x11AE-#x11AF] |
 * [#x11B7-#x11B8] | #x11BA | [#x11BC-#x11C2] | #x11EB | #x11F0 | #x11F9 |
 * [#x1E00-#x1E9B] | [#x1EA0-#x1EF9] | [#x1F00-#x1F15] | [#x1F18-#x1F1D] |
 * [#x1F20-#x1F45] | [#x1F48-#x1F4D] | [#x1F50-#x1F57] | #x1F59 | #x1F5B |
 * #x1F5D | [#x1F5F-#x1F7D] | [#x1F80-#x1FB4] | [#x1FB6-#x1FBC] | #x1FBE |
 * [#x1FC2-#x1FC4] | [#x1FC6-#x1FCC] | [#x1FD0-#x1FD3] | [#x1FD6-#x1FDB] |
 * [#x1FE0-#x1FEC] | [#x1FF2-#x1FF4] | [#x1FF6-#x1FFC] | #x2126 |
 * [#x212A-#x212B] | #x212E | [#x2180-#x2182] | [#x3041-#x3094] |
 * [#x30A1-#x30FA] | [#x3105-#x312C] | [#xAC00-#xD7A3]
 * 
 * @see <a href="http://www.w3.org/TR/2008/REC-xml-20081126/#NT-BaseChar">
 *      BaseChar</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class BaseCharProduction extends XMLProduction {
    
    private final NonTerminalSymbol BaseChar;
    
    public BaseCharProduction(final Grammar<UnicodeTerminalSymbols> grammar) {
        super(grammar);
        BaseChar = this.grammar.getNonTerminalSymbol("BaseChar");
        
        this.grammar.addProduction(BaseChar, characterHandler, capitalA);
        this.grammar.addProduction(BaseChar, characterHandler, capitalB);
        this.grammar.addProduction(BaseChar, characterHandler, capitalC);
        this.grammar.addProduction(BaseChar, characterHandler, capitalD);
        this.grammar.addProduction(BaseChar, characterHandler, capitalE);
        this.grammar.addProduction(BaseChar, characterHandler, capitalF);
        this.grammar.addProduction(BaseChar, characterHandler, capitalG);
        this.grammar.addProduction(BaseChar, characterHandler, capitalH);
        this.grammar.addProduction(BaseChar, characterHandler, capitalI);
        this.grammar.addProduction(BaseChar, characterHandler, capitalJ);
        this.grammar.addProduction(BaseChar, characterHandler, capitalK);
        this.grammar.addProduction(BaseChar, characterHandler, capitalL);
        this.grammar.addProduction(BaseChar, characterHandler, capitalM);
        this.grammar.addProduction(BaseChar, characterHandler, capitalN);
        this.grammar.addProduction(BaseChar, characterHandler, capitalO);
        this.grammar.addProduction(BaseChar, characterHandler, capitalP);
        this.grammar.addProduction(BaseChar, characterHandler, capitalQ);
        this.grammar.addProduction(BaseChar, characterHandler, capitalR);
        this.grammar.addProduction(BaseChar, characterHandler, capitalS);
        this.grammar.addProduction(BaseChar, characterHandler, capitalT);
        this.grammar.addProduction(BaseChar, characterHandler, capitalU);
        this.grammar.addProduction(BaseChar, characterHandler, capitalV);
        this.grammar.addProduction(BaseChar, characterHandler, capitalW);
        this.grammar.addProduction(BaseChar, characterHandler, capitalX);
        this.grammar.addProduction(BaseChar, characterHandler, capitalY);
        this.grammar.addProduction(BaseChar, characterHandler, capitalZ);
        this.grammar.addProduction(BaseChar, characterHandler, smallA);
        this.grammar.addProduction(BaseChar, characterHandler, smallB);
        this.grammar.addProduction(BaseChar, characterHandler, smallC);
        this.grammar.addProduction(BaseChar, characterHandler, smallD);
        this.grammar.addProduction(BaseChar, characterHandler, smallE);
        this.grammar.addProduction(BaseChar, characterHandler, smallF);
        this.grammar.addProduction(BaseChar, characterHandler, smallG);
        this.grammar.addProduction(BaseChar, characterHandler, smallH);
        this.grammar.addProduction(BaseChar, characterHandler, smallI);
        this.grammar.addProduction(BaseChar, characterHandler, smallJ);
        this.grammar.addProduction(BaseChar, characterHandler, smallK);
        this.grammar.addProduction(BaseChar, characterHandler, smallL);
        this.grammar.addProduction(BaseChar, characterHandler, smallM);
        this.grammar.addProduction(BaseChar, characterHandler, smallN);
        this.grammar.addProduction(BaseChar, characterHandler, smallO);
        this.grammar.addProduction(BaseChar, characterHandler, smallP);
        this.grammar.addProduction(BaseChar, characterHandler, smallQ);
        this.grammar.addProduction(BaseChar, characterHandler, smallR);
        this.grammar.addProduction(BaseChar, characterHandler, smallS);
        this.grammar.addProduction(BaseChar, characterHandler, smallT);
        this.grammar.addProduction(BaseChar, characterHandler, smallU);
        this.grammar.addProduction(BaseChar, characterHandler, smallV);
        this.grammar.addProduction(BaseChar, characterHandler, smallW);
        this.grammar.addProduction(BaseChar, characterHandler, smallX);
        this.grammar.addProduction(BaseChar, characterHandler, smallY);
        this.grammar.addProduction(BaseChar, characterHandler, smallZ);
        // TODO: upper characters
    }
    
    @Override
    public NonTerminalSymbol getNonTerminalSymbol() {
        return BaseChar;
    }
    
}
