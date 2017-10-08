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
    Deque<String> nesting = new ArrayDeque<>();
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
    private int last_state;
    private void transition(int state) {
        System.out.printf("Transitioning to state %s \n", state);
        if (yy_lexical_state != state) {
            last_state = yy_lexical_state;
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
    last_state = yy_lexical_state;
	}

	private boolean yy_eof_done = false;
	private final int FEATURE_DEC = 7;
	private final int FEATURE = 6;
	private final int YYINITIAL = 0;
	private final int EXPR = 4;
	private final int EXPR_STRING = 5;
	private final int FORMAL = 8;
	private final int COMMENT = 3;
	private final int CLASS_DEC = 2;
	private final int CLASS = 1;
	private final int yy_state_dtrans[] = {
		0,
		41,
		42,
		40,
		43,
		43,
		43,
		43,
		43
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
		/* 11 */ YY_NOT_ACCEPT,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NOT_ACCEPT,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NOT_ACCEPT,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NOT_ACCEPT,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NOT_ACCEPT,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NOT_ACCEPT,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NOT_ACCEPT,
		/* 27 */ YY_NOT_ACCEPT,
		/* 28 */ YY_NOT_ACCEPT,
		/* 29 */ YY_NOT_ACCEPT,
		/* 30 */ YY_NOT_ACCEPT,
		/* 31 */ YY_NOT_ACCEPT,
		/* 32 */ YY_NOT_ACCEPT,
		/* 33 */ YY_NOT_ACCEPT,
		/* 34 */ YY_NOT_ACCEPT,
		/* 35 */ YY_NOT_ACCEPT,
		/* 36 */ YY_NOT_ACCEPT,
		/* 37 */ YY_NOT_ACCEPT,
		/* 38 */ YY_NOT_ACCEPT,
		/* 39 */ YY_NOT_ACCEPT,
		/* 40 */ YY_NOT_ACCEPT,
		/* 41 */ YY_NOT_ACCEPT,
		/* 42 */ YY_NOT_ACCEPT,
		/* 43 */ YY_NOT_ACCEPT
	};
	private int yy_cmap[] = unpackFromString(1,130,
"11:10,0,11:2,0,11:18,1,11:7,8,10,9,11:18,23,24,11:6,22,11:10,21,11:3,19,11:" +
"4,20,11:2,2,11:4,14,11,12,11,18,4,11,17,16,11:2,13,11,3,11:3,5,15,6,11,7,11" +
":9,25:2")[0];

	private int yy_rmap[] = unpackFromString(1,44,
"0,1,2:7,1,2,3,2:2,4,5:2,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,2" +
"4,25,26,27,28,29,30,31,32")[0];

	private int yy_nxt[][] = unpackFromString(33,26,
"-1,1,12:6,15,12:3,18,12:10,21,12,2,-1:2,11,-1:52,17,-1:36,20,-1:20,3,-1:18," +
"22,-1:36,14,-1:22,10,-1:30,24,-1:34,4,-1:5,26,-1:24,32,-1:37,5,-1:30,33,-1:" +
"7,27,-1:28,28,-1:22,29,-1:29,30,-1:21,31,-1:30,6,-1:35,34,-1:29,35,-1:22,36" +
",-1:29,7,-1:8,37,-1:36,38,-1:15,39,-1:34,8,-1:11,9,13:6,16,19,13:15,2,-1,1," +
"12:6,15,12:16,2,-1,12:7,15,12:7,23,12:2,25,12:5,2,-1,12:7,15,12:16,2");

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
        default: // If in any other state, then something was left unclosed
            return new Symbol(TokenConstants.error);
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
						{ /* This rule should be the very last
       in your lexical specification and will match match everything not
       matched by other lexical rules. */
    System.out.printf("State: %s", yy_lexical_state);
    System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}
					case -2:
						break;
					case 2:
						
					case -3:
						break;
					case 3:
						{
    transition(COMMENT);
    nesting.push("(*");
}
					case -4:
						break;
					case 4:
						{
    /* Sample lexical rule for "=>" arrow.
       Further lexical rules should be defined
       here, after the last %% separator */
    return new Symbol(TokenConstants.DARROW);
}
					case -5:
						break;
					case 5:
						{
    transition(CLASS_DEC);
    return new Symbol(TokenConstants.CLASS);
}
					case -6:
						break;
					case 6:
						{
    // Eat whitespace where relevant
    return null;
}
					case -7:
						break;
					case 7:
						{
    return new Symbol(TokenConstants.TYPEID);
}
					case -8:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -9:
						break;
					case 9:
						{
    // Ignore anything in a comment
    System.out.println("Found comment content!");
}
					case -10:
						break;
					case 10:
						{
    String last_open = nesting.pop();
    if ("(*".equals(last_open)) {
        if ("(*".equals(nesting.peek())) {
            // Still in a comment, don't transition state
        }
        else { // No longer in a comment, transition state
            transition(last_state);
        }
    } else {
        // In comment without open comment in `nesting`, something's fishy.
        return new Symbol(TokenConstants.error);
    }
}
					case -11:
						break;
					case 12:
						{ /* This rule should be the very last
       in your lexical specification and will match match everything not
       matched by other lexical rules. */
    System.out.printf("State: %s", yy_lexical_state);
    System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}
					case -12:
						break;
					case 13:
						{
    // Ignore anything in a comment
    System.out.println("Found comment content!");
}
					case -13:
						break;
					case 15:
						{ /* This rule should be the very last
       in your lexical specification and will match match everything not
       matched by other lexical rules. */
    System.out.printf("State: %s", yy_lexical_state);
    System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}
					case -14:
						break;
					case 16:
						{
    // Ignore anything in a comment
    System.out.println("Found comment content!");
}
					case -15:
						break;
					case 18:
						{ /* This rule should be the very last
       in your lexical specification and will match match everything not
       matched by other lexical rules. */
    System.out.printf("State: %s", yy_lexical_state);
    System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}
					case -16:
						break;
					case 19:
						{
    // Ignore anything in a comment
    System.out.println("Found comment content!");
}
					case -17:
						break;
					case 21:
						{ /* This rule should be the very last
       in your lexical specification and will match match everything not
       matched by other lexical rules. */
    System.out.printf("State: %s", yy_lexical_state);
    System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}
					case -18:
						break;
					case 23:
						{ /* This rule should be the very last
       in your lexical specification and will match match everything not
       matched by other lexical rules. */
    System.out.printf("State: %s", yy_lexical_state);
    System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}
					case -19:
						break;
					case 25:
						{ /* This rule should be the very last
       in your lexical specification and will match match everything not
       matched by other lexical rules. */
    System.out.printf("State: %s", yy_lexical_state);
    System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}
					case -20:
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
