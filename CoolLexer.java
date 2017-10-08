/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;
import java.util.Deque;
import java.util.ArrayDeque;
class Yytoken {
    int id;
    String value;
    Yytoken(int id) {
        this.id = id;
    }
    Yytoken(int id, String value) {
        this.id = id;
        this.value = value;
    }
} 
class NestedState {
    String prefix;
    int state;
    NestedState(String prefix, int state) {
        this.prefix = prefix;
        this.state = state;
    }
}


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuilder sb = new StringBuilder();
    // Serves as a stack of braces, parentheses, etc.
    Deque<NestedState> nesting = new ArrayDeque<>();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
    private void transition(int state) {
        System.out.printf("Transitioning to state %s \n", state);
        if (yy_lexical_state != state) {
            yybegin(state);
        }
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    nesting.push(new NestedState("", YYINITIAL));
	}

	private boolean yy_eof_done = false;
	private final int INT = 11;
	private final int FEATURE = 7;
	private final int STRING = 10;
	private final int FUNCTION_PARAMETERS = 9;
	private final int EXPR_BLOCK = 6;
	private final int YYINITIAL = 0;
	private final int EXPR = 4;
	private final int FUNCTION = 8;
	private final int EXPR_STRING = 5;
	private final int COMMENT = 3;
	private final int CLASS_DEC = 2;
	private final int CLASS = 1;
	private final int yy_state_dtrans[] = {
		0,
		71,
		72,
		79,
		84,
		84,
		80,
		84,
		81,
		82,
		83,
		84
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NOT_ACCEPT,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NOT_ACCEPT,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NOT_ACCEPT,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NOT_ACCEPT,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NOT_ACCEPT,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NOT_ACCEPT,
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NOT_ACCEPT,
		/* 73 */ YY_NOT_ACCEPT,
		/* 74 */ YY_NOT_ACCEPT,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NOT_ACCEPT,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NOT_ACCEPT,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NOT_ACCEPT,
		/* 81 */ YY_NOT_ACCEPT,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NOT_ACCEPT,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"33,35:8,43,34,35,43,19,35:18,43,35,32,35:5,16,18,17,41,39,37,29,42,38:10,20" +
",21,36,40,35:3,23,24,25,24,5,24:6,26,24:5,7,4,24,27,24:5,35:4,28,35,3,30,1," +
"30,13,9,30,12,10,30:2,2,30,11,30:3,14,15,6,8,44,30:4,22,35,31,35:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,96,
"0,1,2,3,1:7,4,1:9,5,6,3,1:15,7,1,6,5:2,8,9,10:2,11,12,5,13,12:2,14,15,16,15" +
":2,17,18,3,4,19,20,21,22,4,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38," +
"39,10,40,41,42,43,44,45,46,47,48,49")[0];

	private int yy_nxt[][] = unpackFromString(50,45,
"1,2,40:3,85,49,40:2,55,40:3,85,40:2,3,4,5,6,40:2,7,40:2,2,40:5,8,9,40,6,40:" +
"2,10,11,12,13,14,15,6:2,-1:47,39,-1:23,39,-1:35,16,-1:65,11,-1:7,21:15,-1:7" +
",21:6,-1,21,-1:7,21,-1:5,21,-1,22:15,-1:7,22:6,-1,22,-1:7,22,-1:5,22,-1:3,8" +
"8,-1:19,88,-1:22,21:4,42,21:7,42,21:2,-1:7,21:6,-1,21,-1:7,21,-1:5,21,-1,22" +
":4,41,22:7,41,22:2,-1:7,22:6,-1,22,-1:7,22,-1:5,22,-1:2,48,-1:23,48,-1:22,6" +
"3,-1:10,63,-1:36,54,-1:6,54,-1:31,21:4,50,21:7,50,21:2,-1:7,21:6,-1,21,-1:7" +
",21,-1:5,21,-1:8,66,-1:18,66,-1:20,59,-1:19,59,-1:22,21:4,43,21:7,43,21:2,-" +
"1:7,21:6,-1,21,-1:7,21,-1:5,21,-1:2,68,-1:23,68,-1:29,73,-1:38,17,-1:7,17,-" +
"1:68,32,-1:25,30,-1:31,18,-1:7,18,-1:35,70,-1:10,70,-1:33,19,-1:10,19,-1:34" +
",20,-1:7,20,-1:31,1,21:3,22,92,91,22,21,95,21:3,93,21:2,23,4,5,6,24,25,26,2" +
"2:5,40:2,21,8,9,40,6,40:2,10,11,12,13,14,15,6,21,1,40:3,22,92,49,22,40,55,6" +
"0,40:2,85,40:2,3,4,5,6,24,40,27,22:5,40:3,8,9,40,6,40:2,10,11,12,13,14,15,6" +
":2,-1:12,74,-1:45,75,-1:45,76,-1:40,77,-1:40,78,-1:53,28,-1:29,1,29:4,46,52" +
",29:2,57,29:3,46,29:2,61,65,29:20,67,29:6,1,21:3,40,85,91,40,21,95,21:3,93," +
"21:2,3,4,5,6,40,25,7,40:6,31,21,8,9,40,6,40,64,10,11,12,13,14,15,6,21,1,21:" +
"3,40,85,91,40,21,95,21:3,93,21:2,3,4,5,6,40:2,33,40:6,31,21,8,9,40,6,40:2,1" +
"0,11,12,13,14,15,6,21,1,21:3,22,92,91,22,21,95,21:3,93,21:2,3,4,34,6,24,40," +
"7,22:5,40:2,21,8,9,40,6,40:2,10,11,12,13,14,15,6,21,1,35:4,47,53,35:2,58,35" +
":3,47,35:2,3,35,5,6,35:2,7,35:8,8,36,37,38,35:3,62,35:6,1,40:4,85,49,40:2,5" +
"5,40:3,85,40:2,3,4,5,6,40:2,7,40:8,8,9,40,6,40:2,10,11,12,13,14,15,6:2,-1,2" +
"1:7,44,21:7,-1:7,21:4,44,21,-1,21,-1:7,21,-1:5,21,-1,22:3,45,22:10,45,-1:7," +
"22:6,-1,22,-1:7,22,-1:5,22,-1:4,69,-1:10,69,-1:30,21:3,51,21:10,51,-1:7,21:" +
"6,-1,21,-1:7,21,-1:5,21,-1,21:3,56,21:10,56,-1:7,21:6,-1,21,-1:7,21,-1:5,21" +
",-1,21:6,86,21:6,86,21,-1:7,21:6,-1,21,-1:7,21,-1:5,21,-1,22,87,22:13,-1:7," +
"22:3,87,22:2,-1,22,-1:7,22,-1:5,22,-1,21,89,21:13,-1:7,21:3,89,21:2,-1,21,-" +
"1:7,21,-1:5,21,-1,21,90,21:13,-1:7,21:3,90,21:2,-1,21,-1:7,21,-1:5,21,-1,21" +
":2,94,21:12,-1:7,94,21:5,-1,21,-1:7,21,-1:5,21");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

    switch(yy_lexical_state) {
        case YYINITIAL: // Expected
            break;
        case COMMENT:
            return new Symbol(TokenConstants.ERROR, "EOF in comment");
        default: // If in any other state, then something was left unclosed
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
            }
            return new Symbol(TokenConstants.ERROR,
                    String.format("EOF in state %d.", yy_lexical_state));
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ 
    // Anything unmatched
    System.err.printf("UNMATCHED: %s (State %d)\n", yytext(), yy_lexical_state);
}
					case -3:
						break;
					case 3:
						{
    nesting.push(new NestedState("(", yy_lexical_state));
    return new Symbol(TokenConstants.LPAREN);
}
					case -4:
						break;
					case 4:
						{
    return new Symbol(TokenConstants.MULT);
}
					case -5:
						break;
					case 5:
						{
    if (!"(".equals(nesting.pop().prefix) || nesting.peek() == null) {
        return new Symbol(TokenConstants.ERROR, "Unmatched ')'");
    }
    transition(nesting.peek().state);
    return new Symbol(TokenConstants.RPAREN);
}
					case -6:
						break;
					case 6:
						{
    // whitespace
}
					case -7:
						break;
					case 7:
						{
    nesting.push(new NestedState("{", yy_lexical_state));
    return new Symbol(TokenConstants.LBRACE);
}
					case -8:
						break;
					case 8:
						{
    if (!"{".equals(nesting.pop().prefix) || nesting.peek() == null) {
        return new Symbol(TokenConstants.ERROR, "Unmatched '}'");
    }
    transition(nesting.peek().state);
    return new Symbol(TokenConstants.RBRACE);
}
					case -9:
						break;
					case 9:
						{
    transition(STRING);
    nesting.push(new NestedState("\"", yy_lexical_state));
}
					case -10:
						break;
					case 10:
						{
    return new Symbol(TokenConstants.MINUS);
}
					case -11:
						break;
					case 11:
						{
    return new Symbol(TokenConstants.INT_CONST,
                      AbstractTable.inttable.addString(yytext()));
}
					case -12:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.COMMA);
}
					case -13:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.EQ);
}
					case -14:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.PLUS);
}
					case -15:
						break;
					case 15:
						{
    return new Symbol(TokenConstants.DIV);
}
					case -16:
						break;
					case 16:
						{
    transition(COMMENT);
    System.out.println("(*");
    nesting.push(new NestedState("(*", COMMENT));
}
					case -17:
						break;
					case 17:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -18:
						break;
					case 18:
						{
    return new Symbol(TokenConstants.BOOL_CONST, true);
}
					case -19:
						break;
					case 19:
						{
    transition(CLASS_DEC);
    return new Symbol(TokenConstants.CLASS);
}
					case -20:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.BOOL_CONST, false);
}
					case -21:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -22:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -23:
						break;
					case 23:
						{
    transition(FUNCTION_PARAMETERS);
    nesting.push(new NestedState("(", yy_lexical_state));
    return new Symbol(TokenConstants.LPAREN);
}
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.COLON);
}
					case -25:
						break;
					case 25:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -26:
						break;
					case 26:
						{
    transition(FUNCTION);
    nesting.push(new NestedState("{", yy_lexical_state));
    return new Symbol(TokenConstants.LBRACE);
}
					case -27:
						break;
					case 27:
						{
    transition(CLASS);
    nesting.push(new NestedState("{", yy_lexical_state));
    return new Symbol(TokenConstants.LBRACE);
}
					case -28:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -29:
						break;
					case 29:
						{
    // Ignore anything in a comment
}
					case -30:
						break;
					case 30:
						{
    if ("(*".equals(nesting.pop().prefix)) {
        transition(nesting.peek().state);
        System.out.println("*)");
    } else {
        // In comment without open comment in `nesting`, something's fishy.
        return new Symbol(TokenConstants.ERROR);
    }
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.DOT);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -33:
						break;
					case 33:
						{
    transition(EXPR_BLOCK);
    nesting.push(new NestedState("{", yy_lexical_state));
    return new Symbol(TokenConstants.LBRACE);
}
					case -34:
						break;
					case 34:
						{
    if ("(".equals(nesting.pop().prefix)) {
        transition(nesting.peek().state);
        return new Symbol(TokenConstants.RPAREN);
    }
}
					case -35:
						break;
					case 35:
						{
    sb.append(yytext());
}
					case -36:
						break;
					case 36:
						{
    // End of string
    if (!"\"".equals(nesting.pop().prefix) || nesting.peek() == null) {
        return new Symbol(TokenConstants.ERROR, "Unmatched '\"'");
    }
    if (sb.length() >= MAX_STR_CONST) {
        return new Symbol(TokenConstants.ERROR, "String constant too long");
    }
    transition(nesting.peek().state);
    AbstractSymbol string = AbstractTable.idtable.addString(sb.toString());
    sb.setLength(0);
    return new Symbol(TokenConstants.STR_CONST, string);
}
					case -37:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -39:
						break;
					case 40:
						{ 
    // Anything unmatched
    System.err.printf("UNMATCHED: %s (State %d)\n", yytext(), yy_lexical_state);
}
					case -40:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -41:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.BOOL_CONST, true);
}
					case -42:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.BOOL_CONST, false);
}
					case -43:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -44:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -45:
						break;
					case 46:
						{
    // Ignore anything in a comment
}
					case -46:
						break;
					case 47:
						{
    sb.append(yytext());
}
					case -47:
						break;
					case 49:
						{ 
    // Anything unmatched
    System.err.printf("UNMATCHED: %s (State %d)\n", yytext(), yy_lexical_state);
}
					case -48:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -49:
						break;
					case 51:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -50:
						break;
					case 52:
						{
    // Ignore anything in a comment
}
					case -51:
						break;
					case 53:
						{
    sb.append(yytext());
}
					case -52:
						break;
					case 55:
						{ 
    // Anything unmatched
    System.err.printf("UNMATCHED: %s (State %d)\n", yytext(), yy_lexical_state);
}
					case -53:
						break;
					case 56:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -54:
						break;
					case 57:
						{
    // Ignore anything in a comment
}
					case -55:
						break;
					case 58:
						{
    sb.append(yytext());
}
					case -56:
						break;
					case 60:
						{ 
    // Anything unmatched
    System.err.printf("UNMATCHED: %s (State %d)\n", yytext(), yy_lexical_state);
}
					case -57:
						break;
					case 61:
						{
    // Ignore anything in a comment
}
					case -58:
						break;
					case 62:
						{
    sb.append(yytext());
}
					case -59:
						break;
					case 64:
						{ 
    // Anything unmatched
    System.err.printf("UNMATCHED: %s (State %d)\n", yytext(), yy_lexical_state);
}
					case -60:
						break;
					case 65:
						{
    // Ignore anything in a comment
}
					case -61:
						break;
					case 67:
						{
    // Ignore anything in a comment
}
					case -62:
						break;
					case 85:
						{ 
    // Anything unmatched
    System.err.printf("UNMATCHED: %s (State %d)\n", yytext(), yy_lexical_state);
}
					case -63:
						break;
					case 86:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -64:
						break;
					case 87:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -65:
						break;
					case 89:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -66:
						break;
					case 90:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -67:
						break;
					case 91:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -68:
						break;
					case 92:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -69:
						break;
					case 93:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -70:
						break;
					case 94:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -71:
						break;
					case 95:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -72:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
