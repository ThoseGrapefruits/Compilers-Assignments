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
    String string_error = null;
    int lineno_shift = 0;
    int get_curr_lineno() {
        int lineno = yyline + 1 + lineno_shift;
        lineno_shift = 0;
	return lineno;
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
	private final int STRING_ERROR = 3;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int yy_state_dtrans[] = {
		0,
		66,
		88,
		92
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
		/* 54 */ YY_NO_ANCHOR,
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
		/* 66 */ YY_NOT_ACCEPT,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
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
		/* 88 */ YY_NOT_ACCEPT,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NOT_ACCEPT,
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
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"55,7:8,64,53,64:2,5,7:13,56,7:4,64,7,52,7:5,1,3,2,62,61,6,47,63,58:10,27,28" +
",57,50,51,7,60,30,31,32,33,34,17,31,35,36,31:2,37,31,38,39,40,31,41,42,21,4" +
"3,44,45,31:3,7,4,7:2,46,7,10,54,8,24,12,16,48,20,18,48:2,9,48,19,23,25,48,1" +
"4,11,13,15,22,26,48:3,29,7,49,59,7,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,174,
"0,1,2,3,1:3,4,1,5,6,1:5,7,1:2,8,9,1:7,10,11:2,12,11,1:3,11:15,2,1:14,13,14," +
"15,16:2,17,16:14,18,19,20,21,22,1,23,24,25,26,27,28,29,30,31,32,33,34,35,36" +
",37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61" +
",62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86" +
",87,88,89,90,91,92,93,11,16,94,95,96,97,98,99,100,101,102")[0];

	private int yy_nxt[][] = unpackFromString(103,65,
"1,2,3,4,5,6,7,8,9,123,163:2,165,167,163:2,67,10,89,125,163,68,163,93,163,16" +
"9,171,11,12,13,164:2,166,164,168,164,90,124,126,94,170,164:4,172,8,14,163,1" +
"5,16,8,17,6,163,18,8,19,20,21,22,23,24,25,6,-1:67,26,-1:65,27,-1:67,28,-1:6" +
"6,163,173,127,163:16,-1:3,127,163:6,173,163:9,-1,163,-1:5,163,-1:3,163,-1:1" +
"4,164:10,69,164:8,-1:3,164:6,69,164:10,-1,164,-1:5,164,-1:3,164,-1:57,33,-1" +
":19,34,-1:43,35,-1:72,20,-1:7,28:4,-1,28:47,-1,28:11,-1:8,163:19,-1:3,163:1" +
"7,-1,163,-1:5,163,-1:3,163,-1:14,163:12,153,163:6,-1:3,163:5,153,163:11,-1," +
"163,-1:5,163,-1:3,163,-1:6,1,51,86,91:62,-1:8,163:2,139,163:7,29,163:8,-1:3" +
",139,163:5,29,163:10,-1,163,-1:5,163,-1:3,163,-1:14,164:12,128,164:6,-1:3,1" +
"64:5,128,164:11,-1,164,-1:5,164,-1:3,164,-1:14,164:19,-1:3,164:17,-1,164,-1" +
":5,164,-1:3,164,-1:14,164:12,150,164:6,-1:3,164:5,150,164:11,-1,164,-1:5,16" +
"4,-1:3,164,-1:9,52,-1:62,59:3,60,-1,59:7,61,59:2,61,59:2,61,59:33,62,61,63," +
"59:9,1,53:3,87,54,53:46,55,56,53,57,58,53:8,-1:8,163:3,141,163:4,30:2,163,3" +
"1,163:7,-1:3,163:8,31,163:3,141,163:4,-1,163,-1:5,163,-1:3,163,-1:14,164:3," +
"138,164:4,70:2,164,71,164:7,-1:3,164:8,71,164:3,138,164:4,-1,164,-1:5,164,-" +
"1:3,164,-1:6,1,64:4,6,64:46,65:2,64:11,-1:8,163:8,32:2,163:9,-1:3,163:17,-1" +
",163,-1:5,163,-1:3,163,-1:14,164:8,72:2,164:9,-1:3,164:17,-1,164,-1:5,164,-" +
"1:3,164,-1:14,163:5,36,163:7,36,163:5,-1:3,163:17,-1,163,-1:5,163,-1:3,163," +
"-1:14,164:5,73,164:7,73,164:5,-1:3,164:17,-1,164,-1:5,164,-1:3,164,-1:14,16" +
"3:18,37,-1:3,163:15,37,163,-1,163,-1:5,163,-1:3,163,-1:14,164:18,74,-1:3,16" +
"4:15,74,164,-1,164,-1:5,164,-1:3,164,-1:14,163:5,38,163:7,38,163:5,-1:3,163" +
":17,-1,163,-1:5,163,-1:3,163,-1:14,164:5,75,164:7,75,164:5,-1:3,164:17,-1,1" +
"64,-1:5,164,-1:3,164,-1:14,163:4,39,163:14,-1:3,163:4,39,163:12,-1,163,-1:5" +
",163,-1:3,163,-1:14,164:11,80,164:7,-1:3,164:8,80,164:8,-1,164,-1:5,164,-1:" +
"3,164,-1:14,163:17,40,163,-1:3,163:10,40,163:6,-1,163,-1:5,163,-1:3,163,-1:" +
"14,164:4,76,164:14,-1:3,164:4,76,164:12,-1,164,-1:5,164,-1:3,164,-1:14,163:" +
"4,41,163:14,-1:3,163:4,41,163:12,-1,163,-1:5,163,-1:3,163,-1:14,164:4,78,16" +
"4:14,-1:3,164:4,78,164:12,-1,164,-1:5,164,-1:3,164,-1:14,42,163:18,-1:3,163" +
":2,42,163:14,-1,163,-1:5,163,-1:3,163,-1:14,79,164:18,-1:3,164:2,79,164:14," +
"-1,164,-1:5,164,-1:3,164,-1:14,163:4,43,163:14,-1:3,163:4,43,163:12,-1,163," +
"-1:5,163,-1:3,163,-1:14,164:17,77,164,-1:3,164:10,77,164:6,-1,164,-1:5,164," +
"-1:3,164,-1:14,163:11,44,163:7,-1:3,163:8,44,163:8,-1,163,-1:5,163,-1:3,163" +
",-1:14,164,81,164:17,-1:3,164:7,81,164:9,-1,164,-1:5,164,-1:3,164,-1:14,163" +
",45,163:17,-1:3,163:7,45,163:9,-1,163,-1:5,163,-1:3,163,-1:14,164:3,82,164:" +
"15,-1:3,164:12,82,164:4,-1,164,-1:5,164,-1:3,164,-1:14,163:3,46,163:15,-1:3" +
",163:12,46,163:4,-1,163,-1:5,163,-1:3,163,-1:14,164:4,83,164:14,-1:3,164:4," +
"83,164:12,-1,164,-1:5,164,-1:3,164,-1:14,163:4,47,163:14,-1:3,163:4,47,163:" +
"12,-1,163,-1:5,163,-1:3,163,-1:14,164:16,84,164:2,-1:3,164:3,84,164:13,-1,1" +
"64,-1:5,164,-1:3,164,-1:14,163:4,48,163:14,-1:3,163:4,48,163:12,-1,163,-1:5" +
",163,-1:3,163,-1:14,164:3,85,164:15,-1:3,164:12,85,164:4,-1,164,-1:5,164,-1" +
":3,164,-1:14,163:16,49,163:2,-1:3,163:3,49,163:13,-1,163,-1:5,163,-1:3,163," +
"-1:14,163:3,50,163:15,-1:3,163:12,50,163:4,-1,163,-1:5,163,-1:3,163,-1:14,1" +
"63:4,95,163:10,129,163:3,-1:3,163:4,95,163:4,129,163:7,-1,163,-1:5,163,-1:3" +
",163,-1:14,164:4,96,164:10,140,164:3,-1:3,164:4,96,164:4,140,164:7,-1,164,-" +
"1:5,164,-1:3,164,-1:14,163:4,97,163:10,99,163:3,-1:3,163:4,97,163:4,99,163:" +
"7,-1,163,-1:5,163,-1:3,163,-1:14,164:4,98,164:10,100,164:3,-1:3,164:4,98,16" +
"4:4,100,164:7,-1,164,-1:5,164,-1:3,164,-1:14,163:3,101,163:15,-1:3,163:12,1" +
"01,163:4,-1,163,-1:5,163,-1:3,163,-1:14,164:4,102,164:14,-1:3,164:4,102,164" +
":12,-1,164,-1:5,164,-1:3,164,-1:14,163:15,103,163:3,-1:3,163:9,103,163:7,-1" +
",163,-1:5,163,-1:3,163,-1:14,164:2,146,164:16,-1:3,146,164:16,-1,164,-1:5,1" +
"64,-1:3,164,-1:14,163:3,105,163:15,-1:3,163:12,105,163:4,-1,163,-1:5,163,-1" +
":3,163,-1:14,164:3,104,164:15,-1:3,164:12,104,164:4,-1,164,-1:5,164,-1:3,16" +
"4,-1:14,163:2,107,163:16,-1:3,107,163:16,-1,163,-1:5,163,-1:3,163,-1:14,164" +
":3,106,164:15,-1:3,164:12,106,164:4,-1,164,-1:5,164,-1:3,164,-1:14,163:7,10" +
"9,163:11,-1:3,163:13,109,163:3,-1,163,-1:5,163,-1:3,163,-1:14,164:2,108,164" +
":16,-1:3,108,164:16,-1,164,-1:5,164,-1:3,164,-1:14,163:4,111,163:14,-1:3,16" +
"3:4,111,163:12,-1,163,-1:5,163,-1:3,163,-1:14,164:14,148,164:4,-1:3,164:14," +
"148,164:2,-1,164,-1:5,164,-1:3,164,-1:14,163,149,163:17,-1:3,163:7,149,163:" +
"9,-1,163,-1:5,163,-1:3,163,-1:14,164:15,110,164:3,-1:3,164:9,110,164:7,-1,1" +
"64,-1:5,164,-1:3,164,-1:14,163:14,151,163:4,-1:3,163:14,151,163:2,-1,163,-1" +
":5,163,-1:3,163,-1:14,164:15,112,164:3,-1:3,164:9,112,164:7,-1,164,-1:5,164" +
",-1:3,164,-1:14,163:15,113,163:3,-1:3,163:9,113,163:7,-1,163,-1:5,163,-1:3," +
"163,-1:14,164:10,152,164:8,-1:3,164:6,152,164:10,-1,164,-1:5,164,-1:3,164,-" +
"1:14,163:10,155,163:8,-1:3,163:6,155,163:10,-1,163,-1:5,163,-1:3,163,-1:14," +
"164:3,114,164:15,-1:3,164:12,114,164:4,-1,164,-1:5,164,-1:3,164,-1:14,163:3" +
",115,163:15,-1:3,163:12,115,163:4,-1,163,-1:5,163,-1:3,163,-1:14,164:15,154" +
",164:3,-1:3,164:9,154,164:7,-1,164,-1:5,164,-1:3,164,-1:14,163:3,117,163:15" +
",-1:3,163:12,117,163:4,-1,163,-1:5,163,-1:3,163,-1:14,164:4,156,164:14,-1:3" +
",164:4,156,164:12,-1,164,-1:5,164,-1:3,164,-1:14,163:15,157,163:3,-1:3,163:" +
"9,157,163:7,-1,163,-1:5,163,-1:3,163,-1:14,164,116,164:17,-1:3,164:7,116,16" +
"4:9,-1,164,-1:5,164,-1:3,164,-1:14,163:4,159,163:14,-1:3,163:4,159,163:12,-" +
"1,163,-1:5,163,-1:3,163,-1:14,164:10,118,164:8,-1:3,164:6,118,164:10,-1,164" +
",-1:5,164,-1:3,164,-1:14,163,119,163:17,-1:3,163:7,119,163:9,-1,163,-1:5,16" +
"3,-1:3,163,-1:14,164:6,158,164:12,-1:3,164:11,158,164:5,-1,164,-1:5,164,-1:" +
"3,164,-1:14,163:10,121,163:8,-1:3,163:6,121,163:10,-1,163,-1:5,163,-1:3,163" +
",-1:14,164:10,160,164:8,-1:3,164:6,160,164:10,-1,164,-1:5,164,-1:3,164,-1:1" +
"4,163:6,161,163:12,-1:3,163:11,161,163:5,-1,163,-1:5,163,-1:3,163,-1:14,164" +
":5,120,164:7,120,164:5,-1:3,164:17,-1,164,-1:5,164,-1:3,164,-1:14,163:10,16" +
"2,163:8,-1:3,163:6,162,163:10,-1,163,-1:5,163,-1:3,163,-1:14,163:5,122,163:" +
"7,122,163:5,-1:3,163:17,-1,163,-1:5,163,-1:3,163,-1:14,163,131,163,133,163:" +
"15,-1:3,163:7,131,163:4,133,163:4,-1,163,-1:5,163,-1:3,163,-1:14,164,130,13" +
"2,164:16,-1:3,132,164:6,130,164:9,-1,164,-1:5,164,-1:3,164,-1:14,163:6,135," +
"163:5,137,163:6,-1:3,163:5,137,163:5,135,163:5,-1,163,-1:5,163,-1:3,163,-1:" +
"14,164,134,164,136,164:15,-1:3,164:7,134,164:4,136,164:4,-1,164,-1:5,164,-1" +
":3,164,-1:14,163:15,143,163:3,-1:3,163:9,143,163:7,-1,163,-1:5,163,-1:3,163" +
",-1:14,164:15,142,164:3,-1:3,164:9,142,164:7,-1,164,-1:5,164,-1:3,164,-1:14" +
",163:12,145,163:6,-1:3,163:5,145,163:11,-1,163,-1:5,163,-1:3,163,-1:14,164:" +
"12,144,164:6,-1:3,164:5,144,164:11,-1,164,-1:5,164,-1:3,164,-1:14,163:2,147" +
",163:16,-1:3,147,163:16,-1,163,-1:5,163,-1:3,163,-1:6");

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
        case STRING_ERROR:
        case YYINITIAL: // Expected
            break;
        case COMMENT:
            transition(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in comment");
        case STRING:
            transition(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in String");
        default: // If in any other state, then something was left unclosed
            transition(YYINITIAL);
            return new Symbol(TokenConstants.ERROR, "EOF in non-initial state");
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
    return new Symbol(TokenConstants.RPAREN);
}
					case -5:
						break;
					case 5:
						{
    return new Symbol(TokenConstants.ERROR, "\\");
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
    return new Symbol(TokenConstants.MINUS);
}
					case -8:
						break;
					case 8:
						{ 
    return new Symbol(TokenConstants.ERROR, yytext());
}
					case -9:
						break;
					case 9:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -10:
						break;
					case 10:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -11:
						break;
					case 11:
						{
    return new Symbol(TokenConstants.COLON);
}
					case -12:
						break;
					case 12:
						{
    return new Symbol(TokenConstants.SEMI);
}
					case -13:
						break;
					case 13:
						{
    nesting.push("{");
    return new Symbol(TokenConstants.LBRACE);
}
					case -14:
						break;
					case 14:
						{
    return new Symbol(TokenConstants.DOT);
}
					case -15:
						break;
					case 15:
						{
    if (nesting.size() != 0 && "{".equals(nesting.pop())) {
        return new Symbol(TokenConstants.RBRACE);
    }
    return new Symbol(TokenConstants.ERROR, "Unmatched '}'");
}
					case -16:
						break;
					case 16:
						{
    return new Symbol(TokenConstants.EQ);
}
					case -17:
						break;
					case 17:
						{
    transition(STRING);
    nesting.push("\"");
    sb.setLength(0);
}
					case -18:
						break;
					case 18:
						{
    return new Symbol(TokenConstants.ERROR, "\000");
}
					case -19:
						break;
					case 19:
						{
    return new Symbol(TokenConstants.LT);
}
					case -20:
						break;
					case 20:
						{
    return new Symbol(TokenConstants.INT_CONST,
                      AbstractTable.inttable.addString(yytext()));
}
					case -21:
						break;
					case 21:
						{
    return new Symbol(TokenConstants.NEG);
}
					case -22:
						break;
					case 22:
						{
    return new Symbol(TokenConstants.AT);
}
					case -23:
						break;
					case 23:
						{
    return new Symbol(TokenConstants.COMMA);
}
					case -24:
						break;
					case 24:
						{
    return new Symbol(TokenConstants.PLUS);
}
					case -25:
						break;
					case 25:
						{
    return new Symbol(TokenConstants.DIV);
}
					case -26:
						break;
					case 26:
						{
    transition(COMMENT);
    nesting.push("(*");
}
					case -27:
						break;
					case 27:
						{
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -28:
						break;
					case 28:
						{
    // Inline comment
}
					case -29:
						break;
					case 29:
						{
    return new Symbol(TokenConstants.FI);
}
					case -30:
						break;
					case 30:
						{
    return new Symbol(TokenConstants.IF);
}
					case -31:
						break;
					case 31:
						{
    return new Symbol(TokenConstants.IN);
}
					case -32:
						break;
					case 32:
						{
    return new Symbol(TokenConstants.OF);
}
					case -33:
						break;
					case 33:
						{
    return new Symbol(TokenConstants.DARROW);
}
					case -34:
						break;
					case 34:
						{
    return new Symbol(TokenConstants.ASSIGN);
}
					case -35:
						break;
					case 35:
						{
    return new Symbol(TokenConstants.LE);
}
					case -36:
						break;
					case 36:
						{
    return new Symbol(TokenConstants.LET);
}
					case -37:
						break;
					case 37:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -38:
						break;
					case 38:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -39:
						break;
					case 39:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -40:
						break;
					case 40:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -41:
						break;
					case 41:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -42:
						break;
					case 42:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -43:
						break;
					case 43:
						{
    return new Symbol(TokenConstants.BOOL_CONST, true);
}
					case -44:
						break;
					case 44:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -45:
						break;
					case 45:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -46:
						break;
					case 46:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -47:
						break;
					case 47:
						{
    return new Symbol(TokenConstants.BOOL_CONST, false);
}
					case -48:
						break;
					case 48:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -49:
						break;
					case 49:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -50:
						break;
					case 50:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -51:
						break;
					case 51:
						{
    // Ignore anything in a comment
}
					case -52:
						break;
					case 52:
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
					case -53:
						break;
					case 53:
						{
    sb.append(yytext());
}
					case -54:
						break;
					case 54:
						{
    sb.append('\015');
}
					case -55:
						break;
					case 55:
						{
    // End of string
    if ("\"".equals(nesting.pop())) {
        String string = sb.toString();
        transition(YYINITIAL);
        if (string.length() < MAX_STR_CONST) {
            AbstractSymbol symbol = AbstractTable.idtable.addString(string);
            return new Symbol(TokenConstants.STR_CONST, symbol);
        }
        else {
            return new Symbol(TokenConstants.ERROR, "String constant too long");
        }
    }
    return new Symbol(TokenConstants.ERROR, "Unmatched '\"'");
}
					case -56:
						break;
					case 56:
						{
    transition(YYINITIAL);
    lineno_shift = 1;
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -57:
						break;
					case 57:
						{
    transition(STRING_ERROR);
    return new Symbol(TokenConstants.ERROR, "String contains null character.");
}
					case -58:
						break;
					case 58:
						{
    sb.append('\033');
}
					case -59:
						break;
					case 59:
						{
    sb.append(yytext().charAt(1));
}
					case -60:
						break;
					case 60:
						{
    sb.append("\\");
}
					case -61:
						break;
					case 61:
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
					case -62:
						break;
					case 62:
						{
    sb.append("\n");
}
					case -63:
						break;
					case 63:
						{
    transition(STRING_ERROR);
    return new Symbol(TokenConstants.ERROR, "String contains escaped null character.");
}
					case -64:
						break;
					case 64:
						{
}
					case -65:
						break;
					case 65:
						{
    transition(YYINITIAL);
}
					case -66:
						break;
					case 67:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -67:
						break;
					case 68:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -68:
						break;
					case 69:
						{
    return new Symbol(TokenConstants.FI);
}
					case -69:
						break;
					case 70:
						{
    return new Symbol(TokenConstants.IF);
}
					case -70:
						break;
					case 71:
						{
    return new Symbol(TokenConstants.IN);
}
					case -71:
						break;
					case 72:
						{
    return new Symbol(TokenConstants.OF);
}
					case -72:
						break;
					case 73:
						{
    return new Symbol(TokenConstants.LET);
}
					case -73:
						break;
					case 74:
						{
    return new Symbol(TokenConstants.NEW);
}
					case -74:
						break;
					case 75:
						{
    return new Symbol(TokenConstants.NOT);
}
					case -75:
						break;
					case 76:
						{
    return new Symbol(TokenConstants.CASE);
}
					case -76:
						break;
					case 77:
						{
    return new Symbol(TokenConstants.LOOP);
}
					case -77:
						break;
					case 78:
						{
    return new Symbol(TokenConstants.ELSE);
}
					case -78:
						break;
					case 79:
						{
    return new Symbol(TokenConstants.ESAC);
}
					case -79:
						break;
					case 80:
						{
    return new Symbol(TokenConstants.THEN);
}
					case -80:
						break;
					case 81:
						{
    return new Symbol(TokenConstants.POOL);
}
					case -81:
						break;
					case 82:
						{
    return new Symbol(TokenConstants.CLASS);
}
					case -82:
						break;
					case 83:
						{
    return new Symbol(TokenConstants.WHILE);
}
					case -83:
						break;
					case 84:
						{
    return new Symbol(TokenConstants.ISVOID);
}
					case -84:
						break;
					case 85:
						{
    return new Symbol(TokenConstants.INHERITS);
}
					case -85:
						break;
					case 86:
						{
    // Ignore anything in a comment
}
					case -86:
						break;
					case 87:
						{
    sb.append(yytext());
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
    // Ignore anything in a comment
}
					case -90:
						break;
					case 93:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -91:
						break;
					case 94:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -92:
						break;
					case 95:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -93:
						break;
					case 96:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -94:
						break;
					case 97:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -95:
						break;
					case 98:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -96:
						break;
					case 99:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -97:
						break;
					case 100:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -98:
						break;
					case 101:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -99:
						break;
					case 102:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -100:
						break;
					case 103:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -101:
						break;
					case 104:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -102:
						break;
					case 105:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -103:
						break;
					case 106:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -104:
						break;
					case 107:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -105:
						break;
					case 108:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -106:
						break;
					case 109:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -107:
						break;
					case 110:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -108:
						break;
					case 111:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -109:
						break;
					case 112:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -110:
						break;
					case 113:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -111:
						break;
					case 114:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -112:
						break;
					case 115:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -113:
						break;
					case 116:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -114:
						break;
					case 117:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -115:
						break;
					case 118:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -116:
						break;
					case 119:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -117:
						break;
					case 120:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -118:
						break;
					case 121:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -119:
						break;
					case 122:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -120:
						break;
					case 123:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -121:
						break;
					case 124:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -122:
						break;
					case 125:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -123:
						break;
					case 126:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -124:
						break;
					case 127:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -125:
						break;
					case 128:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -126:
						break;
					case 129:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -127:
						break;
					case 130:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -128:
						break;
					case 131:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -129:
						break;
					case 132:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -130:
						break;
					case 133:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -131:
						break;
					case 134:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -132:
						break;
					case 135:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -133:
						break;
					case 136:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -134:
						break;
					case 137:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -135:
						break;
					case 138:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -136:
						break;
					case 139:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -137:
						break;
					case 140:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -138:
						break;
					case 141:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -139:
						break;
					case 142:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -140:
						break;
					case 143:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -141:
						break;
					case 144:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -142:
						break;
					case 145:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -143:
						break;
					case 146:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -144:
						break;
					case 147:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -145:
						break;
					case 148:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -146:
						break;
					case 149:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -147:
						break;
					case 150:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -148:
						break;
					case 151:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -149:
						break;
					case 152:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -150:
						break;
					case 153:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -151:
						break;
					case 154:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -152:
						break;
					case 155:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -153:
						break;
					case 156:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -154:
						break;
					case 157:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -155:
						break;
					case 158:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -156:
						break;
					case 159:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -157:
						break;
					case 160:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -158:
						break;
					case 161:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -159:
						break;
					case 162:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -160:
						break;
					case 163:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -161:
						break;
					case 164:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -162:
						break;
					case 165:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -163:
						break;
					case 166:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -164:
						break;
					case 167:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -165:
						break;
					case 168:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -166:
						break;
					case 169:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -167:
						break;
					case 170:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -168:
						break;
					case 171:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -169:
						break;
					case 172:
						{
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -170:
						break;
					case 173:
						{
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}
					case -171:
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
