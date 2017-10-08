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

%%

%state CLASS
%state CLASS_DEC
%state COMMENT
%state EXPR
%state EXPR_STRING
%state EXPR_BLOCK
%state FEATURE
%state FUNCTION
%state FUNCTION_PARAMETERS
%state STRING
%state INT

%char  /* int yychar; */
%line  /* int yyline; */

\w = [a-zA-Z0-9_]
\s = [ \n\f\r\t\v]
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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    nesting.push(new NestedState("", YYINITIAL));

%init}

%eofval{

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

%eofval}

%class CoolLexer
%cup

%%

<YYINITIAL>[cC][lL][aA][sS][sS] {
    transition(CLASS_DEC);
    return new Symbol(TokenConstants.CLASS);
}

[eE][lL][sS][eE] {
    return new Symbol(TokenConstants.ELSE);
}

t[rR][uU][eE] {
    return new Symbol(TokenConstants.BOOL_CONST, true);
}

f[aA][lL][sS][eE] {
    return new Symbol(TokenConstants.BOOL_CONST, false);
}

<CLASS_DEC>inherits {
    return new Symbol(TokenConstants.INHERITS);
}

"(*" {
    transition(COMMENT);
    System.out.println("(*");
    nesting.push(new NestedState("(*", COMMENT));
}

<COMMENT>"*)" {
    if ("(*".equals(nesting.pop().prefix)) {
        transition(nesting.peek().state);
        System.out.println("*)");
    } else {
        // In comment without open comment in `nesting`, something's fishy.
        return new Symbol(TokenConstants.ERROR);
    }
}

<COMMENT>[^] {
    // Ignore anything in a comment
}

<CLASS_DEC,CLASS,FUNCTION_PARAMETERS>":" {
    return new Symbol(TokenConstants.COLON);
}

<CLASS,EXPR_BLOCK>";" {
    return new Symbol(TokenConstants.SEMI);
}

<CLASS_DEC>"{" {
    transition(CLASS);
    nesting.push(new NestedState("{", yy_lexical_state));
    return new Symbol(TokenConstants.LBRACE);
}

<CLASS_DEC,CLASS,FUNCTION_PARAMETERS>{\TYPEID} {
    return new Symbol(TokenConstants.TYPEID,
                      AbstractTable.idtable.addString(yytext()));
}

<FUNCTION,EXPR_BLOCK>"." {
    return new Symbol(TokenConstants.DOT);
}

<CLASS>"(" {
    transition(FUNCTION_PARAMETERS);
    nesting.push(new NestedState("(", yy_lexical_state));
    return new Symbol(TokenConstants.LPAREN);
}

"(" {
    nesting.push(new NestedState("(", yy_lexical_state));
    return new Symbol(TokenConstants.LPAREN);
}

<FUNCTION_PARAMETERS>")" {
    if ("(".equals(nesting.pop().prefix)) {
        transition(nesting.peek().state);
        return new Symbol(TokenConstants.RPAREN);
    }
}

<CLASS,FUNCTION_PARAMETERS,FUNCTION,EXPR_BLOCK>{\OBJECTID} {
    return new Symbol(TokenConstants.OBJECTID,
                      AbstractTable.idtable.addString(yytext()));
}

<CLASS>"{" {
    transition(FUNCTION);
    nesting.push(new NestedState("{", yy_lexical_state));
    return new Symbol(TokenConstants.LBRACE);
}

<FUNCTION>"{" {
    transition(EXPR_BLOCK);
    nesting.push(new NestedState("{", yy_lexical_state));
    return new Symbol(TokenConstants.LBRACE);
}

"{" {
    nesting.push(new NestedState("{", yy_lexical_state));
    return new Symbol(TokenConstants.LBRACE);
}

")" {
    if (!"(".equals(nesting.pop().prefix) || nesting.peek() == null) {
        return new Symbol(TokenConstants.ERROR, "Unmatched ')'");
    }

    transition(nesting.peek().state);
    return new Symbol(TokenConstants.RPAREN);
}

"}" {
    if (!"{".equals(nesting.pop().prefix) || nesting.peek() == null) {
        return new Symbol(TokenConstants.ERROR, "Unmatched '}'");
    }

    transition(nesting.peek().state);
    return new Symbol(TokenConstants.RBRACE);
}

<STRING> \" {
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

<STRING>\0 {
    return new Symbol(TokenConstants.ERROR, "String contains null character");
}

<STRING>\n {
    return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}

<STRING>. {
    sb.append(yytext());
}

\" {
    transition(STRING);
    nesting.push(new NestedState("\"", yy_lexical_state));
}

<EXPR_BLOCK>"<-" {
    return new Symbol(TokenConstants.ASSIGN);
}

[0-9]+ {
    return new Symbol(TokenConstants.INT_CONST,
                      AbstractTable.inttable.addString(yytext()));
}

"," {
    return new Symbol(TokenConstants.COMMA);
}

"=" {
    return new Symbol(TokenConstants.EQ);
}

"-" {
    return new Symbol(TokenConstants.MINUS);
}

"+" {
    return new Symbol(TokenConstants.PLUS);
}

"*" {
    return new Symbol(TokenConstants.MULT);
}

"/" {
    return new Symbol(TokenConstants.DIV);
}

{\s} {
    // whitespace
}

. { 
    // Anything unmatched
    System.err.printf("UNMATCHED: %s (State %d)\n", yytext(), yy_lexical_state);
}
