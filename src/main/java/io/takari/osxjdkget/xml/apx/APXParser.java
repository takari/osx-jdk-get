/* Generated By:JavaCC: Do not edit this line. APXParser.java */
/*-
 * Copyright (C) 2007 Erik Larsson
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.takari.osxjdkget.xml.apx;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.LinkedList;

import io.takari.osxjdkget.xml.Attribute;
import io.takari.osxjdkget.xml.DebugXMLContentHandler;
import io.takari.osxjdkget.xml.NullXMLContentHandler;
import io.takari.osxjdkget.xml.XMLContentHandler;

/** Early version of the APX Parser for XML. Currently does not parse DTDs and so doesn't check
    for any validity constraints. It just breaks up the syntactic structure of an XML file and
    reports it to an org.catacombae.xml.XMLContentHandler. This is sufficient for my needs so it
    remains to be seen if the parser will be extended in the future. */
public class APXParser implements APXParserConstants {
  public static final String DEFAULT_ENCODING = "US-ASCII";
  //private Reader usedReader;
  private XMLContentHandler contentHandler;

  /** Test main method. The first and only argument denotes a file containing an XML document. */
  public static void main(String[] args) throws Exception {
    InputStream is;
    if (args.length != 1)
      throw new RuntimeException("The only valid argument is the name of the input file!");

    // First, we read the encoding from the xml declaration (if it exists)
    APXParser encodingParser = create(new InputStreamReader(new FileInputStream(args[0]), DEFAULT_ENCODING),
      new NullXMLContentHandler(Charset.forName(DEFAULT_ENCODING)));
    String encoding = null;
    try {
      encoding = encodingParser.xmlDecl();
    } catch (ParseException pe) {
    }
    if (encoding == null)
      encoding = DEFAULT_ENCODING;

    encodingParser = null; // GC candy

    // Then we create a new stream, and parse the entire document using the appropriate encoding
    InputStreamReader usedReader;
    usedReader = new InputStreamReader(new FileInputStream(args[0]), encoding);
    APXParser a = create(usedReader, new DebugXMLContentHandler(Charset.forName(encoding))); //new NullXMLContentHandler(encoding));//
    a.xmlDocument();
  }

  /** This is the way to create an APXParser. Don't use the constructor even if it would be possible.
      @param misr the reader supplying the input data for the parser. Can not be null.
  @param xch the content handler that takes care of the contents of the parsed XML document. Can
           not be null. Use NullXMLContentHandler if you are not intrested in the contents of
     the document.
  @return the parser (parser.xmlDocument() will start the parsing) */
  public static APXParser create(Reader misr, XMLContentHandler xch) {
    APXParser a = new APXParser(misr);
    //a.usedReader = misr;
    a.contentHandler = xch;
    return a;
  }

  final public void xmlDocument() throws ParseException {
    prolog();
    element();
    label_1: while (true) {
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case STARTPI:
        case STARTCOMMENT:;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
      }
      misc();
    }
    jj_consume_token(0);
  }

  final public void prolog() throws ParseException {
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case STARTXMLDECL:
        xmlDecl();
        break;
      default:
        jj_la1[1] = jj_gen;;
    }
    label_2: while (true) {
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case STARTPI:
        case STARTCOMMENT:;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_2;
      }
      misc();
    }
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case STARTDOCTYPEDECL:
        doctypeDecl();
        label_3: while (true) {
          switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case STARTPI:
            case STARTCOMMENT:;
              break;
            default:
              jj_la1[3] = jj_gen;
              break label_3;
          }
          misc();
        }
        break;
      default:
        jj_la1[4] = jj_gen;;
    }
  }

  /* For convenience, this method returns the encoding. We need to determine that before anything else. */
  final public String xmlDecl() throws ParseException {
    String version, encoding = null;
    Boolean standalone = null;
    jj_consume_token(STARTXMLDECL);
    // Takes us to state WithinXMLDecl
    version = versionInfo();
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case ENCODING:
        /*LOOKAHEAD(2)*/ encoding = encodingDecl();
        break;
      default:
        jj_la1[5] = jj_gen;;
    }
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case STANDALONE:
        /*LOOKAHEAD(2)*/ standalone = sdDecl();
        break;
      default:
        jj_la1[6] = jj_gen;;
    }
    jj_consume_token(ENDXMLDECL);
    contentHandler.xmlDecl(version, encoding, standalone);
    {
      if (true)
        return encoding;
    }
    throw new Error("Missing return statement in function");
  }

  final public String versionInfo() throws ParseException {
    Token versionString;
    jj_consume_token(VERSION);
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case VERSION_DOPEN:
        jj_consume_token(VERSION_DOPEN);
        versionString = jj_consume_token(VER);
        jj_consume_token(VERSION_DCLOSE);
        break;
      case VERSION_SOPEN:
        jj_consume_token(VERSION_SOPEN);
        versionString = jj_consume_token(VER);
        jj_consume_token(VERSION_SCLOSE);
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }
    {
      if (true)
        return versionString.image;
    }
    throw new Error("Missing return statement in function");
  }

  final public String encodingDecl() throws ParseException {
    Token encoding;
    jj_consume_token(ENCODING);
    jj_consume_token(XMLDECL_EQ);
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case XD_SQUOTE_OPEN:
        jj_consume_token(XD_SQUOTE_OPEN);
        encoding = jj_consume_token(XD_SQUOTE_STRING);
        jj_consume_token(XD_SQUOTE_CLOSE);
        break;
      case XD_DQUOTE_OPEN:
        jj_consume_token(XD_DQUOTE_OPEN);
        encoding = jj_consume_token(XD_DQUOTE_STRING);
        jj_consume_token(XD_DQUOTE_CLOSE);
        break;
      default:
        jj_la1[8] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }
    {
      if (true)
        return encoding.image;
    }
    throw new Error("Missing return statement in function");
  }

  final public boolean sdDecl() throws ParseException {
    boolean b;
    jj_consume_token(STANDALONE);
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case STANDALONE_DOPEN:
        jj_consume_token(STANDALONE_DOPEN);
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
          case STANDALONE_YES:
            jj_consume_token(STANDALONE_YES);
            b = true;
            break;
          case STANDALONE_NO:
            jj_consume_token(STANDALONE_NO);
            b = false;
            break;
          default:
            jj_la1[9] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        jj_consume_token(STANDALONE_DCLOSE);
        break;
      case STANDALONE_SOPEN:
        jj_consume_token(STANDALONE_SOPEN);
        switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
          case STANDALONE_YES:
            jj_consume_token(STANDALONE_YES);
            b = true;
            break;
          case STANDALONE_NO:
            jj_consume_token(STANDALONE_NO);
            b = false;
            break;
          default:
            jj_la1[10] = jj_gen;
            jj_consume_token(-1);
            throw new ParseException();
        }
        jj_consume_token(STANDALONE_SCLOSE);
        break;
      default:
        jj_la1[11] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }
    {
      if (true)
        return b;
    }
    throw new Error("Missing return statement in function");
  }

  // Needs more work to support inline DTD declarations
  final public void doctypeDecl() throws ParseException {
    Token name;
    jj_consume_token(STARTDOCTYPEDECL);
    // DEFAULT -> WithinDoctypeDecl

    // Lexical state: WithinDoctypeDecl
    /*<WDD_S>*/ name = jj_consume_token(WDD_NAME);
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case EXTERNALID:
        jj_consume_token(EXTERNALID);
        break;
      default:
        jj_la1[12] = jj_gen;;
    }
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case BEGIN_INTSUBSET:
        jj_consume_token(BEGIN_INTSUBSET);
        intSubset();
        break;
      default:
        jj_la1[13] = jj_gen;;
    }
    jj_consume_token(ENDDOCTYPEDECL);
    contentHandler.doctype(name.image, null);
  }

  final public void intSubset() throws ParseException {
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case WHATEVER:
        jj_consume_token(WHATEVER);
        break;
      default:
        jj_la1[14] = jj_gen;;
    }
    jj_consume_token(END_INTSUBSET);
  }

  //void markupDecl() :
  //{}
  //{
  //	
  //}
  final public void misc() throws ParseException {
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case STARTCOMMENT:
        comment();
        break;
      case STARTPI:
        pi();
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }
  }

  final public void comment() throws ParseException {
    Token t;
    StringBuilder sb = new StringBuilder();
    jj_consume_token(STARTCOMMENT);
    label_4: while (true) {
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case COMMENT_CHAR:;
          break;
        default:
          jj_la1[16] = jj_gen;
          break label_4;
      }
      t = jj_consume_token(COMMENT_CHAR);
      sb.append(t.image);
    }
    jj_consume_token(ENDCOMMENT);
    contentHandler.comment(sb.toString());
  }

  final public void pi() throws ParseException {
    Token target;
    StringBuilder content = null;
    jj_consume_token(STARTPI);
    target = jj_consume_token(PITARGET);
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case WITHINPI_S:
        jj_consume_token(WITHINPI_S);
        content = piContent();
        break;
      case ENDPI:
        jj_consume_token(ENDPI);
        break;
      default:
        jj_la1[17] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }
    contentHandler.pi(target.image, content.toString());
  }

  // returns the content of the processor instruction, minus the trailing "?>"
  final public StringBuilder piContent() throws ParseException {
    Token t;
    StringBuilder sb = new StringBuilder();
    label_5: while (true) {
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case ENDPI:
          jj_consume_token(ENDPI);
          break;
        case PC_CHAR:
          t = jj_consume_token(PC_CHAR);
          sb.append(t.image);
          break;
        default:
          jj_la1[18] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case ENDPI:
        case PC_CHAR:;
          break;
        default:
          jj_la1[19] = jj_gen;
          break label_5;
      }
    }
    {
      if (true)
        return sb;
    }
    throw new Error("Missing return statement in function");
  }

  final public void element() throws ParseException {
    String name;
    Attribute currentAttribute;
    LinkedList<Attribute> attributes = new LinkedList<Attribute>();
    jj_consume_token(STARTTAG);
    // DEFAULT -> WithinTag
    name = elementname();
    label_6: while (true) {
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case WT_NAME:;
          break;
        default:
          jj_la1[20] = jj_gen;
          break label_6;
      }
      currentAttribute = attribute();
      attributes.add(currentAttribute);
    }
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case EMPTY_ENDTAG:
        jj_consume_token(EMPTY_ENDTAG);
        contentHandler.emptyElement(name, attributes);
        break;
      case ENDTAG:
        jj_consume_token(ENDTAG);
        contentHandler.startElement(name, attributes);
        content();
        etag(name);
        break;
      default:
        jj_la1[21] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }
  }

  final public String elementname() throws ParseException {
    Token t;
    t = jj_consume_token(WT_NAME);
    {
      if (true)
        return t.image;
    }
    throw new Error("Missing return statement in function");
  }

  final public Attribute attribute() throws ParseException {
    Token t;
    String name;
    LinkedList<Attribute.ValueComponent> value = new LinkedList<Attribute.ValueComponent>();
    name = elementname();
    jj_consume_token(WT_EQ);
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case WT_DQUOTE:
        jj_consume_token(WT_DQUOTE);
        label_7: while (true) {
          switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case ATTR_DQUOTE_STRING:
            case ATTR_REFERENCE:;
              break;
            default:
              jj_la1[22] = jj_gen;
              break label_7;
          }
          switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case ATTR_DQUOTE_STRING:
              t = jj_consume_token(ATTR_DQUOTE_STRING);
              value.add(new Attribute.StringComponent(t.image));
              break;
            case ATTR_REFERENCE:
              t = jj_consume_token(ATTR_REFERENCE);
              value.add(new Attribute.ReferenceComponent(t.image));
              break;
            default:
              jj_la1[23] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
          }
        }
        jj_consume_token(ATTR_DQUOTE);
        break;
      case WT_SQUOTE:
        jj_consume_token(WT_SQUOTE);
        label_8: while (true) {
          switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case ATTR_SQUOTE_STRING:
            case ATTR_REFERENCE:;
              break;
            default:
              jj_la1[24] = jj_gen;
              break label_8;
          }
          switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
            case ATTR_SQUOTE_STRING:
              t = jj_consume_token(ATTR_SQUOTE_STRING);
              value.add(new Attribute.StringComponent(t.image));
              break;
            case ATTR_REFERENCE:
              t = jj_consume_token(ATTR_REFERENCE);
              value.add(new Attribute.ReferenceComponent(t.image));
              break;
            default:
              jj_la1[25] = jj_gen;
              jj_consume_token(-1);
              throw new ParseException();
          }
        }
        jj_consume_token(ATTR_SQUOTE);
        break;
      default:
        jj_la1[26] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
    }
    {
      if (true)
        return new Attribute(name, new Attribute.Value(value));
    }
    throw new Error("Missing return statement in function");
  }

  final public void etag(String startName) throws ParseException {
    String name;
    jj_consume_token(STARTCLOSINGTAG);
    // DEFAULT -> WithinTag
    name = elementname();
    jj_consume_token(ENDTAG);
    if (startName.equals(name))
      contentHandler.endElement(name);
    else {
      if (true)
        throw new ParseException("Expected \"</" + startName + "\" but got \"</" + name + "\".");
    }
  }

  final public void content() throws ParseException {
    //StringBuilder sb = new StringBuilder();
    Token t;
    switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
      case CHARDATA:
        charData();
        break;
      default:
        jj_la1[27] = jj_gen;;
    }
    label_9: while (true) {
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case STARTTAG:
        case STARTPI:
        case STARTCOMMENT:
        case STARTCDATA:
        case DEFAULT_REFERENCE:;
          break;
        default:
          jj_la1[28] = jj_gen;
          break label_9;
      }
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case STARTTAG:
          element();
          break;
        case DEFAULT_REFERENCE:
          t = jj_consume_token(DEFAULT_REFERENCE);
          break;
        case STARTCDATA:
          cdSect();
          break;
        case STARTPI:
          pi();
          break;
        case STARTCOMMENT:
          comment();
          break;
        default:
          jj_la1[29] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case CHARDATA:
          charData();
          break;
        default:
          jj_la1[30] = jj_gen;;
      }
    }
  }

  final public void charData() throws ParseException {
    Token t;
    int beginLine = -1, beginColumn = -1;
    label_10: while (true) {
      t = jj_consume_token(CHARDATA);
      if (beginLine == -1) {
        beginLine = t.beginLine;
        beginColumn = t.beginColumn;
      }
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case CHARDATA:;
          break;
        default:
          jj_la1[31] = jj_gen;
          break label_10;
      }
    }
    contentHandler.chardata(beginLine, beginColumn, t.endLine, t.endColumn);
  }

  final public void cdSect() throws ParseException {
    Token t;
    StringBuilder cdata = new StringBuilder();
    jj_consume_token(STARTCDATA);
    label_11: while (true) {
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case ENDCDATA:
        case WCD_CHAR:;
          break;
        default:
          jj_la1[32] = jj_gen;
          break label_11;
      }
      switch ((jj_ntk == -1) ? jj_ntk() : jj_ntk) {
        case ENDCDATA:
          jj_consume_token(ENDCDATA);
          break;
        case WCD_CHAR:
          t = jj_consume_token(WCD_CHAR);
          cdata.append(t.image);
          break;
        default:
          jj_la1[33] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
      }
    }
    contentHandler.cdata(cdata.toString());
  }

  public APXParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[34];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static private int[] jj_la1_2;
  static {
    jj_la1_0();
    jj_la1_1();
    jj_la1_2();
  }

  private static void jj_la1_0() {
    jj_la1_0 = new int[] {0x240000, 0x80000, 0x240000, 0x240000, 0x100000, 0x40000000, 0x80000000, 0x0, 0x18000000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x240000, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
        0x0, 0x0, 0x800000, 0x1650000, 0x1650000, 0x800000, 0x800000, 0x0, 0x0,};
  }

  private static void jj_la1_1() {
    jj_la1_1 = new int[] {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xa0, 0x0, 0xc000, 0xc000, 0x1400, 0x40000, 0x400000, 0x2000000, 0x0, 0x10000000, 0x20000000, 0x20000000, 0x20000000, 0x0, 0x0, 0x0, 0x0,
        0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,};
  }

  private static void jj_la1_2() {
    jj_la1_2 = new int[] {0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x2, 0x2, 0x8, 0xc0, 0x2800, 0x2800, 0x3000, 0x3000, 0x30, 0x0, 0x0, 0x0, 0x0, 0x0,
        0xc000, 0xc000,};
  }

  public APXParser(java.io.InputStream stream) {
    this(stream, null);
  }

  public APXParser(java.io.InputStream stream, String encoding) {
    try {
      jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
    } catch (java.io.UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    token_source = new APXParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 34; i++)
      jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    ReInit(stream, null);
  }

  public void ReInit(java.io.InputStream stream, String encoding) {
    try {
      jj_input_stream.ReInit(stream, encoding, 1, 1);
    } catch (java.io.UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 34; i++)
      jj_la1[i] = -1;
  }

  public APXParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new APXParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 34; i++)
      jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 34; i++)
      jj_la1[i] = -1;
  }

  public APXParser(APXParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 34; i++)
      jj_la1[i] = -1;
  }

  public void ReInit(APXParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 34; i++)
      jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null)
      token = token.next;
    else
      token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null)
      token = token.next;
    else
      token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null)
        t = t.next;
      else
        t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt = token.next) == null)
      return (jj_ntk = (token.next = token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector<int[]> jj_expentries = new java.util.Vector<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[80];
    for (int i = 0; i < 80; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 34; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1 << j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1 << j)) != 0) {
            la1tokens[32 + j] = true;
          }
          if ((jj_la1_2[i] & (1 << j)) != 0) {
            la1tokens[64 + j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 80; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {}

  final public void disable_tracing() {}

}
