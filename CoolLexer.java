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
    int get_curr_lineno() {
	return yyline + 1;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
    private void transition(int state) {
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
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		54,
		76
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
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
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
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NOT_ACCEPT,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"3,53:8,61,54,53,61,5,53:18,61,53,50,53:5,1,4,2,59,58,56,45,60,57:10,25,26,5" +
"5,48,49,53:2,28,29,30,31,32,15,29,33,34,29:2,35,29,36,37,38,29,39,40,19,41," +
"42,43,29:3,53,51,53:2,44,53,8,52,6,22,10,14,46,18,16,46:2,7,46,17,21,23,46," +
"12,9,11,13,20,24,46:3,27,53,47,53:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,162,
"0,1,2,3,1:3,4,5,1:6,6,1,7,1,8,1:5,9:2,10,9,1:3,9:15,2,1:6,11,12,13,14:2,15," +
"14:14,16,3,17,18,19,1,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37" +
",38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62" +
",63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87" +
",88,89,90,9,14,91,92,93,94,95,96,97,98,99")[0];

	private int yy_nxt[][] = unpackFromString(100,62,
"1,2,3,4,5,6,7,111,151:2,153,155,151:2,55,8,77,113,151,56,151,81,151,157,159" +
",9,10,11,152:2,154,152,156,152,78,112,114,82,158,152:4,160,12,13,151,14,15," +
"12,16,12,151,12,6,17,18,19,20,21,22,6,-1:64,23,-1:63,24,-1:63,151,161,115,1" +
"51:16,-1:3,115,151:6,161,151:9,-1,151,-1:5,151,-1:4,151,-1:10,152:10,57,152" +
":8,-1:3,152:6,57,152:10,-1,152,-1:5,152,-1:4,152,-1:53,29,-1:60,30,-1:7,31," +
"-1:62,19,-1:10,151:19,-1:3,151:17,-1,151,-1:5,151,-1:4,151,-1:10,151:12,141" +
",151:6,-1:3,151:5,141,151:11,-1,151,-1:5,151,-1:4,151,-1:4,1,47,74,4,79:58," +
"-1:6,151:2,127,151:7,25,151:8,-1:3,127,151:5,25,151:10,-1,151,-1:5,151,-1:4" +
",151,-1:10,152:12,116,152:6,-1:3,152:5,116,152:11,-1,152,-1:5,152,-1:4,152," +
"-1:10,152:19,-1:3,152:17,-1,152,-1:5,152,-1:4,152,-1:10,152:12,138,152:6,-1" +
":3,152:5,138,152:11,-1,152,-1:5,152,-1:4,152,-1:8,48,-1:57,1,49,75,4,49,6,4" +
"9:44,50,80,49:2,51,49:7,-1:6,151:3,129,151:4,26:2,151,27,151:7,-1:3,151:8,2" +
"7,151:3,129,151:4,-1,151,-1:5,151,-1:4,151,-1:10,152:3,126,152:4,58:2,152,5" +
"9,152:7,-1:3,152:8,59,152:3,126,152:4,-1,152,-1:5,152,-1:4,152,-1:5,52:4,-1" +
",52:5,53,52:2,53,52:2,53,52:34,53,52,-1,52:7,-1:6,151:8,28:2,151:9,-1:3,151" +
":17,-1,151,-1:5,151,-1:4,151,-1:10,152:8,60:2,152:9,-1:3,152:17,-1,152,-1:5" +
",152,-1:4,152,-1:10,151:5,32,151:7,32,151:5,-1:3,151:17,-1,151,-1:5,151,-1:" +
"4,151,-1:10,152:5,61,152:7,61,152:5,-1:3,152:17,-1,152,-1:5,152,-1:4,152,-1" +
":10,151:18,33,-1:3,151:15,33,151,-1,151,-1:5,151,-1:4,151,-1:10,152:18,62,-" +
"1:3,152:15,62,152,-1,152,-1:5,152,-1:4,152,-1:10,151:5,34,151:7,34,151:5,-1" +
":3,151:17,-1,151,-1:5,151,-1:4,151,-1:10,152:5,63,152:7,63,152:5,-1:3,152:1" +
"7,-1,152,-1:5,152,-1:4,152,-1:10,151:4,35,151:14,-1:3,151:4,35,151:12,-1,15" +
"1,-1:5,151,-1:4,151,-1:10,152:11,68,152:7,-1:3,152:8,68,152:8,-1,152,-1:5,1" +
"52,-1:4,152,-1:10,151:17,36,151,-1:3,151:10,36,151:6,-1,151,-1:5,151,-1:4,1" +
"51,-1:10,152:4,64,152:14,-1:3,152:4,64,152:12,-1,152,-1:5,152,-1:4,152,-1:1" +
"0,151:4,37,151:14,-1:3,151:4,37,151:12,-1,151,-1:5,151,-1:4,151,-1:10,152:4" +
",66,152:14,-1:3,152:4,66,152:12,-1,152,-1:5,152,-1:4,152,-1:10,38,151:18,-1" +
":3,151:2,38,151:14,-1,151,-1:5,151,-1:4,151,-1:10,67,152:18,-1:3,152:2,67,1" +
"52:14,-1,152,-1:5,152,-1:4,152,-1:10,151:4,39,151:14,-1:3,151:4,39,151:12,-" +
"1,151,-1:5,151,-1:4,151,-1:10,152:17,65,152,-1:3,152:10,65,152:6,-1,152,-1:" +
"5,152,-1:4,152,-1:10,151:11,40,151:7,-1:3,151:8,40,151:8,-1,151,-1:5,151,-1" +
":4,151,-1:10,152,69,152:17,-1:3,152:7,69,152:9,-1,152,-1:5,152,-1:4,152,-1:" +
"10,151,41,151:17,-1:3,151:7,41,151:9,-1,151,-1:5,151,-1:4,151,-1:10,152:3,7" +
"0,152:15,-1:3,152:12,70,152:4,-1,152,-1:5,152,-1:4,152,-1:10,151:3,42,151:1" +
"5,-1:3,151:12,42,151:4,-1,151,-1:5,151,-1:4,151,-1:10,152:4,71,152:14,-1:3," +
"152:4,71,152:12,-1,152,-1:5,152,-1:4,152,-1:10,151:4,43,151:14,-1:3,151:4,4" +
"3,151:12,-1,151,-1:5,151,-1:4,151,-1:10,152:16,72,152:2,-1:3,152:3,72,152:1" +
"3,-1,152,-1:5,152,-1:4,152,-1:10,151:4,44,151:14,-1:3,151:4,44,151:12,-1,15" +
"1,-1:5,151,-1:4,151,-1:10,152:3,73,152:15,-1:3,152:12,73,152:4,-1,152,-1:5," +
"152,-1:4,152,-1:10,151:16,45,151:2,-1:3,151:3,45,151:13,-1,151,-1:5,151,-1:" +
"4,151,-1:10,151:3,46,151:15,-1:3,151:12,46,151:4,-1,151,-1:5,151,-1:4,151,-" +
"1:10,151:4,83,151:10,117,151:3,-1:3,151:4,83,151:4,117,151:7,-1,151,-1:5,15" +
"1,-1:4,151,-1:10,152:4,84,152:10,128,152:3,-1:3,152:4,84,152:4,128,152:7,-1" +
",152,-1:5,152,-1:4,152,-1:10,151:4,85,151:10,87,151:3,-1:3,151:4,85,151:4,8" +
"7,151:7,-1,151,-1:5,151,-1:4,151,-1:10,152:4,86,152:10,88,152:3,-1:3,152:4," +
"86,152:4,88,152:7,-1,152,-1:5,152,-1:4,152,-1:10,151:3,89,151:15,-1:3,151:1" +
"2,89,151:4,-1,151,-1:5,151,-1:4,151,-1:10,152:4,90,152:14,-1:3,152:4,90,152" +
":12,-1,152,-1:5,152,-1:4,152,-1:10,151:15,91,151:3,-1:3,151:9,91,151:7,-1,1" +
"51,-1:5,151,-1:4,151,-1:10,152:2,134,152:16,-1:3,134,152:16,-1,152,-1:5,152" +
",-1:4,152,-1:10,151:3,93,151:15,-1:3,151:12,93,151:4,-1,151,-1:5,151,-1:4,1" +
"51,-1:10,152:3,92,152:15,-1:3,152:12,92,152:4,-1,152,-1:5,152,-1:4,152,-1:1" +
"0,151:2,95,151:16,-1:3,95,151:16,-1,151,-1:5,151,-1:4,151,-1:10,152:3,94,15" +
"2:15,-1:3,152:12,94,152:4,-1,152,-1:5,152,-1:4,152,-1:10,151:7,97,151:11,-1" +
":3,151:13,97,151:3,-1,151,-1:5,151,-1:4,151,-1:10,152:2,96,152:16,-1:3,96,1" +
"52:16,-1,152,-1:5,152,-1:4,152,-1:10,151:4,99,151:14,-1:3,151:4,99,151:12,-" +
"1,151,-1:5,151,-1:4,151,-1:10,152:14,136,152:4,-1:3,152:14,136,152:2,-1,152" +
",-1:5,152,-1:4,152,-1:10,151,137,151:17,-1:3,151:7,137,151:9,-1,151,-1:5,15" +
"1,-1:4,151,-1:10,152:15,98,152:3,-1:3,152:9,98,152:7,-1,152,-1:5,152,-1:4,1" +
"52,-1:10,151:14,139,151:4,-1:3,151:14,139,151:2,-1,151,-1:5,151,-1:4,151,-1" +
":10,152:15,100,152:3,-1:3,152:9,100,152:7,-1,152,-1:5,152,-1:4,152,-1:10,15" +
"1:15,101,151:3,-1:3,151:9,101,151:7,-1,151,-1:5,151,-1:4,151,-1:10,152:10,1" +
"40,152:8,-1:3,152:6,140,152:10,-1,152,-1:5,152,-1:4,152,-1:10,151:10,143,15" +
"1:8,-1:3,151:6,143,151:10,-1,151,-1:5,151,-1:4,151,-1:10,152:3,102,152:15,-" +
"1:3,152:12,102,152:4,-1,152,-1:5,152,-1:4,152,-1:10,151:3,103,151:15,-1:3,1" +
"51:12,103,151:4,-1,151,-1:5,151,-1:4,151,-1:10,152:15,142,152:3,-1:3,152:9," +
"142,152:7,-1,152,-1:5,152,-1:4,152,-1:10,151:3,105,151:15,-1:3,151:12,105,1" +
"51:4,-1,151,-1:5,151,-1:4,151,-1:10,152:4,144,152:14,-1:3,152:4,144,152:12," +
"-1,152,-1:5,152,-1:4,152,-1:10,151:15,145,151:3,-1:3,151:9,145,151:7,-1,151" +
",-1:5,151,-1:4,151,-1:10,152,104,152:17,-1:3,152:7,104,152:9,-1,152,-1:5,15" +
"2,-1:4,152,-1:10,151:4,147,151:14,-1:3,151:4,147,151:12,-1,151,-1:5,151,-1:" +
"4,151,-1:10,152:10,106,152:8,-1:3,152:6,106,152:10,-1,152,-1:5,152,-1:4,152" +
",-1:10,151,107,151:17,-1:3,151:7,107,151:9,-1,151,-1:5,151,-1:4,151,-1:10,1" +
"52:6,146,152:12,-1:3,152:11,146,152:5,-1,152,-1:5,152,-1:4,152,-1:10,151:10" +
",109,151:8,-1:3,151:6,109,151:10,-1,151,-1:5,151,-1:4,151,-1:10,152:10,148," +
"152:8,-1:3,152:6,148,152:10,-1,152,-1:5,152,-1:4,152,-1:10,151:6,149,151:12" +
",-1:3,151:11,149,151:5,-1,151,-1:5,151,-1:4,151,-1:10,152:5,108,152:7,108,1" +
"52:5,-1:3,152:17,-1,152,-1:5,152,-1:4,152,-1:10,151:10,150,151:8,-1:3,151:6" +
",150,151:10,-1,151,-1:5,151,-1:4,151,-1:10,151:5,110,151:7,110,151:5,-1:3,1" +
"51:17,-1,151,-1:5,151,-1:4,151,-1:10,151,119,151,121,151:15,-1:3,151:7,119," +
"151:4,121,151:4,-1,151,-1:5,151,-1:4,151,-1:10,152,118,120,152:16,-1:3,120," +
"152:6,118,152:9,-1,152,-1:5,152,-1:4,152,-1:10,151:6,123,151:5,125,151:6,-1" +
":3,151:5,125,151:5,123,151:5,-1,151,-1:5,151,-1:4,151,-1:10,152,122,152,124" +
",152:15,-1:3,152:7,122,152:4,124,152:4,-1,152,-1:5,152,-1:4,152,-1:10,151:1" +
"5,131,151:3,-1:3,151:9,131,151:7,-1,151,-1:5,151,-1:4,151,-1:10,152:15,130," +
"152:3,-1:3,152:9,130,152:7,-1,152,-1:5,152,-1:4,152,-1:10,151:12,133,151:6," +
"-1:3,151:5,133,151:11,-1,151,-1:5,151,-1:4,151,-1:10,152:12,132,152:6,-1:3," +
"152:5,132,152:11,-1,152,-1:5,152,-1:4,152,-1:10,151:2,135,151:16,-1:3,135,1" +
"51:16,-1,151,-1:5,151,-1:4,151,-1:4");

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
            return new Symbol(TokenConstants.EOF);
        case COMMENT:
            transition(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in comment");
        case STRING:
            transition(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in String");
        default: // If in any other state, then something was left unclosed
            transition(YYINITIAL);
            return new Symbol(TokenConstants.ERROR,
                    String.format("EOF in state %d.", yy_lexical_state));
    }
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
    nesting.push("(");
    return new Symbol(TokenConstants.LPAREN);
}
					case -3:
						break;
					case 3:
						{
    return new Symbol(TokenConstants.MULT);
}
					case -4:
						break;
					case 4:
						{
    return new Symbol(TokenConstants.ERROR, "\000");
}
					case -5:
						break;
					case 5:
						{
    if (nesting.size() != 0 && "(".equals(nesting.pop())) {
        return new Symbol(TokenConstants.RPAREN);
    }
    return new Symbol(TokenConstants.ERROR, "Unmatched ')'");
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
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -8:
						break;
					case 8:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -9:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.COLON);
}
					case -10:
						break;
					case 10:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -11:
						break;
					case 11:
						{
    nesting.push("{");
    return new Symbol(TokenConstants.LBRACE);
}
					case -12:
						break;
					case 12:
						{ 
    // Anything unmatched
    System.err.printf("UNMATCHED: %s (State %d)\n", yytext(), yy_lexical_state);
}
					case -13:
						break;
					case 13:
						{
    return new Symbol(TokenConstants.DOT);
}
					case -14:
						break;
					case 14:
						{
    if (nesting.size() != 0 && "{".equals(nesting.pop())) {
        return new Symbol(TokenConstants.RBRACE);
    }
    return new Symbol(TokenConstants.ERROR, "Unmatched '}'");
}
					case -15:
						break;
					case 15:
						{
    return new Symbol(TokenConstants.EQ);
}
					case -16:
						break;
					case 16:
						{
    transition(STRING);
    nesting.push("\"");
}
					case -17:
						break;
					case 17:
						{
    return new Symbol(TokenConstants.LT);
}
					case -18:
						break;
					case 18:
						{
    return new Symbol(TokenConstants.MINUS);
}
					case -19:
						break;
					case 19:
						{
    return new Symbol(TokenConstants.INT_CONST,
                      AbstractTable.inttable.addString(yytext()));
}
					case -20:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.COMMA);
}
					case -21:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.PLUS);
}
					case -22:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.DIV);
}
					case -23:
						break;
					case 23:
						{
    transition(COMMENT);
    nesting.push("(*");
}
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -25:
						break;
					case 25:
						{
    return new Symbol(TokenConstants.FI);
}
					case -26:
						break;
					case 26:
						{
    return new Symbol(TokenConstants.IF);
}
					case -27:
						break;
					case 27:
						{
    return new Symbol(TokenConstants.IN);
}
					case -28:
						break;
					case 28:
						{
    return new Symbol(TokenConstants.OF);
}
					case -29:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.DARROW);
}
					case -30:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.LE);
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.LET);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -35:
						break;
					case 35:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -36:
						break;
					case 36:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -37:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -39:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.BOOL_CONST, true);
}
					case -40:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -41:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.BOOL_CONST, false);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -47:
						break;
					case 47:
						{
    // Ignore anything in a comment
}
					case -48:
						break;
					case 48:
						{
    if (nesting.size() != 0 && "(*".equals(nesting.pop())) {
        if (nesting.size() == 0 || !"(*".equals(nesting.peek())) {
            transition(YYINITIAL); // Exiting a comment
        }
    } else {
        // In comment without open comment in `nesting`, something's fishy.
        return new Symbol(TokenConstants.ERROR, "Unmatched *)");
    }
}
					case -49:
						break;
					case 49:
						{
    sb.append(yytext());
}
					case -50:
						break;
					case 50:
						{
    // End of string
    if ("\"".equals(nesting.pop())) {
        if (sb.length() < MAX_STR_CONST) {
            AbstractSymbol string = AbstractTable.idtable.addString(sb.toString());
            sb.setLength(0);
            transition(YYINITIAL);
            return new Symbol(TokenConstants.STR_CONST, string);
        }
        else {
            return new Symbol(TokenConstants.ERROR, "String constant too long");
        }
    }
    return new Symbol(TokenConstants.ERROR, "Unmatched '\"'");
}
					case -51:
						break;
					case 51:
						{
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -52:
						break;
					case 52:
						{
    sb.append(yytext().charAt(1));
}
					case -53:
						break;
					case 53:
						{
    switch (yytext().charAt(1)) {
        case 'b':
            sb.append("\b");
            break;
        case 't':
            sb.append("\t");
            break;
        case 'n':
            sb.append("\n");
            break;
        case 'f':
            sb.append("\f");
            break;
        default:
            return new Symbol(TokenConstants.ERROR,
                              "Somehow matched against " + yytext());
    }
}
					case -54:
						break;
					case 55:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -55:
						break;
					case 56:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -56:
						break;
					case 57:
						{
    return new Symbol(TokenConstants.FI);
}
					case -57:
						break;
					case 58:
						{
    return new Symbol(TokenConstants.IF);
}
					case -58:
						break;
					case 59:
						{
    return new Symbol(TokenConstants.IN);
}
					case -59:
						break;
					case 60:
						{
    return new Symbol(TokenConstants.OF);
}
					case -60:
						break;
					case 61:
						{
    return new Symbol(TokenConstants.LET);
}
					case -61:
						break;
					case 62:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -62:
						break;
					case 63:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -63:
						break;
					case 64:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -64:
						break;
					case 65:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -65:
						break;
					case 66:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -66:
						break;
					case 67:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -67:
						break;
					case 68:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -68:
						break;
					case 69:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -69:
						break;
					case 70:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -70:
						break;
					case 71:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -71:
						break;
					case 72:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -72:
						break;
					case 73:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -73:
						break;
					case 74:
						{
    // Ignore anything in a comment
}
					case -74:
						break;
					case 75:
						{
    sb.append(yytext());
}
					case -75:
						break;
					case 77:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -76:
						break;
					case 78:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -77:
						break;
					case 79:
						{
    // Ignore anything in a comment
}
					case -78:
						break;
					case 80:
						{
    sb.append(yytext());
}
					case -79:
						break;
					case 81:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -80:
						break;
					case 82:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -81:
						break;
					case 83:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -82:
						break;
					case 84:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -83:
						break;
					case 85:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -84:
						break;
					case 86:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -85:
						break;
					case 87:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -86:
						break;
					case 88:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -87:
						break;
					case 89:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -88:
						break;
					case 90:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -89:
						break;
					case 91:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -90:
						break;
					case 92:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -91:
						break;
					case 93:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -92:
						break;
					case 94:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -93:
						break;
					case 95:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -94:
						break;
					case 96:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -95:
						break;
					case 97:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -96:
						break;
					case 98:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -97:
						break;
					case 99:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -98:
						break;
					case 100:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -99:
						break;
					case 101:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -100:
						break;
					case 102:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -101:
						break;
					case 103:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -102:
						break;
					case 104:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -103:
						break;
					case 105:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -104:
						break;
					case 106:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -105:
						break;
					case 107:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -106:
						break;
					case 108:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -107:
						break;
					case 109:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -108:
						break;
					case 110:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -109:
						break;
					case 111:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -110:
						break;
					case 112:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -111:
						break;
					case 113:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -112:
						break;
					case 114:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -113:
						break;
					case 115:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -114:
						break;
					case 116:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -115:
						break;
					case 117:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -116:
						break;
					case 118:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -117:
						break;
					case 119:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -118:
						break;
					case 120:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -119:
						break;
					case 121:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -120:
						break;
					case 122:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -121:
						break;
					case 123:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -122:
						break;
					case 124:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -123:
						break;
					case 125:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -124:
						break;
					case 126:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -125:
						break;
					case 127:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -126:
						break;
					case 128:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -127:
						break;
					case 129:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -128:
						break;
					case 130:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -129:
						break;
					case 131:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -130:
						break;
					case 132:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -131:
						break;
					case 133:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -132:
						break;
					case 134:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -133:
						break;
					case 135:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -134:
						break;
					case 136:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -135:
						break;
					case 137:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -136:
						break;
					case 138:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -137:
						break;
					case 139:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -138:
						break;
					case 140:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -139:
						break;
					case 141:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -140:
						break;
					case 142:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -141:
						break;
					case 143:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -142:
						break;
					case 144:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -143:
						break;
					case 145:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -144:
						break;
					case 146:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -145:
						break;
					case 147:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -146:
						break;
					case 148:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -147:
						break;
					case 149:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -148:
						break;
					case 150:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -149:
						break;
					case 151:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -150:
						break;
					case 152:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -151:
						break;
					case 153:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -152:
						break;
					case 154:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -153:
						break;
					case 155:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -154:
						break;
					case 156:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -155:
						break;
					case 157:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -156:
						break;
					case 158:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -157:
						break;
					case 159:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -158:
						break;
					case 160:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -159:
						break;
					case 161:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -160:
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
