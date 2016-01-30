package org.silnith.browser.organic.parser.html4.token;

import java.util.HashMap;
import java.util.Map;


public class NamedEntityReference extends EntityReference {
    
    private static final Map<String, Integer> lookupMap;
    
    static {
        lookupMap = new HashMap<>();
        lookupMap.put("nbsp", 160);
        lookupMap.put("iexcl", 161);
        lookupMap.put("cent", 162);
        lookupMap.put("pound", 163);
        lookupMap.put("curren", 164);
        lookupMap.put("yen", 165);
        lookupMap.put("brvbar", 166);
        lookupMap.put("sect", 167);
        lookupMap.put("uml", 168);
        lookupMap.put("copy", 169);
        lookupMap.put("ordf", 170);
        lookupMap.put("laquo", 171);
        lookupMap.put("not", 172);
        lookupMap.put("shy", 173);
        lookupMap.put("reg", 174);
        lookupMap.put("macr", 175);
        lookupMap.put("deg", 176);
        lookupMap.put("plusmn", 177);
        lookupMap.put("sup2", 178);
        lookupMap.put("sup3", 179);
        lookupMap.put("acute", 180);
        lookupMap.put("micro", 181);
        lookupMap.put("para", 182);
        lookupMap.put("middot", 183);
        lookupMap.put("cedil", 184);
        lookupMap.put("sup1", 185);
        lookupMap.put("ordm", 186);
        lookupMap.put("raquo", 187);
        lookupMap.put("frac14", 188);
        lookupMap.put("frac12", 189);
        lookupMap.put("frac34", 190);
        lookupMap.put("iquest", 191);
        lookupMap.put("Agrave", 192);
        lookupMap.put("Aacute", 193);
        lookupMap.put("Acirc", 194);
        lookupMap.put("Atilde", 195);
        lookupMap.put("Auml", 196);
        lookupMap.put("Aring", 197);
        lookupMap.put("AElig", 198);
        lookupMap.put("Ccedil", 199);
        lookupMap.put("Egrave", 200);
        lookupMap.put("Eacute", 201);
        lookupMap.put("Ecirc", 202);
        lookupMap.put("Euml", 203);
        lookupMap.put("Igrave", 204);
        lookupMap.put("Iacute", 205);
        lookupMap.put("Icirc", 206);
        lookupMap.put("Iuml", 207);
        lookupMap.put("ETH", 208);
        lookupMap.put("Ntilde", 209);
        lookupMap.put("Ograve", 210);
        lookupMap.put("Oacute", 211);
        lookupMap.put("Ocirc", 212);
        lookupMap.put("Otilde", 213);
        lookupMap.put("Ouml", 214);
        lookupMap.put("times", 215);
        lookupMap.put("Oslash", 216);
        lookupMap.put("Ugrave", 217);
        lookupMap.put("Uacute", 218);
        lookupMap.put("Ucirc", 219);
        lookupMap.put("Uuml", 220);
        lookupMap.put("Yacute", 221);
        lookupMap.put("THORN", 222);
        lookupMap.put("szlig", 223);
        lookupMap.put("agrave", 224);
        lookupMap.put("aacute", 225);
        lookupMap.put("acirc", 226);
        lookupMap.put("atilde", 227);
        lookupMap.put("auml", 228);
        lookupMap.put("aring", 229);
        lookupMap.put("aelig", 230);
        lookupMap.put("ccedil", 231);
        lookupMap.put("egrave", 232);
        lookupMap.put("eacute", 233);
        lookupMap.put("ecirc", 234);
        lookupMap.put("euml", 235);
        lookupMap.put("igrave", 236);
        lookupMap.put("iacute", 237);
        lookupMap.put("icirc", 238);
        lookupMap.put("iuml", 239);
        lookupMap.put("eth", 240);
        lookupMap.put("ntilde", 241);
        lookupMap.put("ograve", 242);
        lookupMap.put("oacute", 243);
        lookupMap.put("ocirc", 244);
        lookupMap.put("otilde", 245);
        lookupMap.put("ouml", 246);
        lookupMap.put("divide", 247);
        lookupMap.put("oslash", 248);
        lookupMap.put("ugrave", 249);
        lookupMap.put("uacute", 250);
        lookupMap.put("ucirc", 251);
        lookupMap.put("uuml", 252);
        lookupMap.put("yacute", 253);
        lookupMap.put("thorn", 254);
        lookupMap.put("yuml", 255);
        
        lookupMap.put("fnof", 402);
        lookupMap.put("Alpha", 913);
        lookupMap.put("Beta", 914);
        lookupMap.put("Gamma", 915);
        lookupMap.put("Delta", 916);
        lookupMap.put("Epsilon", 917);
        lookupMap.put("Zeta", 918);
        lookupMap.put("Eta", 919);
        lookupMap.put("Theta", 920);
        lookupMap.put("Iota", 921);
        lookupMap.put("Kappa", 922);
        lookupMap.put("Lambda", 923);
        lookupMap.put("Mu", 924);
        lookupMap.put("Nu", 925);
        lookupMap.put("Xi", 926);
        lookupMap.put("Omicron", 927);
        lookupMap.put("Pi", 928);
        lookupMap.put("Rho", 929);
        // doesn't exist
        lookupMap.put("Sigma", 931);
        lookupMap.put("Tau", 932);
        lookupMap.put("Upsilon", 933);
        lookupMap.put("Phi", 934);
        lookupMap.put("Chi", 935);
        lookupMap.put("Psi", 936);
        lookupMap.put("Omega", 937);
        lookupMap.put("alpha", 945);
        lookupMap.put("beta", 946);
        lookupMap.put("gamma", 947);
        lookupMap.put("delta", 948);
        lookupMap.put("epsilon", 949);
        lookupMap.put("zeta", 950);
        lookupMap.put("eta", 951);
        lookupMap.put("theta", 952);
        lookupMap.put("iota", 953);
        lookupMap.put("kappa", 954);
        lookupMap.put("lambda", 955);
        lookupMap.put("mu", 956);
        lookupMap.put("nu", 957);
        lookupMap.put("xi", 958);
        lookupMap.put("omicron", 959);
        lookupMap.put("pi", 960);
        lookupMap.put("rho", 961);
        lookupMap.put("sigmaf", 962);
        lookupMap.put("sigma", 963);
        lookupMap.put("tau", 964);
        lookupMap.put("upsilon", 965);
        lookupMap.put("phi", 966);
        lookupMap.put("chi", 967);
        lookupMap.put("psi", 968);
        lookupMap.put("omega", 969);
        lookupMap.put("thetasym", 977);
        lookupMap.put("upsih", 978);
        lookupMap.put("piv", 982);
        lookupMap.put("bull", 8226);
        lookupMap.put("hellip", 8230);
        lookupMap.put("prime", 8242);
        lookupMap.put("Prime", 8243);
        lookupMap.put("oline", 8254);
        lookupMap.put("frasl", 8260);
        lookupMap.put("weierp", 8472);
        lookupMap.put("image", 8265);
        lookupMap.put("real", 8476);
        lookupMap.put("trade", 8482);
        lookupMap.put("alefsym", 8501);
        lookupMap.put("larr", 8592);
        lookupMap.put("uarr", 8593);
        lookupMap.put("rarr", 8594);
        lookupMap.put("darr", 8595);
        lookupMap.put("harr", 8596);
        lookupMap.put("crarr", 8629);
        lookupMap.put("lArr", 8656);
        lookupMap.put("uArr", 8657);
        lookupMap.put("rArr", 8658);
        lookupMap.put("dArr", 8659);
        lookupMap.put("hArr", 8660);
        lookupMap.put("forall", 8704);
        lookupMap.put("part", 8706);
        lookupMap.put("exist", 8707);
        lookupMap.put("empty", 8709);
        lookupMap.put("nabla", 8711);
        lookupMap.put("isin", 8712);
        lookupMap.put("notin", 8713);
        lookupMap.put("ni", 8715);
        lookupMap.put("prod", 8719);
        lookupMap.put("sum", 8721);
        lookupMap.put("minus", 8722);
        lookupMap.put("lowast", 8727);
        lookupMap.put("radic", 8730);
        lookupMap.put("prop", 8733);
        lookupMap.put("infin", 8734);
        lookupMap.put("ang", 8736);
        lookupMap.put("and", 8743);
        lookupMap.put("or", 8744);
        lookupMap.put("cap", 8745);
        lookupMap.put("cup", 8746);
        lookupMap.put("int", 8747);
        lookupMap.put("there4", 8756);
        lookupMap.put("sim", 8764);
        lookupMap.put("cong", 8773);
        lookupMap.put("asymp", 8776);
        lookupMap.put("ne", 8800);
        lookupMap.put("equiv", 8801);
        lookupMap.put("le", 8804);
        lookupMap.put("ge", 8805);
        lookupMap.put("sub", 8834);
        lookupMap.put("sup", 8835);
        lookupMap.put("nsub", 8836);
        lookupMap.put("sube", 8838);
        lookupMap.put("supe", 8839);
        lookupMap.put("oplus", 8853);
        lookupMap.put("otimes", 8855);
        lookupMap.put("perp", 8869);
        lookupMap.put("sdot", 8901);
        lookupMap.put("lceil", 8968);
        lookupMap.put("rceil", 8969);
        lookupMap.put("lfloor", 8970);
        lookupMap.put("rfloor", 8971);
        lookupMap.put("lang", 9001);
        lookupMap.put("rang", 9002);
        lookupMap.put("loz", 9674);
        lookupMap.put("spades", 9824);
        lookupMap.put("clubs", 9827);
        lookupMap.put("hearts", 9829);
        lookupMap.put("diams", 9830);
        
        lookupMap.put("quot", 34);
        lookupMap.put("amp", 38);
        lookupMap.put("lt", 60);
        lookupMap.put("gt", 62);
        lookupMap.put("OElig", 338);
        lookupMap.put("oelig", 339);
        lookupMap.put("Scaron", 352);
        lookupMap.put("scaron", 353);
        lookupMap.put("Yuml", 376);
        lookupMap.put("circ", 710);
        lookupMap.put("tilde", 732);
        lookupMap.put("ensp", 8194);
        lookupMap.put("emsp", 8195);
        lookupMap.put("thinsp", 8201);
        lookupMap.put("zwnj", 8204);
        lookupMap.put("zwj", 8205);
        lookupMap.put("lrm", 8206);
        lookupMap.put("rlm", 8207);
        lookupMap.put("ndash", 8211);
        lookupMap.put("mdash", 8212);
        lookupMap.put("lsquo", 8216);
        lookupMap.put("rsquo", 8217);
        lookupMap.put("sbquo", 8218);
        lookupMap.put("ldquo", 8220);
        lookupMap.put("rdquo", 8221);
        lookupMap.put("bdquo", 8222);
        lookupMap.put("dagger", 8224);
        lookupMap.put("Dagger", 8225);
        lookupMap.put("permil", 8240);
        lookupMap.put("lsaquo", 8249);
        lookupMap.put("rsaquo", 8250);
        lookupMap.put("euro", 8364);
    }
    
    private final String entity;
    
    public NamedEntityReference(final String content) {
        super(content);
        this.entity = new NumericEntityReference(String.valueOf(lookupMap.get(content))).getEntity();
    }
    
    @Override
    public String getEntity() {
        return entity;
    }
    
}
