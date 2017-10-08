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

%state CLASS
%state CLASS_DEC
%state COMMENT
%state EXPR
%state EXPR_STRING
%state FEATURE
%state FEATURE_DEC
%state FORMAL

%char  /* int yychar; */
%line  /* int yyline; */

\DIGIT = [0-9]
\ID = [a-z]\w*
\TYPE = [A-Z]\w*
\FORMAL = \ID\s+:\s+\TYPE

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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    last_state = yy_lexical_state;

%init}

%eofval{

    switch(yy_lexical_state) {
        case YYINITIAL: // Expected
            break;
        default: // If in any other state, then something was left unclosed
            return new Symbol(TokenConstants.error);
    }
    return new Symbol(TokenConstants.EOF);

%eofval}

%class CoolLexer
%cup

%%

<YYINITIAL,CLASS,COMMENT>" \n\f\r\t\v" {
    // Eat whitespace where relevant
    return null;
}

"(*" {
    transition(COMMENT);
    nesting.push("(*");
}

<COMMENT>"*)" {
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

<COMMENT>. {
    // Ignore anything in a comment
    System.out.println("Found comment content!");
}

<YYINITIAL>class {
    transition(CLASS_DEC);
    return new Symbol(TokenConstants.CLASS);
}

<CLASS_DEC>inherits {
    return new Symbol(TokenConstants.INHERITS);
}

<CLASS_DEC>\TYPE {
    return new Symbol(TokenConstants.TYPEID);
}

<YYINITIAL>"=>" {
    /* Sample lexical rule for "=>" arrow.
       Further lexical rules should be defined
       here, after the last %% separator */
    return new Symbol(TokenConstants.DARROW);
}

. { /* This rule should be the very last
       in your lexical specification and will match match everything not
       matched by other lexical rules. */
    System.out.printf("State: %s", yy_lexical_state);
    System.err.println("LEXER BUG - UNMATCHED: " + yytext());
}
