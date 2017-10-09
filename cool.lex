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

%%

%state COMMENT
%state STRING
%state STRING_ERROR

%char  /* int yychar; */
%line  /* int yyline; */

\w = [a-zA-Z0-9_]
\s = [ \n\f\r\t\v\013]
\OBJECTID = [a-z]{\w}*
\TYPEID = [A-Z]{\w}*
\FORMAL = \ID{\s}+:{\s}+{\TYPE}

%{

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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

%init}

%eofval{

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

%eofval}

%class CoolLexer
%cup

%%

<YYINITIAL,COMMENT>"(*" {
    transition(COMMENT);
    nesting.push("(*");
}

<COMMENT>"*)" {

    if (nesting.size() != 0 && "(*".equals(nesting.pop())) {
        if (nesting.size() == 0 || !"(*".equals(nesting.peek())) {
            transition(YYINITIAL); // Exiting a comment
        }
    } else {
        // In comment without open comment in `nesting`, something's fishy.
        return new Symbol(TokenConstants.ERROR, "Unmatched *)");
    }
}

<YYINITIAL>"*)" {
    return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}

<YYINITIAL>\\ {
    return new Symbol(TokenConstants.ERROR, "\\");
}

<COMMENT>[^] {
    // Ignore anything in a comment
}

<YYINITIAL>\-\-.* {
    // Inline comment
}

<YYINITIAL>[cC][lL][aA][sS][sS] {
    return new Symbol(TokenConstants.CLASS);
}

<YYINITIAL>[eE][lL][sS][eE] {
    return new Symbol(TokenConstants.ELSE);
}

<YYINITIAL>t[rR][uU][eE] {
    return new Symbol(TokenConstants.BOOL_CONST, true);
}

<YYINITIAL>f[aA][lL][sS][eE] {
    return new Symbol(TokenConstants.BOOL_CONST, false);
}

<YYINITIAL>[fF][iI] {
    return new Symbol(TokenConstants.FI);
}

<YYINITIAL>[iI][fF] {
    return new Symbol(TokenConstants.IF);
}

<YYINITIAL>[iI][nN] {
    return new Symbol(TokenConstants.IN);
}

<YYINITIAL>[iI][nN][hH][eE][rR][iI][tT][sS] {
    return new Symbol(TokenConstants.INHERITS);
}

<YYINITIAL>[iI][sS][vV][oO][iI][dD] {
    return new Symbol(TokenConstants.ISVOID);
}

<YYINITIAL>[lL][eE][tT] {
    return new Symbol(TokenConstants.LET);
}

<YYINITIAL>[lL][oO][oO][pP] {
    return new Symbol(TokenConstants.LOOP);
}

<YYINITIAL>[pP][oO][oO][lL] {
    return new Symbol(TokenConstants.POOL);
}

<YYINITIAL>[tT][hH][eE][nN] {
    return new Symbol(TokenConstants.THEN);
}

<YYINITIAL>[wW][hH][iI][lL][eE] {
    return new Symbol(TokenConstants.WHILE);
}

<YYINITIAL>[cC][aA][sS][eE] {
    return new Symbol(TokenConstants.CASE);
}

<YYINITIAL>[eE][sS][aA][cC] {
    return new Symbol(TokenConstants.ESAC);
}

<YYINITIAL>[nN][eE][wW] {
    return new Symbol(TokenConstants.NEW);
}

<YYINITIAL>[oO][fF] {
    return new Symbol(TokenConstants.OF);
}

<YYINITIAL>[nN][oO][tT] {
    return new Symbol(TokenConstants.NOT);
}

<YYINITIAL>":" {
    return new Symbol(TokenConstants.COLON);
}

<YYINITIAL>";" {
    return new Symbol(TokenConstants.SEMI);
}

<YYINITIAL>"{" {
    nesting.push("{");
    return new Symbol(TokenConstants.LBRACE);
}

<YYINITIAL>{\TYPEID} {
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}

<YYINITIAL>"." {
    return new Symbol(TokenConstants.DOT);
}

<YYINITIAL>"(" {
    return new Symbol(TokenConstants.LPAREN);
}

<YYINITIAL>")" {
    return new Symbol(TokenConstants.RPAREN);
}

<YYINITIAL>{\OBJECTID} {
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}

<YYINITIAL>"{" {
    nesting.push("{");
    return new Symbol(TokenConstants.LBRACE);
}

<YYINITIAL>"}" {
    if (nesting.size() != 0 && "{".equals(nesting.pop())) {
        return new Symbol(TokenConstants.RBRACE);
    }
    return new Symbol(TokenConstants.ERROR, "Unmatched '}'");
}

<YYINITIAL>"=>" {
    return new Symbol(TokenConstants.DARROW);
}

<STRING>"\"" {
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

<STRING>\\\n {
    sb.append("\n");
}

<STRING>\\[btnf] {
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

<STRING>\\\\ {
    sb.append("\\");
}

<STRING>\\\0 {
    transition(STRING_ERROR);
    return new Symbol(TokenConstants.ERROR, "String contains escaped null character.");
}

<STRING>\0 {
    transition(STRING_ERROR);
    return new Symbol(TokenConstants.ERROR, "String contains null character.");
}

<STRING_ERROR>\n|\" {
    transition(YYINITIAL);
}

<STRING_ERROR>. {
}

<STRING>\\. {
    sb.append(yytext().charAt(1));
}

<STRING>\n {
    transition(YYINITIAL);
    lineno_shift = 1;
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}

<STRING>\015 {
    sb.append('\015');
}

<STRING>\033 {
    sb.append('\033');
}

<STRING>. {
    sb.append(yytext());
}

\0 {
    return new Symbol(TokenConstants.ERROR, "\000");
}

<YYINITIAL>"\"" {
    transition(STRING);
    nesting.push("\"");
    sb.setLength(0);
}

<YYINITIAL>"<-" {
    return new Symbol(TokenConstants.ASSIGN);
}

<YYINITIAL>[0-9]+ {
    return new Symbol(TokenConstants.INT_CONST,
                      AbstractTable.inttable.addString(yytext()));
}

<YYINITIAL>"~" {
    return new Symbol(TokenConstants.NEG);
}

<YYINITIAL>"@" {
    return new Symbol(TokenConstants.AT);
}

<YYINITIAL>"," {
    return new Symbol(TokenConstants.COMMA);
}

<YYINITIAL>"=" {
    return new Symbol(TokenConstants.EQ);
}

<YYINITIAL>"<" {
    return new Symbol(TokenConstants.LT);
}

<YYINITIAL>"<=" {
    return new Symbol(TokenConstants.LE);
}

<YYINITIAL>"-" {
    return new Symbol(TokenConstants.MINUS);
}

<YYINITIAL>"+" {
    return new Symbol(TokenConstants.PLUS);
}

<YYINITIAL>"*" {
    return new Symbol(TokenConstants.MULT);
}

<YYINITIAL>"/" {
    return new Symbol(TokenConstants.DIV);
}

{\s} {
    // whitespace
}

. { 
    return new Symbol(TokenConstants.ERROR, yytext());
}
