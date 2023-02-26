package com.ntukhpi.storozhuk.context.states;

public enum CurrentState {
    PARSING_IDENTIFIER, PARSING_OPERATOR, PARSING_OPERAND, WAITING_FOR_LEXEME, WAITING_FOR_IDENTIFIER, PARSING_OPEN_BRACKET,
    PARSING_CLOSE_BRACKET, PARSING_ASSIGNMENT, WAITING_FOR_ASSIGNMENT
}
